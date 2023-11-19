package com.mongodb.repository;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.model.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory,String> {


	List<Inventory> findByEmail(String email);

	List<Inventory> findByItemCategory(String itemCategory);

	List<Inventory> findByItemCategoryAndEmail(String itemCategory, String email);

	List<Inventory> findByItemCategoryAndWarehouseName(String itemCategory, String warehouseName);

}
