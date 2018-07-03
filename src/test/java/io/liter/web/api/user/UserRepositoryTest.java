package io.liter.web.api.user;

import io.liter.web.api.sample.Sample;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class UserRepositoryTest {

    private static Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {

        Flux<User> deleteAndInsert = userRepository.deleteAll()
                .thenMany(userRepository.saveAll(
                        Flux.just(
                                new User(new ObjectId("test01"), "john01", 1, "test", "test", "m", 19, 0)

                        )
                ));

        StepVerifier.create(deleteAndInsert).expectNextCount(1).verifyComplete();

        /*sampleRepository.saveAll(
                Flux.just(
                        new Sample("test01", "john01", BigDecimal.TEN, 1L),
                        new Sample("test02", "john02", BigDecimal.TEN, 2L),
                        new Sample("test03", "john03", BigDecimal.TEN, 3L),
                        new Sample("test04", "john04", BigDecimal.TEN, 4L),
                        new Sample("test05", "john05", BigDecimal.TEN, 5L)
                )
        )
                .then()
                .block();*/

    }

    @Test
    public void findByUserNameTest() {

        Mono<User> userMono = userRepository.findByUsername("john01");

        StepVerifier
                .create(userMono)
                .assertNext(user -> {
                    log.debug("]-----] user [-----[ {}", user);
                    assertNotNull(user.getId());
                    assertEquals("john01", user.getUsername());
                })
                .expectComplete()
                .verify();
    }
}