package io.liter.web.api.awsFileUpload;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AwsRouter {

    @Bean
    public RouterFunction<ServerResponse> sampleJRouterFunction(AwsHandler handler) {

        return RouterFunctions
                .nest(path("/aws"),
                        route(GET(""), handler::getAll)
                                .andRoute(POST("/s3Upload"), handler::s3Upload)
                );
    }
}