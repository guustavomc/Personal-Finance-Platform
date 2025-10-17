package com.example.investment.controller;

import com.example.investment.dto.InvestmentResponse;
import com.example.investment.service.InvestmentService;
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
public class InvestmentControllerTest {

    @Mock
    private InvestmentService investmentService;

    @InjectMocks
    private InvestmentController investmentController;

    @BeforeEach
    public void setup(){

    }

    @Test
    void getAllInvestmentsMade_ReturnResponseEntity_WithInvestmentResponseList(){
        InvestmentResponse investmentResponse = new InvestmentResponse();
        List<InvestmentResponse> investmentResponseList = new ArrayList<>();

        investmentResponseList.add(investmentResponse);

        when(investmentService.findAllInvestmentsMade()).thenReturn(investmentResponseList);

        ResponseEntity<List<InvestmentResponse>> response = investmentController.getAllInvestmentsMade();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }



}
