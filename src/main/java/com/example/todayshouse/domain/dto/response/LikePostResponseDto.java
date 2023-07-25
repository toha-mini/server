package com.example.todayshouse.domain.dto.response;

import com.example.todayshouse.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikePostResponseDto {

    private Long postId;

    private String content;

    private String nickname;

    private String titleImage;

    private Long likeCount;

    public LikePostResponseDto(Post post, Long likeCount) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.nickname = post.getNickname();
        this.titleImage = post.getTitleImageUrl();
        this.likeCount = likeCount;
    }

}
