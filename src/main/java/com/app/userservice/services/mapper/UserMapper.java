package com.app.userservice.services.mapper;

import com.app.userservice.models.User;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = CardInfoMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
    User toEntity(UserCreateDto dto);

    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);
}
