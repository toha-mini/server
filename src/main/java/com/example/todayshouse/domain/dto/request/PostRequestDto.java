package com.example.todayshouse.domain.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @Builder.Default
    private String content = "";
}
