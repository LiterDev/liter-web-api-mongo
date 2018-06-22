package io.liter.web.api.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {


    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {
        return RouterFunctions
                .nest(path("/user"),
                        route(POST("/signUp").and(accept(APPLICATION_JSON_UTF8)).and(contentType(APPLICATION_JSON_UTF8)), handler::signUp)
                                .andRoute(GET("/authInfo").and(accept(APPLICATION_JSON_UTF8)), handler::userInfo)
                );
    }
}
