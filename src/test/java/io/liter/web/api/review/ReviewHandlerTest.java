package io.liter.web.api.review;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.sample.SampleRepositoryTest;
import io.liter.web.api.user.UserRepository;
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
public class ReviewHandlerTest {

    private static Logger log = LoggerFactory.getLogger(SampleRepositoryTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowerRepository followerRepository;

    private String auth = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MDAyIiwiYXVkIjoidW5rbm93biIsInNjb3BlcyI6WyJTQ09QRV9BQ0NFU1MiXSwiZXhwIjoxNTMwNTkwMTMyLCJpYXQiOjE1MzA1ODY1MzJ9.sFEnyqTxHaHzcJLxI0b6xMfcjzXQuwstMkNnA_-SjrFTg44a1Vp3hgtvj6k9ZOydtz4ysIiz-CCX7yUYHUV6rA";

    //GET
    @Test
    public void findByUserIdIn() {
        IntStream.range(0, 100).parallel().forEach(index -> {

            long startTime = System.nanoTime();

            webTestClient.get()
                    .uri("/review?page=0&size=100")
                    .header("Authorization", "Bearer " + auth)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk();

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            log.debug("]-----]duration[-----[{}", duration);
        });

    }

    @Test
    public void review_parallel() throws Exception {
        IntStream.range(0, 10000).parallel().forEach(index -> {
            //long startTime = System.nanoTime();

            this.review();

            /*long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            log.debug("]-----]duration[-----[{}", duration);*/
        });
    }

    public void review() {

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
                        this.reviewRepository.findByUserIdInOrderByCreatedAtDesc(follower.getFollowerId(), PageRequest.of(0, 100))
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