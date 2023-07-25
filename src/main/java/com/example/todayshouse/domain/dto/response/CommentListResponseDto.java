package com.example.todayshouse.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentListResponseDto {

    private List<CommentResponseDto> commentList;

    private int commentSize;
}
