package io.liter.web.api.ssong;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.Review;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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
    private SsongRepository ssongRepository;


    /**
     * ================================================================================================================
     */

    public Mono<ServerResponse> get(ServerRequest request) {

        Ssong ssong = new Ssong();

        return this.userRepository.findAll().collectList()
                .flatMap(user -> {

                    ssong.setUser(user);

                    return ServerResponse.ok().body(this.ssongRepository.save(ssong), Ssong.class);
                })
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getall(ServerRequest request) {

        return ServerResponse.ok().body(this.reviewRepository.findAll(),Review.class);
    }

    public Mono<ServerResponse> postReview(ServerRequest request) {
        log.info("]-----] post [-----[ ");

        //Mono<Review> reviewMono = reviewRepository.findById("5b2c62d57af9476b1fb09385");

        return ServerResponse.ok().build();
    }
}
