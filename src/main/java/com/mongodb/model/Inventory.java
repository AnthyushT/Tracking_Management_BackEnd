package com.mongodb.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "inventory_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
	@MongoId(targetType = FieldType.OBJECT_ID)
	private String inventoryId;
    private String itemCategory;
    private String email;
    private String warehouseName;
    private int stockLevel;
}
