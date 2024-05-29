package com.buteler.saasstarter.user;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String username;
    private Boolean active;
    private Boolean locked;
    private Set<Role> roles = new HashSet<>();

}