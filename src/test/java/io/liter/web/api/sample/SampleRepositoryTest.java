package io.liter.web.api.sample;

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
public class SampleRepositoryTest {

    private static Logger log = LoggerFactory.getLogger(SampleRepositoryTest.class);

    @Autowired
    private SampleRepository sampleRepository;

    @Before
    public void setUp() {

        Flux<Sample> deleteAndInsert = sampleRepository.deleteAll()
                .thenMany(sampleRepository.saveAll(
                        Flux.just(
                                new Sample(new ObjectId("test01"), "john01", BigDecimal.TEN, 1L),
                                new Sample(new ObjectId("test02"), "john02", BigDecimal.TEN, 2L),
                                new Sample(new ObjectId("test03"), "john03", BigDecimal.TEN, 3L),
                                new Sample(new ObjectId("test04"), "john04", BigDecimal.TEN, 4L),
                                new Sample(new ObjectId("test05"), "john05", BigDecimal.TEN, 5L)
                        )
                ));

        StepVerifier.create(deleteAndInsert).expectNextCount(5).verifyComplete();

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
    public void saveTest() {
        Mono<Sample> sampleMono = sampleRepository.save(new Sample(new ObjectId("test01"), "john01", BigDecimal.TEN, 10L));

        StepVerifier
                .create(sampleMono)
                .assertNext(sample -> {
                    log.debug("]-----] sample [-----[ {}", sample);
                    assertNotNull(sample.getId());
                    assertEquals(Long.valueOf(10L), sample.getCreatedAt());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findByIdTest() {

        Mono<Sample> sampleMono = sampleRepository.findById(new ObjectId("test01"));

        StepVerifier
                .create(sampleMono)
                .assertNext(sample -> {
                    log.debug("]-----] sample [-----[ {}", sample);
                    assertNotNull(sample.getId());
                    assertEquals("john01", sample.getTitle());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findAllTest() {

        Flux<Sample> sampleFlux = sampleRepository.findAll();

        StepVerifier
                .create(sampleFlux)
                //.assertNext(sample -> assertEquals("test01", sample.getId()))
                .expectNextCount(5)
                .expectComplete()
                .verify();

    }
}