package com.example.investment.controller;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.model.Investment;
import com.example.investment.service.InvestmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.investment.model.InvestmentType.CRYPTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Answers.valueOf;
import static org.mockito.Mockito.doNothing;
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

    @Test
    void getAllInvestmentsMade_ReturnNotFound(){
        InvestmentResponse investmentResponse = new InvestmentResponse();
        List<InvestmentResponse> investmentResponseList = new ArrayList<>();

        investmentResponseList.add(investmentResponse);

        when(investmentService.findAllInvestmentsMade()).thenThrow(new RuntimeException());

        ResponseEntity<List<InvestmentResponse>> response = investmentController.getAllInvestmentsMade();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getInvestmentById_ReturnResponseEntity_WithInvestmentResponse(){
        long id=1L;
        InvestmentResponse investmentResponse = new InvestmentResponse();
        investmentResponse.setId(id);
        when(investmentService.findInvestmentTransactionWithID(id)).thenReturn(investmentResponse);

        ResponseEntity<InvestmentResponse> response = investmentController.getInvestmentById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void createInvestment_ReturnResponseEntity_WithInvestmentCreated(){
        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        InvestmentResponse investmentResponse = new InvestmentResponse();
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        investmentResponse.setAmountInvested(BigDecimal.valueOf(1000));

        when(investmentService.saveInvestment(createInvestmentRequest)).thenReturn(investmentResponse);

        ResponseEntity<InvestmentResponse> responseEntity = investmentController.createInvestment(createInvestmentRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(investmentResponse.getAmountInvested(), responseEntity.getBody().getAmountInvested());
    }


    @Test
    void createInvestment_ReturnResponseEntity_WithNotFound(){
        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        InvestmentResponse investmentResponse = new InvestmentResponse();
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        investmentResponse.setAmountInvested(BigDecimal.valueOf(1000));

        when(investmentService.saveInvestment(createInvestmentRequest)).thenThrow(new RuntimeException());
        ResponseEntity<InvestmentResponse> responseEntity = investmentController.createInvestment(createInvestmentRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deleteInvestment_DeleteInvestmentWithID(){
        long id=1L;
        InvestmentResponse investmentResponse = new InvestmentResponse();
        investmentResponse.setId(id);
        doNothing().when(investmentService).removeInvestment(id);

        ResponseEntity<String> response = investmentController.deleteInvestment(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateInvestment_ReturnResponse_WithUpdatedInvestment(){
        InvestmentResponse investment = new InvestmentResponse();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        createInvestmentRequest.setInvestmentType(CRYPTO);
        createInvestmentRequest.setAssetSymbol("BTC");
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        createInvestmentRequest.setQuantity(BigDecimal.valueOf(0.2));
        createInvestmentRequest.setInvestmentDate(LocalDate.of(2025, 10, 8));
        createInvestmentRequest.setCurrency("BRL");

        InvestmentResponse investmentUpdated = new InvestmentResponse();
        investmentUpdated.setId(1L);
        investmentUpdated.setInvestmentType(CRYPTO);
        investmentUpdated.setAssetSymbol("BTC");
        investmentUpdated.setAmountInvested(BigDecimal.valueOf(1000));
        investmentUpdated.setQuantity(BigDecimal.valueOf(0.2));
        investmentUpdated.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investmentUpdated.setCurrency("BRL");

        when(investmentService.editInvestmentById(1L, createInvestmentRequest)).thenReturn(investmentUpdated);

        ResponseEntity<InvestmentResponse> response = investmentController.updateInvestment(1L, createInvestmentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createInvestmentRequest.getInvestmentType(), response.getBody().getInvestmentType());
    }


}
