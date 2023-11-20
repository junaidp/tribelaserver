package com.echo.tribela.util;

import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class ApiResponse {
    private final Boolean success;
    private final String message;
    private final String systemMessage;
    private HttpStatus httpStatus;
}
