package com.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "orders_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
	@MongoId(targetType = FieldType.OBJECT_ID)
    private String orderId;
    private String orderTitle;
    private String orderCategory;
    private int items;
    private String email;
}