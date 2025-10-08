package com.example.investment.service;

import com.example.investment.dto.InvestmentResponse;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.investment.model.InvestmentType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private InvestmentService investmentService;

    @BeforeEach
    public void setup(){

    }

    @Test
    void findAllInvestmentsMade_ReturnListOfInvestmentResponse(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        List<Investment> investmentList = Arrays.asList(investment);
        when(investmentRepository.findAll()).thenReturn(investmentList);

        List<InvestmentResponse> investmentResponses = investmentService.findAllInvestmentsMade();
        assertEquals(investmentResponses.get(0).getId(),1L);
        assertEquals(investmentResponses.get(0).getInvestmentType(),CRYPTO);
        assertEquals(investmentResponses.get(0).getAssetSymbol(),"BTC");
        assertEquals(investmentResponses.get(0).getAmountInvested(),BigDecimal.valueOf(1000));
        assertEquals(investmentResponses.get(0).getQuantity(),BigDecimal.valueOf(0.1));
        assertEquals(investmentResponses.get(0).getInvestmentDate(),
                LocalDate.of(2025, 10, 8));
        assertEquals(investmentResponses.get(0).getCurrency(),"BRL");
    }

    @Test
    void findInvestmentTransactionWithID_ReturnInvestmentResponseWithID(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        List<Investment> investmentList = Arrays.asList(investment);
        when(investmentRepository.findById(1L)).thenReturn(Optional.of(investment));

        InvestmentResponse investmentResponses = investmentService.findInvestmentTransactionWithID(1L);
        assertEquals(investmentResponses.getId(),1L);
        assertEquals(investmentResponses.getInvestmentType(),CRYPTO);
        assertEquals(investmentResponses.getAssetSymbol(),"BTC");
        assertEquals(investmentResponses.getAmountInvested(),BigDecimal.valueOf(1000));
        assertEquals(investmentResponses.getQuantity(),BigDecimal.valueOf(0.1));
        assertEquals(investmentResponses.getInvestmentDate(),
                LocalDate.of(2025, 10, 8));
        assertEquals(investmentResponses.getCurrency(),"BRL");
    }
}
