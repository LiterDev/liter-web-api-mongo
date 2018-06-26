package io.liter.web.api.review.view;

import io.liter.web.api.review.Review;
import io.liter.web.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewList implements Serializable {

    private Pagination pagination;

    private User user;

    private List<Review> reviewList;
}
