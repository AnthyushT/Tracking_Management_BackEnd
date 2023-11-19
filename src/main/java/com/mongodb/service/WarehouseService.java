package com.mongodb.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.model.Users;
import com.mongodb.model.Warehouse;
import com.mongodb.repository.WarehouseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private UserService userService;

    public ResponseEntity<?> addWarehouse(Warehouse warehouse) {
        Users user = userService.getUserByEmail(warehouse.getEmail());
        if (user != null) {
            int capacity = warehouse.getWarehouseCapacity(); 
            warehouse.setWarehouseCapacity(capacity);

            warehouseRepository.save(warehouse);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Warehouse added successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "User not found."));
        }
    }
    
    public ResponseEntity<?> deleteWarehouse(String warehouseId) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(warehouseId);
        if(warehouse.isPresent()) {
            warehouseRepository.deleteById(warehouseId);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Warehouse deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Warehouse not found."));
        }
    }


    public ResponseEntity<Warehouse> getWarehouseByLocation(String location){
        Optional<Warehouse> warehouse = warehouseRepository.findByWarehouseLocation(location);
        if (warehouse.isPresent()) {
            return ResponseEntity.ok(warehouse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> updateWarehouseCapacity(Warehouse warehouseRequest, String warehouseId){
        Optional<Warehouse> existingWarehouse = warehouseRepository.findById(warehouseId);
        if (existingWarehouse.isPresent()) {
            existingWarehouse.get().setWarehouseCapacity(warehouseRequest.getWarehouseCapacity());
            warehouseRepository.save(existingWarehouse.get());
            return ResponseEntity.ok(Collections.singletonMap("message", "Warehouse capacity updated successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<List<Warehouse>> getWarehouseByEmail(String email) {
        Users user = userService.getUserByEmail(email);
        if (user != null) {
            List<Warehouse> warehouses = warehouseRepository.findByEmail(email);
            return ResponseEntity.ok(warehouses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    public ResponseEntity<List<Warehouse>> getWarehouseByName(String warehouseName) {
        List<Warehouse> warehouses = warehouseRepository.findByWarehouseName(warehouseName);
        if (warehouses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(warehouses);
    }


}
