package com.example.investment.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvestmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleInvestmentNotFoundException(InvestmentNotFoundException ex){
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex){
        return buildErrorResponse("Failed to delete Investment, Database connection failed: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        return buildErrorResponse("Failed to delete investment, the register is in use: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WithdrawalNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleWithdrawalNotFoundException(WithdrawalNotFoundException ex){
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Map<String,Object>> buildErrorResponse(String message, HttpStatus status){
        Map<String,Object> response = new HashMap<>();
        response.put("timestamp", LocalDate.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }
}
