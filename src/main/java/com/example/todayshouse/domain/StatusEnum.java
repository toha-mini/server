package com.example.todayshouse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    OK(200, "OK"),
    CREATED(201, "CREATED"),

    PARAMETER_WRONG(400, "PARAMETER_WRONG"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    NOT_FOUND(404, "NOT_FOUND");

    private final int code;
    private final String message;

}
