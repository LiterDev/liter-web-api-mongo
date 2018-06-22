package io.liter.web.api.auth.jwt;

import io.jsonwebtoken.Claims;
import io.liter.web.api.auth.Auth;
import io.liter.web.api.auth.AuthScope;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtTokenUtil jwtTokenUtil;

    public JwtReactiveAuthenticationManager(JwtTokenUtil jwtTokenUtil) {
        Assert.notNull(jwtTokenUtil, "jwtTokenUtil cannot be null");
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        if (authentication instanceof JwtPreAuthenticationToken) {
            return Mono.just(authentication)
                    .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                    .cast(JwtPreAuthenticationToken.class)
                    .flatMap(this::authenticateToken)
                    .publishOn(Schedulers.parallel())
                    .onErrorResume(e -> raiseBadCredentials())
                    .map(u -> new JwtAuthenticationToken(u.getUsername(), u.getAuthorities()));
        }

        return Mono.just(authentication);
    }

    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new BadCredentialsException("Invalid Credentials"));
    }

    private Mono<Auth> authenticateToken(final JwtPreAuthenticationToken jwtPreAuthenticationToken) {
        try {
            String authToken = jwtPreAuthenticationToken.getAuthToken();
            String username = jwtPreAuthenticationToken.getUsername();
            String bearerRequestHeader = jwtPreAuthenticationToken.getBearerRequestHeader();

            //logger.debug("checking authentication for user " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenUtil.validateToken(authToken)) {
                    //logger.debug("authenticated user " + username + ", setting security context");
                    final String token = authToken;
                    Auth user = new Auth();
                    user.setUsername(username);
                    Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
                    List<String> scopesMap = claims.get("scopes", List.class);
                    List<AuthScope> scopes = new ArrayList<>();
                    for (String scope : scopesMap) {
                        //log.debug("]-----] JwtReactiveAuthenticationManager::authenticateToken scope [-----[ {}", AuthScope.valueOf(scope));
                        scopes.add(AuthScope.valueOf(scope));
                    }
                    user.setScopes(scopes);
                    return Mono.just(user);
                    //return this.userDetailsService.findByUsername(username);
                }
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token...");
        }

        return null;
    }

}