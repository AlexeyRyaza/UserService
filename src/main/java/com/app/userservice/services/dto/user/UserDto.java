package com.app.userservice.services.dto.user;

import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {
    @NotNull int id;
    @NotBlank String name;
    @NotBlank String surname;
    @NotNull LocalDate birthDate;
    @NotNull List<CardInfoDto> cards;

    @NotBlank
    @Email
    String email;
}
