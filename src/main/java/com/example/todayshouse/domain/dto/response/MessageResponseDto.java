package com.example.todayshouse.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponseDto {

    private String msg;

    private int statusCode;

    private String statusMessage;

}
