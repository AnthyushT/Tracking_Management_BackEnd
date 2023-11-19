package com.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.model.Inventory;
import com.mongodb.service.InventoryService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/inventory_list")
public class InventoryController {
	
    @Autowired
    private InventoryService service;


    @PostMapping("/add")
    public ResponseEntity<?> createInventory(@RequestBody Inventory inventory) {
        return service.addInventory(inventory);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Inventory>> getInventory() {
        return service.findAllInventory();
    }

    @GetMapping("/{itemCategory}")
    public ResponseEntity<List<Inventory>> getInventoryByCategory(@PathVariable String itemCategory){
        return service.getInventoryByCategory(itemCategory);
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<?> modifyInventory(@RequestBody Inventory inventory, @PathVariable String inventoryId){
        return service.updateInventory(inventory, inventoryId);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Inventory>> getInventoryByEmail(@PathVariable String email){
        return service.getInventoryByEmail(email);
    }
    
    @PutMapping("/decrease/{inventoryId}")
    public ResponseEntity<?> decreaseStockLevel(@PathVariable String inventoryId){
        return service.decreaseStockLevel(inventoryId);
    }
    
    @PutMapping("/increase/{inventoryId}")
    public ResponseEntity<?> increaseStockLevel(@PathVariable String inventoryId){
        return service.increaseStockLevel(inventoryId);
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<?> transferInventory(@RequestParam String itemCategory, @RequestParam String sourceWarehouseName, @RequestParam String destinationWarehouseName, @RequestParam int stockLevel) {
        return service.transferInventory(itemCategory, sourceWarehouseName, destinationWarehouseName, stockLevel);
    }


}

