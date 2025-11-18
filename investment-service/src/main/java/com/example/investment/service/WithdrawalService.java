package com.example.investment.service;

import com.example.investment.model.InvestmentType;
import com.example.investment.repository.WithdrawalRepository;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class WithdrawalService {

    private WithdrawalRepository withdrawalRepository;

    public WithdrawalService(WithdrawalRepository withdrawalRepository){
        this.withdrawalRepository=withdrawalRepository;
    }



}
