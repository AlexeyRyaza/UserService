package com.app.userservice.services.dto.cardInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardInfoDto {
    @NotNull Integer id;
    @NotBlank String number;
    @NotBlank String holder;
    @NotNull LocalDate expirationDate;
    @NotNull Integer userId;
}

