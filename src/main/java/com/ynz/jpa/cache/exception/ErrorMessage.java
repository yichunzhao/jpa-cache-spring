package com.ynz.jpa.cache.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class ErrorMessage {
    private final int statusCode;
    private final LocalDateTime dateTime;
    private final String errorMessage;
    private final String description;
}
