package io.liter.web.api.review;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    //TODO::review 페이징처리
    @Bean
    public RouterFunction<ServerResponse> routerFunction(ReviewHandler handler) {
        return RouterFunctions
                .nest(path("/review"),
                        route(GET("/").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::findAllByUserId)
                                .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::findById)
                                .andRoute(GET("/active/{id}").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::isActive)
                                .andRoute(POST("/").and(accept(APPLICATION_JSON_UTF8)).and(contentType(APPLICATION_JSON_UTF8)), handler::post)
                                .andRoute(PUT("/{id}").and(accept(APPLICATION_JSON_UTF8)), handler::put)
                                .andRoute(DELETE("/{id}"), handler::delete)
                );
    }
}
