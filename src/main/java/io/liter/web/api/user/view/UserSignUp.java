package io.liter.web.api.user.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUp {

    private String username;

    private String password;

    @NotNull
    private String passwordRepeat;


}