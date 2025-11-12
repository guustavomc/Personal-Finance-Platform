package com.example.investment.controller;

import com.example.investment.exception.InvestmentNotFoundException;
import com.example.investment.service.InvestmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestmentController.class)

public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InvestmentService investmentService;

    @BeforeEach
    public void setup(){

    }

    @Test
    void investmentNotFoundException_Return404_WhenInvestmentNotFound() throws Exception{
        when(investmentService.findInvestmentTransactionWithID(1L)).thenThrow(new InvestmentNotFoundException("Failed to find investment with id 1"));

        mockMvc.perform(get("/api/investment/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Failed to find investment with id 1"));
    }

    @Test
    void DataIntegrityViolationException_Return400_WhenFailedToDeleteInvestment() throws Exception{
        doThrow(new DataIntegrityViolationException("")).when(investmentService).removeInvestment(1L);

        mockMvc.perform(delete("/api/investment/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Failed to delete investment, the register is in use: "));
    }

    @Test
    void RuntimeException_Return500_WhenUnexpectedErrorOccurs() throws Exception{
        when(investmentService.findInvestmentTransactionWithID(1L)).thenThrow(new RuntimeException(""));

        mockMvc.perform(get("/api/investment/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Failed to delete Investment, Database connection failed: "));

    }
}
