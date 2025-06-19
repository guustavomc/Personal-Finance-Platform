package com.example.expense.service;

import com.example.expense.dto.CreateExpenseRequest;
import com.example.expense.dto.ExpenseResponse;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    public void setup() {

    }

    @Test
    void findAllExpenses_ReturnListOfExpenseResponses(){
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory("Food");
        expense.setDate(LocalDate.of(2025, 6, 18));
        expense.setDescription("Lunch");
        expense.setValueSpent(BigDecimal.valueOf(25.50));

        List<Expense> expenses = Arrays.asList(expense);
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<ExpenseResponse> expenseResponses = expenseService.findAllExpenses();
        assertEquals(1, expenseResponses.size());
        assertEquals("Food", expenseResponses.get(0).getCategory());
        assertEquals(LocalDate.of(2025, 6, 18), expenseResponses.get(0).getDate());
        assertEquals("Lunch", expenseResponses.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(25.50), expenseResponses.get(0).getValueSpent());


    }
}
