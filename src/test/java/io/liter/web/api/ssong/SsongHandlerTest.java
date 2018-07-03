package io.liter.web.api.ssong;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.UserRepository;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class SsongHandlerTest {

    private static Logger log = LoggerFactory.getLogger(SsongHandlerTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void review() throws Exception {

        for (int i = 0; i < 300000; i++) {
            webTestClient.get().uri("/ssong/review")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Test
    public void review2() throws Exception {
        IntStream.range(0, 300000).parallel().forEach(index -> {
            log.debug("]-----]parallel[-----[");

            webTestClient.get().uri("/ssong/review")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk();
        });
    }

    @Test
    public void find() throws Exception {

        webTestClient.get().uri("/ssong")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void ssongReviewGet() throws Exception {
        IntStream.range(0, 1000).parallel().forEach(index -> {

            webTestClient.get().uri("/ssong")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk();

        });
    }

    @Test
    public void ssongReviewGet2() throws Exception {
        IntStream.range(0, 1).parallel().forEach(index -> {
            //long startTime = System.nanoTime();

            this.review3();

            /*long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            log.debug("]-----]duration[-----[{}", duration);*/
        });
    }

    public void review3() {

        ReviewList reviewList = new ReviewList();
        Pagination pagination = new Pagination();

        Mono<ReviewList> reviewListMono =  this.userRepository.findByUsername("test002")
                .map(user -> {
                    log.debug("]-----] user [-----[ {}", user.getUsername());
                    reviewList.setUser(user);
                    return user;
                })
                .flatMap(user -> this.followerRepository.findByUserId(user.getId()))
                .flatMap(follower ->
                        this.reviewRepository.findByUserIdIn(follower.getFollowerId(), PageRequest.of(0, 100))
                                .collectList()
                                .map(collections -> {
                                    reviewList.setReview(collections);
                                    return follower;
                                }))
                .flatMap(follower -> this.reviewRepository.countByUserIdIn(follower.getFollowerId()))
                .flatMap(count -> {
                    log.debug("]-----] count [-----[ {}", count);
                    pagination.setTotal(count);
                    pagination.setPage(0);
                    pagination.setSize(100);

                    reviewList.setPagination(pagination);

                    return Mono.just(reviewList);
                })
                .thenReturn(reviewList);

        StepVerifier
                .create(reviewListMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}