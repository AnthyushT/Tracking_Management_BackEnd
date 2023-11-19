package com.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "users_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
	@MongoId(targetType = FieldType.OBJECT_ID)
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
}