package com.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.model.Warehouse;

public interface WarehouseRepository extends MongoRepository<Warehouse,String> {


	Optional<Warehouse> findByWarehouseLocation(String location);

	List<Warehouse> findByEmail(String email);

	List<Warehouse> findByWarehouseName(String warehouseName);

}
