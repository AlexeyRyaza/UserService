package com.app.userservice.services.mapper;

import com.app.userservice.models.CardInfo;
import com.app.userservice.models.User;
import com.app.userservice.services.dto.CardInfoDto;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-11T14:05:39+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class CardMapperImpl implements CardMapper {

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
        if ( card.getExpirationDate() != null ) {
            cardInfoDto.setExpirationDate( Date.from( card.getExpirationDate().atStartOfDay( ZoneOffset.UTC ).toInstant() ) );
        }

        return cardInfoDto;
    }

    @Override
    public CardInfo toEntity(CardInfoDto dto) {
        if ( dto == null ) {
            return null;
        }

        CardInfo cardInfo = new CardInfo();

        cardInfo.setUser( cardInfoDtoToUser( dto ) );
        if ( dto.getId() != null ) {
            cardInfo.setId( dto.getId() );
        }
        cardInfo.setNumber( dto.getNumber() );
        cardInfo.setHolder( dto.getHolder() );
        if ( dto.getExpirationDate() != null ) {
            cardInfo.setExpirationDate( LocalDateTime.ofInstant( dto.getExpirationDate().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }

        return cardInfo;
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

    protected User cardInfoDtoToUser(CardInfoDto cardInfoDto) {
        if ( cardInfoDto == null ) {
            return null;
        }

        User user = new User();

        if ( cardInfoDto.getUserId() != null ) {
            user.setId( cardInfoDto.getUserId() );
        }

        return user;
    }
}
