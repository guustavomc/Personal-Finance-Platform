package com.example.expense.controller;

import com.example.expense.dto.ExpenseResponse;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.service.ExpenseService;
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
public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @Test
    void getAllExpenses_ReturnListOfResponseEntity(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        when(expenseService.findAllExpenses()).thenReturn(expenseResponseList);

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getAllExpenses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(expenseResponseList, response.getBody());

    }

    @Test
    void getAllExpenses_ReturnNotFound(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        when(expenseService.findAllExpenses()).thenThrow(new RuntimeException());

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getAllExpenses();

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getExpenseByID_ReturnExpenseResponse(){
        long id = 1L;
        ExpenseResponse expenseResponse = new ExpenseResponse();

        when(expenseService.findExpenseById(id)).thenReturn(expenseResponse);

        ResponseEntity<ExpenseResponse> response = expenseController.getExpenseByID(id);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expenseResponse, response.getBody());
    }

    @Test
    void getExpenseByID_ReturnNotFound(){
        long id = 1L;
        ExpenseResponse expenseResponse = new ExpenseResponse();

        when(expenseService.findExpenseById(id)).thenThrow(new RuntimeException());

        ResponseEntity<ExpenseResponse> response = expenseController.getExpenseByID(id);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void getExpensesWithCategory_ReturnExpenseResponseList_WithCategory(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        String category = "Health";

        when(expenseService.findExpensesByCategory(category)).thenReturn(expenseResponseList);

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getExpensesWithCategory(category);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

    }

    @Test
    void getExpensesWithCategory_ReturnNotFound(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        String category = "Health";

        when(expenseService.findExpensesByCategory(category)).thenThrow(new RuntimeException());

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getExpensesWithCategory(category);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void getMonthlyExpenses_ReturnExpenseResponseList_WithMonthlyExpenses(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        int year = 2023;
        int month = 6;

        when(expenseService.findExpensesByMonth(year, month)).thenReturn(expenseResponseList);

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getMonthlyExpenses(year, month);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getMonthlyExpenses_ReturnNotFound(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        int year = 2023;
        int month = 6;

        when(expenseService.findExpensesByMonth(year, month)).thenThrow(new RuntimeException());

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getMonthlyExpenses(year, month);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

