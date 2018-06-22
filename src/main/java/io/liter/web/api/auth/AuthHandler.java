package io.liter.web.api.auth;

import io.liter.web.api.auth.jwt.JwtAuthenticationResponse;
import io.liter.web.api.auth.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Slf4j
@Component
public class AuthHandler {


    private final JwtTokenUtil jwtTokenUtil;
    private final AuthRepositoryReactiveAuthenticationManager authRepositoryReactiveAuthenticationManager;

    public AuthHandler(JwtTokenUtil jwtTokenUtil, AuthRepositoryReactiveAuthenticationManager authRepositoryReactiveAuthenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authRepositoryReactiveAuthenticationManager = authRepositoryReactiveAuthenticationManager;
    }


    /**
     * POST a Object
     */
    public Mono<ServerResponse> signIn(ServerRequest request) {
        log.info("]-----] AuthHandler::token call [-----[ ");
        Mono<AuthRequest> authMono = request.bodyToMono(AuthRequest.class);

        //return Mono.empty();
        return authMono
                .flatMap(auth -> authRepositoryReactiveAuthenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                auth.getUsername(),
                                auth.getPassword()
                        )
                )).doOnError(err -> {
                    log.error("]-----] AuthHandler::token call [-----[ {}", err);
                })
                .map(authentication -> (UserDetails) authentication.getPrincipal())
                .flatMap(user -> ServerResponse.ok().contentType(APPLICATION_JSON_UTF8).body(
                        fromObject(new JwtAuthenticationResponse(
                                jwtTokenUtil.generateAccessToken(user)
                                , jwtTokenUtil.generateRefreshToken(user)
                                , user.getUsername()))))
                .switchIfEmpty(notFound().build());

    }

    /**
     * POST a Object
     */
    @PreAuthorize("hasAuthority('SCOPE_REFRESH')")
    public Mono<ServerResponse> claimAccessToken(ServerRequest request) {
        log.info("]-----] AuthHandler::token call [-----[ ");

        //return Mono.empty();
        return request.principal()
                .map(p -> {
                    Auth auth = new Auth();
                    auth.setUsername(p.getName());
                    return auth;
                })
                .flatMap(user -> ServerResponse.ok().contentType(APPLICATION_JSON_UTF8).body(
                        fromObject(new JwtAuthenticationResponse(
                                jwtTokenUtil.generateAccessToken(user)
                                , null
                                , user.getUsername()))))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> options(ServerRequest request) {
        log.info("]-----] AuthHandler::options call [-----[ ");

        return ServerResponse.ok().build();
    }
}
