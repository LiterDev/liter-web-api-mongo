package io.liter.web.api.user;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User extends BaseEntity {

    @Id
    private ObjectId id;

    private String username;

    private Integer level;

    private String profileImageUrl;

    private String email;

    private String gender;

    private Integer age;

    private Integer married;



}