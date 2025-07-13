package com.app.userservice.services.dto.cardInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardInfoDto {
    @NotNull Integer id;
    @NotBlank String number;
    @NotBlank String holder;
    @NotNull Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull private LocalDate expirationDate;
}

