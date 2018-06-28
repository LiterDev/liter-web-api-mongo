package io.liter.web.api.sample;

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
public class SampleRouter {

    @Bean
    public RouterFunction<ServerResponse> sampleRouterFunction(SampleHandler handler) {

        return RouterFunctions
                .nest(path("/sample"),
                        route(GET("").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::getAll)
                                .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::get)
                                .andRoute(POST("/").and(accept(APPLICATION_JSON_UTF8)).and(contentType(APPLICATION_JSON_UTF8)), handler::post)
                                .andRoute(PUT("/{id}").and(accept(APPLICATION_JSON_UTF8)), handler::put)
                                .andRoute(DELETE("/{id}"), handler::delete)
                );
    }
}
