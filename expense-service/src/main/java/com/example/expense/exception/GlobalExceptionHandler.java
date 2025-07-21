package com.example.expense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<Object> handleExpenseNotFoundException(ExpenseNotFoundException ex){
        Map<String,Object> response = new HashMap<>();
        response.put("timestamp", LocalDate.now());
        response.put("status", HttpStatus.NOT_FOUND);
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex){
        Map<String,Object> response = new HashMap<>();
        response.put("timestamp", LocalDate.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
