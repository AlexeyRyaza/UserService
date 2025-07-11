package com.app.userservice.services.mapper;

import com.app.userservice.models.CardInfo;
import com.app.userservice.models.User;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-11T14:05:39+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class CardInfoMapperImpl implements CardInfoMapper {

    @Autowired
    private UserIdMapper userIdMapper;

    @Override
    public CardInfoDto toDto(CardInfo card) {
        if ( card == null ) {
            return null;
        }

        CardInfoDto cardInfoDto = new CardInfoDto();

        cardInfoDto.setUserId( cardUserId( card ) );
        cardInfoDto.setId( card.getId() );
        cardInfoDto.setNumber( card.getNumber() );
        cardInfoDto.setHolder( card.getHolder() );
        cardInfoDto.setExpirationDate( card.getExpirationDate() );

        return cardInfoDto;
    }

    @Override
    public CardInfo toEntity(CardInfoCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        CardInfo cardInfo = new CardInfo();

        cardInfo.setUser( userIdMapper.map( dto.getUserId() ) );
        cardInfo.setNumber( dto.getNumber() );
        cardInfo.setHolder( dto.getHolder() );
        cardInfo.setExpirationDate( dto.getExpirationDate() );

        return cardInfo;
    }

    @Override
    public CardInfo toEntity(CardInfoDto dto) {
        if ( dto == null ) {
            return null;
        }

        CardInfo cardInfo = new CardInfo();

        cardInfo.setUser( userIdMapper.map( dto.getUserId() ) );
        if ( dto.getId() != null ) {
            cardInfo.setId( dto.getId() );
        }
        cardInfo.setNumber( dto.getNumber() );
        cardInfo.setHolder( dto.getHolder() );
        cardInfo.setExpirationDate( dto.getExpirationDate() );

        return cardInfo;
    }

    @Override
    public void updateEntityFromDto(CardInfoDto dto, CardInfo entity) {
        if ( dto == null ) {
            return;
        }

        entity.setUser( userIdMapper.map( dto.getUserId() ) );
        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        entity.setNumber( dto.getNumber() );
        entity.setHolder( dto.getHolder() );
        entity.setExpirationDate( dto.getExpirationDate() );
    }

    private Integer cardUserId(CardInfo cardInfo) {
        if ( cardInfo == null ) {
            return null;
        }
        User user = cardInfo.getUser();
        if ( user == null ) {
            return null;
        }
        int id = user.getId();
        return id;
    }
}
