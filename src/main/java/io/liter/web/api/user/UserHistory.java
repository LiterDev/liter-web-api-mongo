package io.liter.web.api.user;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserHistory extends BaseEntity {

    @Id
    private String id;

    private String userId;

    private String userName;

    private Integer level;

    private String profileImageUrl;

    private String email;

    private String gender;

    private Integer age;

    private Integer married;

}
