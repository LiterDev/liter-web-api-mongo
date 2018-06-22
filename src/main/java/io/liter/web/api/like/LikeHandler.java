package io.liter.web.api.like;

import io.liter.web.api.review.Review;
import io.liter.web.api.review.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class LikeHandler {


    private final LikeRepository likeRepository;

    public LikeHandler(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    /**
     * GET a Single Object by ID
     */
    public Mono<ServerResponse> getById(ServerRequest request) {

        return this.likeRepository
                .findById(Long.parseLong(request.pathVariable("id")))
                .flatMap((post) -> ok().body(BodyInserters.fromObject(post)))
                .switchIfEmpty(notFound().build());
    }

    /**
     * POST a Object
     */
    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] LikeHandler::post call [-----[ ");

        return ServerResponse.ok().build();
    }

    /**
     * DELETE a Object
     */
    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] ReviewHandler::delete call [-----[ ");

        return this.likeRepository.findById(Long.parseLong(request.pathVariable("id")))
                .flatMap((post) -> noContent().build())
                .switchIfEmpty(notFound().build());
    }
}