package io.liter.web.api.reply;

import io.liter.web.api.collection.MediaCollectionRepository;
import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.like.LikeRepository;
import io.liter.web.api.review.Review;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class ReplyHandler {


    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final ReplyRepository replyRepository;


    public ReplyHandler(
            UserRepository userRepository
            , ReviewRepository reviewRepository
            , ReplyRepository replyRepository
    ) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.replyRepository = replyRepository;
    }

    public Mono<ServerResponse> findAllByReviewId (ServerRequest request) {

        ObjectId reviewId = new ObjectId(request.pathVariable("reviewId"));

        return ok().build();
    }

    public Mono<ServerResponse> post (ServerRequest request) {

        ObjectId reviewId = new ObjectId(request.pathVariable("reviewId"));

        return ok().build();
    }

}