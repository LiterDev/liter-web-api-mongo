package io.liter.web.api.block;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document

public class Block extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private ObjectId reviewId;
}
