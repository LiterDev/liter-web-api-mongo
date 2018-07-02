package io.liter.web.api.review.view;

import io.liter.web.api.review.Review;
import io.liter.web.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetail implements Serializable {

    private User user;

    private Review review;
}
