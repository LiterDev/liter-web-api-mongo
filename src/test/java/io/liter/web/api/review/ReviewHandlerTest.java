package io.liter.web.api.review;

import io.liter.web.api.sample.SampleRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class ReviewHandlerTest {

    private static Logger log = LoggerFactory.getLogger(SampleRepositoryTest.class);

    @Autowired
    private WebTestClient webTestClient;

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
    public void test() {
        IntStream.range(0, 1).parallel().forEach(index -> {
            System.out.println("1");
        });
    }

    //POST
    @Test
    public void review2() throws Exception{
        IntStream.range(0, 100).parallel().forEach(index -> {

            webTestClient.get().uri("/ssong/review")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk();
        });
    }
}