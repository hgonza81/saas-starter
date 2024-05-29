package com.buteler.saasstarter.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User mapUserRequestToUser(UserRequest userRequest);

    UserResponse mapUserToUserResponse(User user);

}