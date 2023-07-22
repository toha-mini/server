package com.example.todayshouse.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {

    private String content;

    @NotBlank
    private String titleImage;

    private String subImage1;

    private String subImage2;
}
