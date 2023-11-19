package com.mongodb.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.model.Orders;

public interface OrderRepository extends MongoRepository<Orders,String>{

	List<Orders> getOrderByOrderCategory(String orderCategory);

	List<Orders> findByOrderCategory(String orderCategory);

	List<Orders> findByOrderTitle(String orderTitle);

	List<Orders> findByEmail(String email);


}
