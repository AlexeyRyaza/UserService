package com.app.userservice.services.dto.cardInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardInfoCreateDto {
    @NotBlank private String number;
    @NotBlank private String holder;
    @NotNull private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull private LocalDate expirationDate;
}
