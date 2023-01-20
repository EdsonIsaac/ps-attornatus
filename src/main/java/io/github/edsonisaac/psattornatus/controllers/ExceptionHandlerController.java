package io.github.edsonisaac.psattornatus.controllers;

import io.github.edsonisaac.psattornatus.exceptions.ObjectNotFoundException;
import io.github.edsonisaac.psattornatus.exceptions.StandardError;
import io.github.edsonisaac.psattornatus.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The type Exception handler controller.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * Method argument not valid exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        var errors = ex.getBindingResult().getFieldErrors().stream().map(error ->
                new StandardError(
                        System.currentTimeMillis(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        StringUtils.capitalize(error.getField()) + " " + error.getDefaultMessage() + "!",
                        request.getRequestURI())
        ).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Method argument type mismatch exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        var error = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Object not found exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity objectNotFoundException(ObjectNotFoundException ex, HttpServletRequest request) {

        var error = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Validation exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity validationException(ValidationException ex, HttpServletRequest request) {

        var error = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}