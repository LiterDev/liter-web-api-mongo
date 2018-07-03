package io.liter.web.api.sample;

import org.bson.types.ObjectId;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class SampleHandlerTest {

    private static Logger log = LoggerFactory.getLogger(SampleHandlerTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllTest() {
        EntityExchangeResult<List<Sample>> result = webTestClient.get()
                .uri("/sample")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Sample.class)
                .returnResult();

        log.debug("]-----] result [-----[ {}", result);

        assertThat(result.getResponseBody(), is(notNullValue()));
    }

    @Test
    public void getTest() {
        EntityExchangeResult<Sample> result = webTestClient.get()
                .uri("/sample/{id}", Collections.singletonMap("id", "test01"))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Sample.class)
                .returnResult();

        log.debug("]-----] result [-----[ {}", result);
        log.debug("]-----] result.getResponseBody [-----[ {}", result.getResponseBody());
        assertThat(result.getResponseBody(), is(notNullValue()));
    }

    @Test
    public void postTest() {
        Sample sample = new Sample(new ObjectId("test06"), "john06", BigDecimal.TEN, 6L);

        EntityExchangeResult<Sample> result = webTestClient.post()
                .uri("/sample")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(sample), Sample.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Sample.class)
                .returnResult();
        //.jsonPath("$.id").isNotEmpty()
        //.jsonPath("$.title").isEqualTo("john01");

        log.debug("]-----] result [-----[ {}", result);
        log.debug("]-----] result.getResponseBody [-----[ {}", result.getResponseBody());
        assertThat(result.getResponseBody(), is(notNullValue()));

    }

    @Test
    public void putTest() {
        Sample sample = new Sample(new ObjectId("test01"), "john11", BigDecimal.TEN, 1L);

        EntityExchangeResult<Sample> result = webTestClient.put()
                .uri("/sample/{id}", Collections.singletonMap("id", "test01"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(sample), Sample.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Sample.class)
                .returnResult();

        log.debug("]-----] result [-----[ {}", result);
        log.debug("]-----] result.getResponseBody [-----[ {}", result.getResponseBody());

        assertThat(result.getResponseBody(), is(notNullValue()));

        log.debug("]-----] result.title [-----[ {}", result.getResponseBody().getTitle());
        assertThat(result.getResponseBody().getTitle(), is(equals("john11")));
    }
}