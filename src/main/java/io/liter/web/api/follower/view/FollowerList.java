package io.liter.web.api.follower.view;


import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerList implements Serializable {

    private Pagination pagination;

    private long followerCount;

    private List<User> followerUser = new ArrayList<>();
}
