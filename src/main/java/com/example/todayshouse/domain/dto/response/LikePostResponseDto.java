package com.example.todayshouse.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikePostResponseDto {

    private Long postId;

    private String content;

    private String nickname;

    private String titleImage;

    private String likeCount;

}
