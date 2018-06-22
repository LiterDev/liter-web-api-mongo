package io.liter.web.api.review.view;

import io.liter.web.api.collection.Collection;
import io.liter.web.api.review.Review;
import io.liter.web.api.tag.Tag;
import io.liter.web.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetail implements Serializable {

    private User user;

    private Review review;

    private List<Collection> collection;

    private List<Tag> tag;

}
