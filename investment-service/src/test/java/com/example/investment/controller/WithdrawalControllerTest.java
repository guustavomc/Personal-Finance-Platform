package com.example.investment.controller;

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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
