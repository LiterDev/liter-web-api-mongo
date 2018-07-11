package io.liter.web.api.follower.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerPost {

    private ObjectId userId;

    private ObjectId followerId;
}
