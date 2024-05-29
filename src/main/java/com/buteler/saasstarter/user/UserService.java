package com.buteler.saasstarter.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

    /**
     * Creates a new user and publishes a {@link UserCreatedEvent}.
     *
     * @param userRequest the user to be created.
     * @return the created user.
     * @throws UserServiceException if the user could not be created.
     */
    UserResponse createUser(@NotNull UserRequest userRequest) throws UserServiceException;

}