package com.app.userservice.services.dto.user;

import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull int id;
    @NotBlank String name;
    @NotBlank String surname;
    @NotNull List<CardInfoDto> cards;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull LocalDate birthDate;

    @NotBlank
    @Email String email;
}
