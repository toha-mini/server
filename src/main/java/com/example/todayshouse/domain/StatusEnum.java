package com.example.todayshouse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    OK(200, "OK"),
    CREATED(201, "CREATED");

    private final int code;
    private final String message;

}
