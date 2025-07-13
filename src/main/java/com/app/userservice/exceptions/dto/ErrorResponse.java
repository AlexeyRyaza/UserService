package com.app.userservice.exceptions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект ошибки, возвращаемый в случае неудачного запроса")
public class ErrorResponse {
    @Schema(description = "HTTP статус", example = "404")
    private int status;

    @Schema(description = "Название ошибки", example = "Not Found")
    private String error;

    @Schema(description = "Описание ошибки", example = "User with ID 123 not found")
    private String message;

    @Schema(description = "Путь запроса", example = "/user/123")
    private String path;

    @Schema(description = "Ошибки валидации, если есть")
    private List<ValidationError> errors;
}
