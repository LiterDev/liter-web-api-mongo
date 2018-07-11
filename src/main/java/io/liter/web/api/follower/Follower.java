package io.liter.web.api.follower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.BsonTimestamp;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Follower implements Serializable {
        //extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private long followerCount;

    private Set<ObjectId> followerId = new HashSet<>();

    private BsonTimestamp createdAt = new BsonTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

    private BsonTimestamp updateAt = new BsonTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

}
