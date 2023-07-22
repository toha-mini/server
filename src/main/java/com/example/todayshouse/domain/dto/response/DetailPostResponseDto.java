package com.example.todayshouse.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DetailPostResponseDto {

    private String content;

    private String titleImage;

    private String subImage1;

    private String subImage2;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private int likeCount;

    private int scrapCount;

    private boolean isLike;

    private boolean isScrap;

}
