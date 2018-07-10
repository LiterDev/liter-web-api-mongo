package io.liter.web.api.reply;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReplyRouter {

    @Bean
    public RouterFunction<ServerResponse> replyRouterFunction(ReplyHandler handler) {
        return RouterFunctions
                .nest(path("/reply"),
                        route(GET("/{reviewId}").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::findAllByReviewId)
                        .andRoute(POST("/{reviewId}").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::post)
                );
    }
}
