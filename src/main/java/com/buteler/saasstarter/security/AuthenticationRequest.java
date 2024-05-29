package com.buteler.saasstarter.security;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

}