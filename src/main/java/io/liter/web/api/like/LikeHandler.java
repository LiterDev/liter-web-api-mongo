package io.liter.web.api.like;

import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class LikeHandler {


    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public LikeHandler(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    /**
     * GET a Single Object by ID
     */
    public Mono<ServerResponse> getById(ServerRequest request) {

        return this.likeRepository
                .findById(new ObjectId(request.pathVariable("id")))
                .flatMap((post) -> ok().body(BodyInserters.fromObject(post)))
                .switchIfEmpty(notFound().build());
    }

    /**
     * POST a Object
     */
    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] LikeHandler::post call [-----[ ");
        Like like = new Like();

        ObjectId reviewId = new ObjectId(request.pathVariable("reviewId"));

        return request
                .principal()
                .map(p -> p.getName())
                .flatMap(user -> this.userRepository.findByUsername(user))
                .flatMap(user -> {

                    ArrayList<ObjectId>likes = new ArrayList<>();
                    likes.add(user.getId());
                    like.setLikeId(likes);
                    like.setReviewId((reviewId));


                    return ServerResponse.ok().body(this.likeRepository.save(like), Like.class);
                })
                .switchIfEmpty(notFound().build());
    }


    /**
     * DELETE a Object
     */
    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] ReviewHandler::delete call [-----[ ");

        return this.likeRepository.findById(new ObjectId(request.pathVariable("id")))
                .flatMap((post) -> noContent().build())
                .switchIfEmpty(notFound().build());
    }
}