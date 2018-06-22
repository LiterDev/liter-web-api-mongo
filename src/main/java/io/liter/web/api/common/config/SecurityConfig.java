package io.liter.web.api.common.config;

import io.liter.web.api.auth.jwt.JwtAuthenticationConverter;
import io.liter.web.api.auth.jwt.JwtAuthenticationWebFilter;
import io.liter.web.api.auth.jwt.JwtReactiveAuthenticationManager;
import io.liter.web.api.auth.jwt.UnauthorizedAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import java.security.SecureRandom;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/auth/signIn",
            "/user/signUp",
            "/test/ws",
            "/sample/**",
            "/review/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            JwtReactiveAuthenticationManager authenticationManager,
            JwtAuthenticationConverter converter,
            UnauthorizedAuthenticationEntryPoint entryPoint

    ) {


        return http.csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterAt(new JwtAuthenticationWebFilter(authenticationManager, converter, entryPoint)
                        , SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    @Bean
    public WebSessionServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return new BCryptPasswordEncoder(12, random);
    }

    /*@Bean(name = "authenticationManagerWithRepository")
    public ReactiveAuthenticationManager authenticationManagerWithRepository(CustomReactiveUserDetailsService userRepository) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userRepository);
    }*/
    /*@Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil("test", 100000L);
    }*/

}