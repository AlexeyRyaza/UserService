package com.app.userservice.services.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateDto {
    @NotBlank private String name;
    @NotBlank private String surname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull private LocalDate birthDate;

    @NotBlank
    @Email private String email;
}
