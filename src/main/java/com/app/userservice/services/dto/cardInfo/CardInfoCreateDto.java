package com.app.userservice.services.dto.cardInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardInfoCreateDto {
    @NotBlank private String number;
    @NotBlank private String holder;
    @NotNull private LocalDate expirationDate;
    @NotNull private Integer userId;
}
