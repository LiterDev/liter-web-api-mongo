package io.liter.web.api.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class AuthRepositoryReactiveAuthenticationManager {
    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    public AuthRepositoryReactiveAuthenticationManager(AuthReactiveUserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.encoder = encoder;
    }

    public Mono<Authentication> authenticate(Authentication authentication) {
        final String username = authentication.getName();
        return userDetailsService.findByUsername(username)
                .publishOn(Schedulers.parallel())
                .filter(u -> encoder.matches((String) authentication.getCredentials(), u.getPassword()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
                .map(u -> new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities()));
    }
}
