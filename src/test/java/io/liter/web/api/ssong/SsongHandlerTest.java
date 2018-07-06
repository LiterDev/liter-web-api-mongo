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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
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
    public void reviewSaveAll() throws Exception {
        IntStream.range(0, 300000).parallel().forEach(index -> {
            log.debug("]-----]parallel[-----[");

            webTestClient.get().uri("/ssong/review")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk();
        });
    }

}