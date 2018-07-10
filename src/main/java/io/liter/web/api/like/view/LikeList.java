package io.liter.web.api.like.view;

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
public class LikeList implements Serializable {

    private Pagination pagination;

    private long likerCount;

    private List<User> likeUser = new ArrayList<>();

}

