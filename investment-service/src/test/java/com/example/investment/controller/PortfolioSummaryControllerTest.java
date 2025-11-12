package com.example.investment.controller;

import com.example.investment.dto.InvestmentResponse;
import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.service.PortfolioSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PortfolioSummaryControllerTest {

    @Mock
    PortfolioSummaryService portfolioSummaryService;

    @InjectMocks
    PortfolioSummaryController portfolioSummaryController;

    @BeforeEach
    public void setup(){

    }

    @Test
    void getSummary_ReturnResponseEntity_WithInvestmentResponseList(){
        PortfolioSummaryResponse portfolio = new PortfolioSummaryResponse();

        when(portfolioSummaryService.getPortfolioSummary()).thenReturn(portfolio);

        ResponseEntity<PortfolioSummaryResponse> response = portfolioSummaryController.getSummary();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSummary_ReturnNotFound(){
        PortfolioSummaryResponse portfolio = new PortfolioSummaryResponse();

        when(portfolioSummaryService.getPortfolioSummary()).thenThrow(new RuntimeException());

        ResponseEntity<PortfolioSummaryResponse> response = portfolioSummaryController.getSummary();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}
