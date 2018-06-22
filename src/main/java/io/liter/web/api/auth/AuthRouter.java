package io.liter.web.api.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {


    @Bean
    public RouterFunction<ServerResponse> authRouterFunction(AuthHandler handler) {
        return RouterFunctions
                .nest(path("/auth"),
                        route(POST("/signIn").and(accept(APPLICATION_JSON_UTF8)), handler::signIn)
                                .andRoute(GET("/claimAccessToken").and(accept(APPLICATION_JSON_UTF8)), handler::claimAccessToken));
    }


}
