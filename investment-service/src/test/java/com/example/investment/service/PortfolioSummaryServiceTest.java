package com.example.investment.service;

import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;
import com.example.investment.repository.WithdrawalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.investment.model.InvestmentType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PortfolioSummaryServiceTest {
    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @InjectMocks
    private PortfolioSummaryService portfolioSummaryService;

    @BeforeEach
    public void setup(){

    }

    @Test
    void getPortfolioSummary_returnCurrentInvestments(){
        Investment firstInvestment = new Investment();
        firstInvestment.setId(1L);
        firstInvestment.setInvestmentType(CRYPTO);
        firstInvestment.setAssetSymbol("BTC");
        firstInvestment.setAmountInvested(BigDecimal.valueOf(1000));
        firstInvestment.setQuantity(BigDecimal.valueOf(1));
        firstInvestment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        firstInvestment.setCurrency("BRL");

        Investment secondInvestment = new Investment();
        secondInvestment.setId(1L);
        secondInvestment.setInvestmentType(CRYPTO);
        secondInvestment.setAssetSymbol("SOL");
        secondInvestment.setAmountInvested(BigDecimal.valueOf(500));
        secondInvestment.setQuantity(BigDecimal.valueOf(1));
        secondInvestment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        secondInvestment.setCurrency("BRL");

        List<Investment> investmentList = new ArrayList<>();
        investmentList.add(firstInvestment);
        investmentList.add(secondInvestment);

        when(investmentRepository.findAll()).thenReturn(investmentList);

        PortfolioSummaryResponse response = portfolioSummaryService.getPortfolioSummary();

        assertEquals(2,response.getAssetList().size());

    }

}
