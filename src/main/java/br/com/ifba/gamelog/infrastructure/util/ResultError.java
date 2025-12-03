package br.com.ifba.gamelog.infrastructure.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class ResultError {

    @AllArgsConstructor
    @Getter
    public static class ErrorDTO {
        private String field;
        private String message;
    }

    public static List<ErrorDTO> getResultErrors(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(fieldError -> new ErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}