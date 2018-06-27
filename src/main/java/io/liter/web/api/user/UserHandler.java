package io.liter.web.api.user;

import io.liter.web.api.auth.Auth;
import io.liter.web.api.auth.AuthRepository;
import io.liter.web.api.common.util.UUIDGenerator;
import io.liter.web.api.user.view.UserSignUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;


@Slf4j
@Component
public class UserHandler {

    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public UserHandler(
            UserRepository userRepository
            , UserValidation userValidation
            , AuthRepository authRepository
            , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * User signUp
     * 1. password 검증
     * 2. userName exists 검증
     * 3. id 생성
     * 4. user save
     * 5. auth save
     * 6. return userName
     */
    public Mono<ServerResponse> signUp(ServerRequest request) {
        log.info("]-----] UserHandler::signUp call [-----[ ");
        Mono<UserSignUp> userSignUpMono = request.bodyToMono(UserSignUp.class);


        return userSignUpMono
                .flatMap(userValidation::validateUserSignUp)
                .flatMap(userSignUp -> {
                    User user = new User();
                    user.setUsername(userSignUp.getUsername());
                    return userRepository.save(user)
                            .flatMap(userSaved -> {
                                Auth auth = new Auth();
                                auth.setUserId(userSaved.getId());
                                auth.setUsername(userSaved.getUsername());
                                auth.setPassword(passwordEncoder.encode(userSignUp.getPassword()));
                                return authRepository.save(auth);

                            })
                            .switchIfEmpty(Mono.empty());
                })
                .flatMap(userSignUp -> ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(userSignUp)))
                .switchIfEmpty(badRequest().build());
    }

    public Mono<ServerResponse> userInfo(ServerRequest request) {
        log.info("]-----] UserHandler::userInfo call [-----[ ");

        return request.principal()
                .map(p -> p.getName())
                .flatMap(name -> userRepository.findByUsername(name)
                        .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(user)))
                )
                .switchIfEmpty(notFound().build());
    }
}
