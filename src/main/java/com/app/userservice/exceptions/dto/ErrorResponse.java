package com.app.userservice.exceptions.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ValidationError> errors;
}
