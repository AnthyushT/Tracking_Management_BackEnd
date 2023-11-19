package com.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mongodb.model.Warehouse;
import com.mongodb.service.WarehouseService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/warehouse_list")
public class WarehouseController {
	
    @Autowired
    private WarehouseService service;

    @PostMapping("/add")
    public ResponseEntity<?> createWarehouse(@RequestBody Warehouse warehouse) {
        return service.addWarehouse(warehouse);
    }

    @GetMapping("/{location}")
    public ResponseEntity<Warehouse> getWarehouseByLocation(@PathVariable String location){
        return service.getWarehouseByLocation(location);
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<?> modifyWarehouseCapacity(@RequestBody Warehouse warehouse, @PathVariable String warehouseId){
        return service.updateWarehouseCapacity(warehouse, warehouseId);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getWarehouseByEmail(@PathVariable String email) {
        return service.getWarehouseByEmail(email);
    }
    
    @DeleteMapping("/delete/{warehouseId}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable String warehouseId) {
        return service.deleteWarehouse(warehouseId);
    }
}

