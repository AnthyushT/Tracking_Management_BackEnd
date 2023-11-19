package com.mongodb.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mongodb.model.Orders;
import com.mongodb.model.Users;
import com.mongodb.service.OrderService;
import com.mongodb.service.UserService;


import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/orders_list")
public class OrderController {
	
    @Autowired
    private OrderService service;
    
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> createOrder(@RequestBody Orders order) {
        Users user = userService.getUserByEmail(order.getEmail()); //method to get User by Email
        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        return service.addOrder(order);
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<Orders>> getOrders() {
        return service.findAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrder(@PathVariable String orderId){
        return service.getOrderByOrderId(orderId);
    }

    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Orders>> getOrdersByEmail(@PathVariable String email){
        return service.getOrdersByEmail(email);
    }
    
    @PutMapping("/edit/{orderId}")
    public ResponseEntity<?> editOrder(@RequestBody Orders order, @PathVariable String orderId){
        return service.editOrder(order, orderId);
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId){
        return service.cancelOrder(orderId);
    }
}