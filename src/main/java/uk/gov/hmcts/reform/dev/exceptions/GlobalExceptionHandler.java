package uk.gov.hmcts.reform.dev.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
        
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, List<String>>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
        .body(Map.of("errors", List.of("Status must be one of: TODO, IN_PROGRESS, COMPLETED")));
    }
}
