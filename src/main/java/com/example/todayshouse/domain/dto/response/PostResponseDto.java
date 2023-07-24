package com.example.todayshouse.domain.dto.response;

import com.example.todayshouse.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {

    private Long postId;

    private String content;

    private String nickname;

    private String titleImage;

    private Boolean isScrap;


    public PostResponseDto(Post post, Boolean isScrap) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.nickname = post.getNickname();
        this.titleImage = post.getTitleImageUrl();
        this.isScrap = isScrap;
    }

}
