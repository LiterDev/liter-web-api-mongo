package io.liter.web.api.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Slf4j
public class Responses {

    public static Mono<ServerResponse> ok(String result) {
        return ServerResponse.ok().body(Mono.just(result), String.class);
    }

    public static Mono<ServerResponse> badRequest(Exception e) {
        return fromException(HttpStatus.BAD_REQUEST, e);
    }

    public static Mono<ServerResponse> forbidden(Exception... notUsedException) {
        return ServerResponse.status(HttpStatus.FORBIDDEN).build();
    }

    public static Mono<ServerResponse> noContent(Object... notUsed) {
        return ServerResponse.noContent().build();
    }

    public static Mono<ServerResponse> notFound(Exception... notUsed) {
        return ServerResponse.notFound().build();
    }

    public static Mono<ServerResponse> internalServerError() {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public static Mono<ServerResponse> internalServerError(Exception e) {
        return fromException(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private static Mono<ServerResponse> fromException(HttpStatus status, Exception e) {
        //final ServerResponse.BodyBuilder responseBuilder = ServerResponse.status(status);
        //return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(e));
        /*log.debug("]-----] Responses::fromException status::e [-----[ {} :: {}", status, e);
        return e.getMessage() == null
                ? responseBuilder.build()
                : responseBuilder.body(Mono.just(e.getMessage()), String.class);*/
        //return ServerResponse.status(status).build();
        return  ServerResponse.status(status).body(fromObject(new ResponseStatusException( status, e.getMessage())));
        //return new ServerResponse.BodyBuilder.

    }

    /*public Mono<ServerResponse> myHandler(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body( serviceReturningFluxError()
                        .onErrorMap(RuntimeException.class, e -> new ResponseStatusException( BAD_REQUEST, e.getMessage())), String.class);
    }*/
}
