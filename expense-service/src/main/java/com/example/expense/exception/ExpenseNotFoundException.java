package com.example.expense.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(String message){
        super(message);
    }
}
