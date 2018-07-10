package io.liter.web.api.follower;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Follower extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private long followerCount;

    private Set<ObjectId> followerId = new HashSet<>();
}
