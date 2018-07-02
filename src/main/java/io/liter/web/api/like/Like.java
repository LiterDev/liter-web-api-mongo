package io.liter.web.api.like;

import io.liter.web.api.common.model.BaseEntity;
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
public class Like extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId reviewId;

    private List<ObjectId> likeId = new ArrayList<>();

}
