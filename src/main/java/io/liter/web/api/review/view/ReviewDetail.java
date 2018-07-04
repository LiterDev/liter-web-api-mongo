package io.liter.web.api.review.view;

import io.liter.web.api.review.Review;
import io.liter.web.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetail {

    private User user;

    private int userLikeActive;

    private Review review;
}
