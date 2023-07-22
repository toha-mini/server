package com.example.todayshouse.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDto {

    private boolean isLike;

    private int statusCode;

    private String statusMessage;

}
