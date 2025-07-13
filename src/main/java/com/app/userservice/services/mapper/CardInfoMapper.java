package com.app.userservice.services.mapper;

import com.app.userservice.models.CardInfo;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UserIdMapper.class)
public interface CardInfoMapper {
    @Mapping(source = "user.id", target = "userId")
    CardInfoDto toDto(CardInfo card);

    @Mapping(source = "userId", target = "user")
    CardInfo toEntity(CardInfoCreateDto dto);

    @Mapping(source = "userId", target = "user")
    CardInfo toEntity(CardInfoDto dto);

    @Mapping(source = "userId", target = "user")
    void updateEntityFromDto(CardInfoDto dto, @MappingTarget CardInfo entity);
}
