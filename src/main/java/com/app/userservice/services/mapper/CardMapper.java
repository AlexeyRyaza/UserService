package com.app.userservice.services.mapper;

import com.app.userservice.models.CardInfo;
import com.app.userservice.services.dto.CardInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(source = "user.id", target = "userId")
    CardInfoDto toDto(CardInfo card);

    @Mapping(source = "userId", target = "user.id")
    CardInfo toEntity(CardInfoDto dto);
}
