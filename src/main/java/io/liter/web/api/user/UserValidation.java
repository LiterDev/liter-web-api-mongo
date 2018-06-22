package io.liter.web.api.user;

import io.liter.web.api.common.exception.ErrorMessagerCode;
import io.liter.web.api.common.exception.LiterError;
import io.liter.web.api.user.exception.PasswordEmptyException;
import io.liter.web.api.user.exception.PasswordNotValidException;
import io.liter.web.api.user.exception.UserNameIsAlreadyExistsException;
import io.liter.web.api.user.exception.UserNameIsEmptyException;
import io.liter.web.api.user.view.UserSignUp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Slf4j
@Component
public class UserValidation {

    private static Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,20}$");
    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<UserSignUp> validateUserSignUp(UserSignUp userSignUp) {
        //String password = userSignUp.getPassword();


        return Mono.justOrEmpty(userSignUp)
                .flatMap(this::validateUsername)
                .flatMap(this::validatePassword);


    }

    public Mono<UserSignUp> validateUsername(UserSignUp userSignUp) {
        log.debug("]-----] UserValidation::validateUsername userSignUp[-----[ {}", userSignUp);
        if (StringUtils.isEmpty(userSignUp.getUsername())) {
            return Mono.error(new UserNameIsEmptyException(ErrorMessagerCode.USER_NAME_IS_EMPTY));
        }

        /*userRepository.findByUserName(userSignUp.getUserName())
                .flatMap(exists -> {
                    log.debug("]-----] UserValidation::validateUsername exists[-----[ {}", exists);
                    throw new UserNameIsAlreadyExistsException(ErrorMessagerCode.USER_NAME_IS_ALREADY_EXISTS);
                })
                .subscribe();*/
        //return Mono.just(userSignUp);
        return Mono.justOrEmpty(userSignUp)
                .flatMap(user -> {
                    log.debug("]-----] UserValidation::validateUsername exists[-----[ {}", user);
                    return userRepository.findByUsername(user.getUsername())
                            .flatMap(exists -> {
                                log.debug("]-----] UserValidation::validateUsername exists 1[-----[ {}", exists);
                                return Mono.error(new UserNameIsAlreadyExistsException(ErrorMessagerCode.USER_NAME_IS_ALREADY_EXISTS));
                            })
                            .switchIfEmpty(Mono.just(userSignUp));
                })
                .flatMap(users -> {
                    log.debug("]-----] UserValidation::validateUsername exists users [-----[ {}", users);
                    return Mono.just(userSignUp);
                });






/*
        return Mono.just(userSignUp)
                .flatMap(u -> userRepository.findByUserName(u.getUserName())
                        .flatMap(exists -> Mono.error(new UserNameIsEmptyException(ErrorMessagerCode.USER_NAME_IS_EMPTY)))
                        .switchIfEmpty()
                );*/

    }

    public Mono<UserSignUp> validatePassword(UserSignUp userSignUp) {
        //log.debug("]-----] UserValidation::validatePassword password[-----[ {}", userSignUp.getPassword());
        //log.debug("]-----] UserValidation::validatePassword password[-----[ {}", userSignUp.getPasswordRepeat());


        List<LiterError> errors = new ArrayList<>();
        if (StringUtils.isEmpty(userSignUp.getPassword())) {
            return Mono.error(new PasswordEmptyException(ErrorMessagerCode.USER_PASSWORD_IS_EMPTY));
        }

        Matcher mtchPassword = passwordPattern.matcher(userSignUp.getPassword());
        Matcher mtchpasswordRepeat = passwordPattern.matcher(userSignUp.getPasswordRepeat());

        if (!mtchPassword.matches()) {
            errors.add(new LiterError("/user/signUp", ErrorMessagerCode.USER_PASSWORD_PATTERN_IS_NOT_ALLOWED.getCode(), ErrorMessagerCode.USER_PASSWORD_PATTERN_IS_NOT_ALLOWED.getResponseValue()));
        }
        if (!mtchpasswordRepeat.matches()) {
            errors.add(new LiterError("/user/signUp", ErrorMessagerCode.USER_PASSWORD_REPEAT_PATTERN_IS_NOT_ALLOWED.getCode(), ErrorMessagerCode.USER_PASSWORD_REPEAT_PATTERN_IS_NOT_ALLOWED.getResponseValue()));
        }

        if (!StringUtils.equals(userSignUp.getPassword(), userSignUp.getPasswordRepeat())) {
            errors.add(new LiterError("/user/signUp", ErrorMessagerCode.USER_PASSWORD_IS_NOT_EQUALS.getCode(), ErrorMessagerCode.USER_PASSWORD_IS_NOT_EQUALS.getResponseValue()));
        }

        if (errors.size() > 0) {
            PasswordNotValidException passwordNotValidException = new PasswordNotValidException(ErrorMessagerCode.USER_PASSWORD_IS_NOT_ALLOWED);
            passwordNotValidException.setErrors(errors);

            return Mono.error(passwordNotValidException);
        }
        return Mono.just(userSignUp);
    }
}
