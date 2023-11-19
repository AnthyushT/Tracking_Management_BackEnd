package com.mongodb.service;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.DTO.RequestStatus;
import com.mongodb.DTO.StatusMessages;
import com.mongodb.model.Users;
import com.mongodb.repository.UserRepository;

import jakarta.servlet.http.HttpSession;



@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	
	// CRUD 
	public ResponseEntity<?> addUser(Users user) {
	    if(repository.findByEmail(user.getEmail()) != null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusMessages(RequestStatus.FAILED , "The user already exists!"));
	    }
	    repository.save(user);
	    return ResponseEntity.ok(new StatusMessages(RequestStatus.CREATED , "User Successfully added"));
	}

	 
	 public List<Users> findAllUsers() {
	        return repository.findAll();
	 }
	 
	 public Users getUserByUserId(String userId){
		 
	        return repository.findById(userId).get();
	 }
	 
	 public Users getUserByEmail(String email){
	        return repository.findByEmail(email);
	 }
	 
	 public List<Users> getUserByFirstName(int firstName){
	        return  repository.findByFirstName(firstName);
	 }
	 
	 
	 public ResponseEntity<?> updateUser(Users userRequest, String userId){
	        //get the existing document from DB
	        // populate new value from request to existing object/entity/document
		 	Optional<Users> existingUser1 = repository.findById(userId);
		 	if (existingUser1.isPresent()) {
		 		Users existingUser = existingUser1.get();
		 		existingUser.setFirstName(userRequest.getFirstName()); 
		        existingUser.setLastName(userRequest.getLastName());
		        existingUser.setEmail(userRequest.getEmail());
		        existingUser.setPassword(userRequest.getPassword());
		        existingUser.setMobileNumber(userRequest.getMobileNumber());
		        repository.save(existingUser);
		        return ResponseEntity.ok(new StatusMessages(RequestStatus.CREATED , "User Successfully Updated"));
		 	}
		 	return ResponseEntity.ok(new StatusMessages(RequestStatus.NOTFOUND, "User details not found"));
	    }

	    public ResponseEntity<?> deleteUser(String userId){
	    	Optional<Users> existingUser1 = repository.findById(userId);
	    	if (existingUser1.isPresent()) {
	    		repository.deleteById(userId);
	    		return ResponseEntity.ok(new StatusMessages(RequestStatus.DELETED, userId+" User Deleted Successfully"));
	    	}
	    	return ResponseEntity.ok(new StatusMessages(RequestStatus.NOTFOUND, userId+" User not found"));
	    }
	    
	    public ResponseEntity<?> login(Users user, HttpSession session) {
	        Users existingUser = repository.findByEmail(user.getEmail());
	        if(existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
	            // Include the user's email in the success message
	            return ResponseEntity.ok(new StatusMessages(RequestStatus.SUCCESS, existingUser.getEmail()));
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StatusMessages(RequestStatus.FAILED , "Invalid email or password"));
	    }
	    
	    public ResponseEntity<?> resetPassword(String email, String oldPassword, String newPassword){
	        Users user = repository.findByEmail(email);
	        if (user == null) {
	            return ResponseEntity.ok(new StatusMessages(RequestStatus.FAILED , "Email not found"));
	        }
	        // Authenticate the old password
	        if (!oldPassword.equals(user.getPassword())) {
	            return ResponseEntity.ok(new StatusMessages(RequestStatus.FAILED , "Old password is incorrect"));
	        }
	        // Set the new password
	        user.setPassword(newPassword);
	        repository.save(user);
	        return ResponseEntity.ok(new StatusMessages(RequestStatus.SUCCESS , "Password reset successful"));
	    }

}