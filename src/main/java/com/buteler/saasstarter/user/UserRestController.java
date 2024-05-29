package com.buteler.saasstarter.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "${com.buteler.api-url-prefix}" + "${com.buteler.subscription.api-endpoint}")
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) throws ServiceException {
        log.info("Creating user with username: {}", userRequest.getUsername());
        UserResponse userResponse = userService.createUser(userRequest);
        log.info("User created with username: {}", userResponse.getUsername());
        return userResponse;
    }

}