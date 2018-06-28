package io.liter.web.api.ssong;

import io.liter.web.api.common.model.BaseEntity;
import io.liter.web.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Ssong extends BaseEntity {

    @Id
    private ObjectId id;

    private List<User> user = new ArrayList<>();

}
