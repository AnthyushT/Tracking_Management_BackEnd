package com.mongodb.service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.DTO.RequestStatus;
import com.mongodb.DTO.StatusMessages;
import com.mongodb.model.Users;
import com.mongodb.model.Inventory;
import com.mongodb.model.Orders;
import com.mongodb.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private UserService userService;
	
	// CRUD 
	@Autowired
	private InventoryService inventoryService;

	public ResponseEntity<?> addOrder(Orders order) {
	    // validate the user before creating the order
	    Users user = userService.getUserByEmail(order.getEmail());
	    if (user == null) {
	        return new ResponseEntity<>(Collections.singletonMap("status", new StatusMessages(RequestStatus.FAILED , "User not found")), HttpStatus.NOT_FOUND);
	    }
	    // check the inventory levels before creating the order
	    ResponseEntity<List<Inventory>> inventoryResponse = inventoryService.getInventoryByCategoryAndEmail(order.getOrderCategory(), order.getEmail());
	    if (inventoryResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
	        return new ResponseEntity<>(Collections.singletonMap("status", new StatusMessages(RequestStatus.FAILED , "Inventory not found")), HttpStatus.NOT_FOUND);
	    }
	    List<Inventory> inventories = inventoryResponse.getBody();
	    // assuming you want to use the first inventory item in the list
	    Inventory inventory = inventories.get(0);
	    if (inventory.getStockLevel() < order.getItems()) {
	        return new ResponseEntity<>(Collections.singletonMap("status", new StatusMessages(RequestStatus.FAILED , "Not enough stock")), HttpStatus.BAD_REQUEST);
	    }
	    // deduct the inventory levels
	    inventory.setStockLevel(inventory.getStockLevel() - order.getItems());
	    ResponseEntity<?> updateResponse = inventoryService.updateInventory(inventory, inventory.getInventoryId());
	    if (updateResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
	        return new ResponseEntity<>(Collections.singletonMap("status", new StatusMessages(RequestStatus.FAILED , "Failed to update inventory")), HttpStatus.NOT_FOUND);
	    }
	    // save the order and return a success message
	    repository.save(order);
	    return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Order created successfully"));
	}




		 
	public ResponseEntity<List<Orders>> findAllOrders() {
	    // return all orders with OK status
	    return ResponseEntity.ok(repository.findAll());
	}
		 
	public ResponseEntity<Orders> getOrderByOrderId(String orderId){
	    // Optional to avoid null pointer exception
	    Optional<Orders> order = repository.findById(orderId);
	    if (order.isPresent()) {
	        // return the order with OK status
	        return ResponseEntity.ok(order.get());
	    } else {
	        // return a not found status
	        return ResponseEntity.notFound().build();
	    }
	}
		
		 
	public ResponseEntity<List<Orders>> getOrderByTypeOfOrder(String orderCategory){
	    // return the orders by category with OK status
	    return ResponseEntity.ok(repository.findByOrderCategory(orderCategory));
	}
		 
	public ResponseEntity<List<Orders>> getOrderByTitleOfOrder(String orderTitle){
	    // return the orders by title with OK status
	    return ResponseEntity.ok(repository.findByOrderTitle(orderTitle));
	}
		
	
	public ResponseEntity<List<Orders>> getOrdersByEmail(String email){
        // return the orders by email with OK status
        return ResponseEntity.ok(repository.findByEmail(email));
    }
	
	public ResponseEntity<?> editOrder(Orders orderRequest, String orderId){
	    // use Optional to avoid null pointer exception
	    Optional<Orders> existingOrder = repository.findById(orderId);
	    if (existingOrder.isPresent()) {
	        // update the order fields and save it
	        existingOrder.get().setOrderTitle(orderRequest.getOrderTitle()); 
	        existingOrder.get().setOrderCategory(orderRequest.getOrderCategory());
	        existingOrder.get().setItems(orderRequest.getItems());
	        repository.save(existingOrder.get());
	        // return a success message
	        return ResponseEntity.ok(Collections.singletonMap("message", "Order updated successfully"));
	    } else {
	        // return a not found status
	        return ResponseEntity.notFound().build();
	    }
	}



	public ResponseEntity<?> cancelOrder(String orderId){
	    // Check if orderId is a valid ObjectId
	    if(ObjectId.isValid(orderId)) {
	        Optional<Orders> existingOrder = repository.findById(orderId);
	        if (existingOrder.isPresent()) {
	            repository.deleteById(orderId);
	            return ResponseEntity.ok(Collections.singletonMap("message", "Order cancelled successfully"));
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Invalid Order ID"));
	    }
	}

}



