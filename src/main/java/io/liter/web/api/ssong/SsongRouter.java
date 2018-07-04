package io.liter.web.api.ssong;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SsongRouter {


    @Bean
    public RouterFunction<ServerResponse> ssongRouterFunction(SsongHandler handler) {

        return RouterFunctions
                .nest(path("/ssong"),
                        route(GET(""), handler::get)
                                .andRoute(GET("/all"), handler::getall)
                                .andRoute(GET("/review"), handler::testSaveReview)
                                .andRoute(POST("/formdata"), handler::formdata)

                );
    }
}