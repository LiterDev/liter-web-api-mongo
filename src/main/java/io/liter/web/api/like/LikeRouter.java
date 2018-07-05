package io.liter.web.api.like;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LikeRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(LikeHandler handler) {
        return RouterFunctions
                .nest(path("/like"),
                        route(GET("/{id}").and(accept(APPLICATION_JSON_UTF8)), handler::getById)
                                .andRoute(POST("/{reviewId}").and(accept(APPLICATION_JSON_UTF8)).and(contentType(APPLICATION_JSON_UTF8)), handler::post)
                                .andRoute(DELETE("/{id}"), handler::delete)

                );
    }
}
