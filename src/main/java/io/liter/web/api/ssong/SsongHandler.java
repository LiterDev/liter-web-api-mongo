package io.liter.web.api.ssong;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.Review;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.sample.Sample;
import io.liter.web.api.tag.ReviewTag;
import io.liter.web.api.tag.ReviewTagRepository;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Slf4j
@Component
public class SsongHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private ReviewTagRepository reviewTagRepository;

    /**
     * ================================================================================================================
     */

    public Mono<ServerResponse> get(ServerRequest request) {
        Flux<Review> reviewFlux = this.reviewRepository.findAll();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(reviewFlux, Review.class);
    }

    public Mono<ServerResponse> postTag(ServerRequest request) {
        log.info("]-----] post [-----[ ");

        return request.bodyToMono(ReviewTag.class)
                .flatMap(t -> this.reviewTagRepository.save(t))
                .flatMap(r -> ServerResponse.ok().body(Mono.just(r), ReviewTag.class))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> postReview(ServerRequest request) {
        log.info("]-----] post [-----[ ");

        Mono<Review> reviewMono = reviewRepository.findById("5b2c62d57af9476b1fb09385");

        return ServerResponse.ok().build();
    }
}
