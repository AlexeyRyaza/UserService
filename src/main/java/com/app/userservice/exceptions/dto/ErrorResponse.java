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
@Schema(description = "Error object returned in case of a failed request")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error title", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "User with ID 123 not found")
    private String message;

    @Schema(description = "Request path", example = "/user/123")
    private String path;

    @Schema(description = "Validation errors, if any")
    private List<ValidationError> errors;
}
