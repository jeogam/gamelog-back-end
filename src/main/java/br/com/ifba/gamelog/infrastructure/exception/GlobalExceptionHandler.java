package br.com.ifba.gamelog.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils; // Precisa da dependencia commons-lang3 no pom.xml
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value(value = "${server.error.include-exception:false}")
    private boolean printStackTrace;

    /**
     * Captura erros de validação (@Valid, @NotNull, etc)
     */
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação. Verifique o campo 'errors' para detalhes.");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    /**
     * Captura qualquer exceção não tratada (Erro 500)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {

        final String errorMessage = "Ocorreu um erro interno inesperado.";
        log.error(errorMessage, exception);

        return buildErrorMessage(exception, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Trata as exceções de negócio (BusinessException)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        final String errorMessage = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST; // Default status

        // Lógica simplificada para definir o Status HTTP baseado na mensagem
        if (errorMessage.contains(BusinessExceptionMessage.NOT_FOUND.getMessage()) ||
                errorMessage.contains("não encontrado")) {
            status = HttpStatus.NOT_FOUND;
        } else if (errorMessage.contains(BusinessExceptionMessage.INVALID_CREDENTIALS.getMessage())) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (errorMessage.contains("já está em uso") ||
                errorMessage.contains(BusinessExceptionMessage.CLASS_IN_USE.getMessage())) {
            status = HttpStatus.CONFLICT;
        }

        log.error("Erro de Negócio: {}", errorMessage);
        return buildErrorMessage(ex, errorMessage, status, request);
    }

    private ResponseEntity<Object> buildErrorMessage(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStacktrace(ExceptionUtils.getStackTrace(exception));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}