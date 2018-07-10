package io.liter.web.api.follower;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FollowerRouter {

    @Bean
    public RouterFunction<ServerResponse> followerRouterFunction(FollowerHandler handler) {
        return RouterFunctions
                .nest(path("/follower"),
                        route(GET(""), handler::findAll)
                                .andRoute(GET("/{userId}"), handler::findAllByUserId)
                                .andRoute(POST("/{userId}"), handler::post)
                );
    }
}