package com.n11.paymentservice.exception;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;
}