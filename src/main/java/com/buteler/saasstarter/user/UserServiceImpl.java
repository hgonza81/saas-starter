package com.buteler.saasstarter.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher publisher;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating user with username: {}", userRequest.getUsername());

        checkThatUsernameIsAvailableOrThrowException(userRequest.getUsername());

        User user = userMapper.mapUserRequestToUser(userRequest);
        populateUserRolesFromRequest(user, userRequest);
        encryptUserPassword(user);
        userRepository.saveAndFlush(user);
        log.info("User saved with ID: {}", user.getId());
        UserResponse userResponse = userMapper.mapUserToUserResponse(user);

        publisher.publishEvent(new UserCreatedEvent(this, user.getUsername()));
        log.info("UserCreatedEvent published for user ID: {}", user.getId());

        log.info("User successfully created with username: {}", user.getUsername());
        return userResponse;
    }

    private void encryptUserPassword(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    private void populateUserRolesFromRequest(User user, UserRequest userRequest) {
        user.getRoles().clear();
        userRequest.getRoles().forEach(roleName -> {
            Optional<Role> role = roleRepository.findByName(roleName);
            if (role.isPresent()) {
                user.getRoles().add(role.get());
            } else {
                log.error("Role not found with name {}", roleName);
                throw new UserServiceException("Role " + roleName + " does not exist");
            }
        });
    }

    private void checkThatUsernameIsAvailableOrThrowException(String userName) {
        boolean userWithSameUsernameExists = userRepository.existsByUsername(userName);
        if (userWithSameUsernameExists) {
            log.error("Attempted to create a user with an already existing username: {}", userName);
            throw new UserServiceException("User with same username already exists");
        }
    }

}