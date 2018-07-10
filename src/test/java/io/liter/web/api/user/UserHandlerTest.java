package io.liter.web.api.user;

import io.liter.web.api.sample.Sample;
import io.liter.web.api.user.view.UserSignUp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class UserHandlerTest {

    private static Logger log = LoggerFactory.getLogger(UserHandlerTest.class);

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserValidation userValidation;

    @Test
    public void signUp() {
<<<<<<< HEAD
        UserSignUp userSign = new UserSignUp("test001", "test1234!", "test1234!T");

=======
        UserSignUp userSign = new UserSignUp("test002", "test1234!T", "test1234!T");
>>>>>>> 33f4d71748c089601b07d765e7cd09a6712c94e8

        EntityExchangeResult<Sample> result = webTestClient.post()
                .uri("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(userSign), UserSignUp.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Sample.class)
                .returnResult();

        log.debug("]-----] result [-----[ {}", result);
        log.debug("]-----] result.getResponseBody [-----[ {}", result.getResponseBody());
        assertThat(result.getResponseBody(), is(notNullValue()));
    }
}