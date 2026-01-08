package com.example.investment.controller;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.CreateWithdrawalRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.dto.WithdrawalResponse;
import com.example.investment.service.WithdrawalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.investment.model.InvestmentType.CRYPTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WithdrawalControllerTest {

    @Mock
    private WithdrawalService withdrawalService;

    @InjectMocks
    private WithdrawalController withdrawalController;

    @BeforeEach
    public  void setup(){

    }

    @Test
    void getAllWithdrawalsMade_ReturnListWith_WithdrawalResponse(){
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        List<WithdrawalResponse> responseList = new ArrayList<>();
        responseList.add(withdrawalResponse);

        when(withdrawalService.findAllWithdrawalsMade()).thenReturn(responseList);

        ResponseEntity<List<WithdrawalResponse>> response = withdrawalController.getAllWithdrawalsMade();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllWithdrawalsMade_ReturnNotFound(){
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        List<WithdrawalResponse> responseList = new ArrayList<>();

        responseList.add(withdrawalResponse);

        when(withdrawalService.findAllWithdrawalsMade())
                .thenThrow(RuntimeException.class);

        ResponseEntity<List<WithdrawalResponse>> response = withdrawalController.getAllWithdrawalsMade();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void getWithdrawalWithId_ReturnWithdrawalResponse(){
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setId(1L);
        long id = 1L;
        when(withdrawalService.findWithdrawalById(id)).thenReturn(withdrawalResponse);

        ResponseEntity<WithdrawalResponse> response = withdrawalController.getWithdrawalWithId(id);
        assertEquals(id, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void getWithdrawalByInvestmentType_ReturnResponseEntity_WithWithdrawalResponseList(){
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setInvestmentType(CRYPTO);
        List<WithdrawalResponse> responseList= new ArrayList<>();
        responseList.add(withdrawalResponse);
        when(withdrawalService.findWithdrawalWithInvestmentType(String.valueOf(CRYPTO)))
                .thenReturn(responseList);

        ResponseEntity<List<WithdrawalResponse>> response = withdrawalController.getWithdrawalByInvestmentType(String.valueOf(CRYPTO));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CRYPTO, response.getBody().get(0).getInvestmentType());
    }

    @Test
    void createWithdrawal_CreateWithdrawalRequest_ReturnWithdrawalResponse(){
        CreateWithdrawalRequest createWithdrawalRequest = new CreateWithdrawalRequest();
        createWithdrawalRequest.setProceeds(BigDecimal.valueOf(1000));

        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setProceeds(BigDecimal.valueOf(1000));

        when(withdrawalService.saveWithdrawal(createWithdrawalRequest))
                .thenReturn(withdrawalResponse);

        ResponseEntity<WithdrawalResponse> response = withdrawalController.createWithdrawal(createWithdrawalRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(1000), response.getBody().getProceeds());
    }

    @Test
    void deleteWithdrawal_DeleteWithdrawal_WithGivenID(){
        long id = 1L;

        doNothing().when(withdrawalService).removeWithdrawalWithID(id);

        ResponseEntity<String> response = withdrawalController.deleteWithdrawal(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void  editWithdrawal_UpdateWithdrawalWithID_ReturnUpdatedWithdrawalResponse(){
        long id = 1L;
        CreateWithdrawalRequest createWithdrawalRequest = new CreateWithdrawalRequest();
        createWithdrawalRequest.setProceeds(BigDecimal.valueOf(1000));

        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setId(1L);
        withdrawalResponse.setProceeds(BigDecimal.valueOf(1000));

        when(withdrawalService.editWithdrawalWithID(id, createWithdrawalRequest))
                .thenReturn(withdrawalResponse);

        ResponseEntity<WithdrawalResponse> response = withdrawalController.editWithdrawal(id, createWithdrawalRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createWithdrawalRequest.getProceeds(), withdrawalResponse.getProceeds());
    }
}
