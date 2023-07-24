package com.example.todayshouse.domain.dto.response;

import com.example.todayshouse.domain.entity.Post;
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

    private Long likeCount;

    private Long scrapCount;

    private Boolean isLike;

    private Boolean isScrap;

    public DetailPostResponseDto(Post post, Long likeCount, Long scrapCount, boolean isLike, boolean isScrap) {
        this.content = post.getContent();
        this.titleImage = post.getTitleImageUrl();
        this.subImage1 = post.getSubImageUrl1();
        this.subImage2 = post.getSubImageUrl2();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCount = likeCount;
        this.scrapCount = scrapCount;
        this.isLike = isLike;
        this.isScrap = isScrap;
    }
}
