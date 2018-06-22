package io.liter.web.api.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    public AuthRepository authRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("]-----] CustomReactiveUserDetailsService::findByUsername call [-----[ ");
        return authRepository.findByUsername(username);
    }
}