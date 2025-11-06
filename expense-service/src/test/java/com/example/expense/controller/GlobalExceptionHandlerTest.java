package com.example.expense.controller;

import com.example.expense.exception.ExpenseNotFoundException;
import com.example.expense.service.ExpenseService;
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

@WebMvcTest(ExpenseController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Test
    void expenseNotFoundException_Return404_WhenExpenseNotFound() throws Exception{
        when(expenseService.findExpenseById(1L)).thenThrow(new ExpenseNotFoundException("Failed to find expense with id 1"));

        mockMvc.perform(get("/api/expense/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Failed to find expense with id 1"));

    }

    @Test
    void DataIntegrityViolationException_Return400_WhenFailedToDeleteExpense() throws Exception{
        doThrow(new DataIntegrityViolationException("")).when(expenseService).removeExpense(1L);

        mockMvc.perform(delete("/api/expense/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Failed to delete Expense, the register is in use: "));

    }

    @Test
    void RuntimeException_Return500_WhenUnexpectedErrorOccurs() throws Exception{
        when(expenseService.findAllExpenses()).thenThrow(new RuntimeException(""));

        mockMvc.perform(get("/api/expense"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Failed to delete Expense, Database connection failed: "));

    }

}
