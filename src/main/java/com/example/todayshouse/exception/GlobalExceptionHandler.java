package com.example.todayshouse.exception;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import static com.example.todayshouse.domain.StatusEnum.NOT_FOUND;
import static com.example.todayshouse.domain.StatusEnum.PARAMETER_WRONG;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        MessageResponseDto messageResponseDto = new MessageResponseDto(e.getMessage(), PARAMETER_WRONG.getCode(), PARAMETER_WRONG.getMessage());
        return ResponseEntity.status(PARAMETER_WRONG.getCode()).body(messageResponseDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponseDto> handleNotFoundException(NotFoundException e) {
        MessageResponseDto messageResponseDto = new MessageResponseDto(e.getMessage(), NOT_FOUND.getCode(), NOT_FOUND.getMessage());
        return ResponseEntity.status(NOT_FOUND.getCode()).body(messageResponseDto);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<MessageResponseDto> handleMultipartException(MultipartException e) {
        MessageResponseDto messageResponseDto = new MessageResponseDto(e.getMessage(), PARAMETER_WRONG.getCode(), PARAMETER_WRONG.getMessage());
        return ResponseEntity.status(PARAMETER_WRONG.getCode()).body(messageResponseDto);
    }

}