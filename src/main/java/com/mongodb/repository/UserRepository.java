package com.mongodb.repository;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongodb.model.Users;


public interface UserRepository extends MongoRepository<Users,String>{

	List<Users> findByFirstName(int firstName);

	@Query("{ 'email' :?0 }")
	Users findByEmail(String email);
	
}