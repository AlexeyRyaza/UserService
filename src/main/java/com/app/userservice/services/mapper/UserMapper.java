package com.app.userservice.services.mapper;

import com.app.userservice.models.User;
import com.app.userservice.services.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
