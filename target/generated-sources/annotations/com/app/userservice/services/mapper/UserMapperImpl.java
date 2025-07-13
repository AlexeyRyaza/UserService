package com.app.userservice.services.mapper;

import com.app.userservice.models.CardInfo;
import com.app.userservice.models.User;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-11T14:05:39+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private CardInfoMapper cardInfoMapper;

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setName( user.getName() );
        userDto.setSurname( user.getSurname() );
        userDto.setCards( cardInfoListToCardInfoDtoList( user.getCards() ) );
        userDto.setEmail( user.getEmail() );

        return userDto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setName( dto.getName() );
        user.setSurname( dto.getSurname() );
        user.setEmail( dto.getEmail() );
        user.setCards( cardInfoDtoListToCardInfoList( dto.getCards() ) );

        return user;
    }

    @Override
    public User toEntity(UserCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setName( dto.getName() );
        user.setSurname( dto.getSurname() );
        user.setEmail( dto.getEmail() );

        return user;
    }

    @Override
    public void updateEntityFromDto(UserDto dto, User entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getSurname() != null ) {
            entity.setSurname( dto.getSurname() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( entity.getCards() != null ) {
            List<CardInfo> list = cardInfoDtoListToCardInfoList( dto.getCards() );
            if ( list != null ) {
                entity.getCards().clear();
                entity.getCards().addAll( list );
            }
        }
        else {
            List<CardInfo> list = cardInfoDtoListToCardInfoList( dto.getCards() );
            if ( list != null ) {
                entity.setCards( list );
            }
        }
    }

    protected List<CardInfoDto> cardInfoListToCardInfoDtoList(List<CardInfo> list) {
        if ( list == null ) {
            return null;
        }

        List<CardInfoDto> list1 = new ArrayList<CardInfoDto>( list.size() );
        for ( CardInfo cardInfo : list ) {
            list1.add( cardInfoMapper.toDto( cardInfo ) );
        }

        return list1;
    }

    protected List<CardInfo> cardInfoDtoListToCardInfoList(List<CardInfoDto> list) {
        if ( list == null ) {
            return null;
        }

        List<CardInfo> list1 = new ArrayList<CardInfo>( list.size() );
        for ( CardInfoDto cardInfoDto : list ) {
            list1.add( cardInfoMapper.toEntity( cardInfoDto ) );
        }

        return list1;
    }
}
