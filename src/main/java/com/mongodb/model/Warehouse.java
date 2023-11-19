package com.mongodb.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "warehouse_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
	@MongoId(targetType = FieldType.OBJECT_ID)
    private String warehouseId;
    private String warehouseName;
    private String warehouseLocation;
    private int warehouseCapacity;
    private String email;
}
