package com.example.investment.exception;

public class InvestmentNotFoundException extends RuntimeException {

    public InvestmentNotFoundException(String message) {
        super(message);
    }
}
