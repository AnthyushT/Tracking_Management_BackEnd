package com.mongodb.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mongodb.DTO.StatusMessages;
import com.mongodb.model.Users;
import com.mongodb.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users_list")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
    @Autowired
    private UserService service;

    @PostMapping(value = "/save")
    public ResponseEntity<?> createUser(@RequestBody Users user){
        return service.addUser(user);
    }

    @GetMapping(value = "/getAll")
    public List<Users> getUsers() {
        return service.findAllUsers();
    }


    @GetMapping("get/{userId}")
    public Users getUser(@PathVariable String userId){
        return service.getUserByUserId(userId);
    }
    
    @GetMapping("getByEmail/{email}")
    public Users getEmail(@PathVariable String email){
        return service.getUserByEmail(email);
    }


    @PutMapping("/edit/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> modifyUsers(@RequestBody Users user, @PathVariable String userId){
        return service.updateUser(user, userId);
    }

    
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody Users user, HttpSession session){
        ResponseEntity<?> response = service.login(user, session);
        // If login is successful, store the user's email in the session
        if (response.getStatusCode() == HttpStatus.OK) {
            StatusMessages statusMessages = (StatusMessages) response.getBody();
            session.setAttribute("userEmail", statusMessages.getMessage());
        }
        return response;
    }

    
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId){
    	return service.deleteUser(userId);
    }
    
    @PostMapping(value = "/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> payload){
        String email = (String) payload.get("email");
        String oldPassword = (String) payload.get("oldPassword");
        String newPassword = (String) payload.get("newPassword");
        return service.resetPassword(email, oldPassword, newPassword);
    }
}






