package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleExceptions(
            final Exception exception
    ) {
        HttpStatus status;
        String errorMessage = exception.getMessage();

        if (exception instanceof AlreadyExistException) {
            log.error(errorMessage);
            status = HttpStatus.CONFLICT;

        } else if (exception instanceof NotFoundException) {
            log.error(errorMessage);
            status = HttpStatus.NOT_FOUND;

        } else if (exception instanceof AccessDeniedException) {
            log.error(errorMessage);
            status = HttpStatus.FORBIDDEN;

        } else if (exception instanceof NotAvailableException) {
            log.error(errorMessage);
            status = HttpStatus.BAD_REQUEST;
        } else {
            log.error("Внутренняя ошибка сервера");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(new ErrorResponse(errorMessage), status);
    }
}
