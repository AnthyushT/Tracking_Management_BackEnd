package com.mongodb.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.model.Inventory;
import com.mongodb.model.Users;
import com.mongodb.model.Warehouse;
import com.mongodb.repository.InventoryRepository;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WarehouseService warehouseService;

	public ResponseEntity<?> addInventory(Inventory inventory) {
	    Users user = userService.getUserByEmail(inventory.getEmail());
	    if (user != null) {
	        ResponseEntity<List<Warehouse>> warehouseResponse = warehouseService.getWarehouseByEmail(inventory.getEmail());
	        if (warehouseResponse.getBody().isEmpty()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Please lease a warehouse to keep your inventory"));
	        }
	        Warehouse warehouse = warehouseResponse.getBody().stream()
	            .filter(w -> w.getWarehouseName().equals(inventory.getWarehouseName()))
	            .findFirst()
	            .orElse(null);
	        if (warehouse == null) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "You have not leased the specified warehouse"));
	        }
	        if (warehouse.getWarehouseCapacity() < inventory.getStockLevel()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Not enough capacity in the warehouse to add the inventory"));
	        }
	        inventoryRepository.save(inventory);
	        warehouse.setWarehouseCapacity(warehouse.getWarehouseCapacity() - inventory.getStockLevel());
	        warehouseService.updateWarehouseCapacity(warehouse, warehouse.getWarehouseId());
	        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Inventory added successfully"));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found"));
	    }
	}



	
    public ResponseEntity<List<Inventory>> findAllInventory() {
        return ResponseEntity.ok(inventoryRepository.findAll());
    }

    public ResponseEntity<List<Inventory>> getInventoryByCategory(String itemCategory){
        List<Inventory> inventory = inventoryRepository.findByItemCategory(itemCategory);
        if (!inventory.isEmpty()) {
            return ResponseEntity.ok(inventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<List<Inventory>> getInventoryByCategoryAndEmail(String itemCategory, String email){
        List<Inventory> inventory = inventoryRepository.findByItemCategoryAndEmail(itemCategory, email);
        if (!inventory.isEmpty()) {
            return ResponseEntity.ok(inventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> updateInventory(Inventory inventoryRequest, String inventoryId){
        Optional<Inventory> existingInventory = inventoryRepository.findById(inventoryId);
        if (existingInventory.isPresent()) {
            existingInventory.get().setItemCategory(inventoryRequest.getItemCategory()); 
            existingInventory.get().setStockLevel(inventoryRequest.getStockLevel());
            inventoryRepository.save(existingInventory.get());
            return ResponseEntity.ok(Collections.singletonMap("message", "Inventory updated successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<List<Inventory>> getInventoryByEmail(String email){
        List<Inventory> inventory = inventoryRepository.findByEmail(email);
        if (!inventory.isEmpty()) {
            return ResponseEntity.ok(inventory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<?> decreaseStockLevel(String inventoryId){
        Optional<Inventory> existingInventory = inventoryRepository.findById(inventoryId);
        if (existingInventory.isPresent()) {
            int currentStockLevel = existingInventory.get().getStockLevel();
            if (currentStockLevel > 0) {
                existingInventory.get().setStockLevel(currentStockLevel - 1);
                inventoryRepository.save(existingInventory.get());
                return ResponseEntity.ok(Collections.singletonMap("message", "Stock level decreased successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Stock level is already 0"));
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<?> increaseStockLevel(String inventoryId){
        Optional<Inventory> existingInventory = inventoryRepository.findById(inventoryId);
        if (existingInventory.isPresent()) {
            int currentStockLevel = existingInventory.get().getStockLevel();
            if (currentStockLevel < 1000) {
                existingInventory.get().setStockLevel(currentStockLevel + 1);
                inventoryRepository.save(existingInventory.get());
                return ResponseEntity.ok(Collections.singletonMap("message", "Stock level Increased successfully"));
            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Warehouse is full."));
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<?> transferInventory(String itemCategory, String sourceWarehouseName, String destinationWarehouseName, int stockLevel) {
        // Get the inventory item by category and source warehouse
        List<Inventory> inventories = inventoryRepository.findByItemCategoryAndWarehouseName(itemCategory, sourceWarehouseName);
        Inventory inventory = inventories.stream()
            .findFirst()
            .orElse(null);
		
		  if (inventory == null || inventory.getStockLevel() < stockLevel) { 
			  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Not enough stock in the source warehouse")); }
		 

        // Get the source and destination warehouses
        ResponseEntity<List<Warehouse>> sourceWarehouseResponse = warehouseService.getWarehouseByName(sourceWarehouseName);
        ResponseEntity<List<Warehouse>> destinationWarehouseResponse = warehouseService.getWarehouseByName(destinationWarehouseName);

        // Check if the warehouses exist
        if (sourceWarehouseResponse.getBody() == null || sourceWarehouseResponse.getBody().isEmpty() || 
            destinationWarehouseResponse.getBody() == null || destinationWarehouseResponse.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "One or both warehouses not found"));
        }

        Warehouse sourceWarehouse = sourceWarehouseResponse.getBody().get(0);
        Warehouse destinationWarehouse = destinationWarehouseResponse.getBody().get(0);

        // Check if the destination warehouse has enough capacity
        if (destinationWarehouse.getWarehouseCapacity() - stockLevel < 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Not enough capacity in the destination warehouse"));
        }

        // Update the capacities of the source and destination warehouses
        sourceWarehouse.setWarehouseCapacity(sourceWarehouse.getWarehouseCapacity() + stockLevel);
        destinationWarehouse.setWarehouseCapacity(destinationWarehouse.getWarehouseCapacity() - stockLevel);
        warehouseService.updateWarehouseCapacity(sourceWarehouse, sourceWarehouse.getWarehouseId());
        warehouseService.updateWarehouseCapacity(destinationWarehouse, destinationWarehouse.getWarehouseId());

        // Update the stock level of the inventory item
        inventory.setStockLevel(inventory.getStockLevel() - stockLevel);
        inventoryRepository.save(inventory);
        
        Inventory newInventory = new Inventory();
        newInventory.setItemCategory(itemCategory);
        newInventory.setWarehouseName(destinationWarehouseName);
        newInventory.setStockLevel(stockLevel);
        newInventory.setEmail(inventory.getEmail());  // Assuming the 'inventory' object has the email
        inventoryRepository.save(newInventory);

        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Inventory transferred successfully"));
    }

}

