package io.liter.web.api.favorite;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserFavorite extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private long favoriteCount;

    private List<Favorite> Favorite;

}
