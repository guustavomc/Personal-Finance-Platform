package com.example.investment.exception;

public class WithdrawalNotFoundException extends RuntimeException {

    public WithdrawalNotFoundException(String message){
        super(message);
    }
}
