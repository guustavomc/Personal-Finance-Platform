package com.budget.controller;

import com.budget.exception.BudgetNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import com.budget.service.BudgetService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BudgetController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BudgetService budgetService;

    @Test
    void BudgetNotFoundException_Returns404Not_Found() throws Exception{
        when(budgetService.findBudgetWithID(1L))
                .thenThrow(new BudgetNotFoundException("Failed to find budget with id 1"));


        mockMvc.perform(get("/api/budget/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Failed to find budget with id 1"));
    }

    @Test
    void RuntimeException_Returns500InternalServerError() throws Exception{
        when(budgetService.findBudgetWithID(1L))
                .thenThrow(new RuntimeException(""));


        mockMvc.perform(get("/api/budget/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Failed to delete Investment, Database connection failed: "));

    }
    
}
