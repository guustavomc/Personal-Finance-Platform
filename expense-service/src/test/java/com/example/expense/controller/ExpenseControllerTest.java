package com.example.expense.controller;

import com.example.expense.dto.CreateExpenseRequest;
import com.example.expense.dto.ExpenseResponse;
import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.exception.ExpenseNotFoundException;
import com.example.expense.model.Expense;
import com.example.expense.model.PaymentMethod;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.service.ExpenseService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void getExpenseByID_ReturnExpenseResponse(){
        long id = 1L;
        ExpenseResponse expenseResponse = new ExpenseResponse();

        when(expenseService.findExpenseById(id)).thenReturn(expenseResponse);

        ResponseEntity<ExpenseResponse> response = expenseController.getExpenseByID(id);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(expenseResponse, response.getBody());
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
    void createExpense_ReturnExpenseResponse(){
        CreateExpenseRequest expense = new CreateExpenseRequest();
        List<ExpenseResponse> expenseResponse = new ArrayList<>();

        when(expenseService.saveExpense(expense)).thenReturn(expenseResponse);

        ResponseEntity<List<ExpenseResponse>> response = expenseController.createExpense(expense);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expenseResponse, response.getBody());
    }

    @Test
    void deleteExpense_DeleteExpenseWithID(){
        long id = 1L;
        ExpenseResponse expenseResponse = new ExpenseResponse();

        doNothing().when(expenseService).removeExpense(id);

        ResponseEntity<String> response = expenseController.deleteExpense(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateExpense_Success_ReturnsOk() {
        CreateExpenseRequest createExpenseRequest = new CreateExpenseRequest();
        createExpenseRequest.setCategory("Food");
        createExpenseRequest.setDate(LocalDate.of(2025, 6, 18));
        createExpenseRequest.setDescription("Lunch");
        createExpenseRequest.setTotalPurchaseValue(BigDecimal.valueOf(50));
        createExpenseRequest.setNumberOfInstallments(2);
        createExpenseRequest.setPaymentMethod(PaymentMethod.CREDIT);
        createExpenseRequest.setCurrency("BRL");

        ExpenseResponse firstInstallment = new ExpenseResponse();
        firstInstallment.setId(1L);
        firstInstallment.setDescription("Lunch");
        firstInstallment.setCategory("Food");
        firstInstallment.setValueSpent(BigDecimal.valueOf(25));
        firstInstallment.setDate(LocalDate.of(2025, 6, 18));
        firstInstallment.setPaymentMethod(PaymentMethod.CREDIT);
        firstInstallment.setTotalPurchaseValue(BigDecimal.valueOf(50));
        firstInstallment.setNumberOfInstallments(2);
        firstInstallment.setCurrentInstallment(1);
        firstInstallment.setCurrency("BRL");

        ExpenseResponse secondInstallment = new ExpenseResponse();
        secondInstallment.setId(2L);
        secondInstallment.setDescription("Lunch");
        secondInstallment.setCategory("Food");
        secondInstallment.setValueSpent(BigDecimal.valueOf(25));
        secondInstallment.setDate(LocalDate.of(2025, 7, 18)); // Next month
        secondInstallment.setPaymentMethod(PaymentMethod.CREDIT);
        secondInstallment.setTotalPurchaseValue(BigDecimal.valueOf(50));
        secondInstallment.setNumberOfInstallments(2);
        secondInstallment.setCurrentInstallment(2);
        secondInstallment.setCurrency("BRL");


        long id = 1L;
        List<ExpenseResponse> expenseResponses= new ArrayList<>();
        expenseResponses.add(firstInstallment);
        expenseResponses.add(secondInstallment);

        when(expenseService.editExpenseById(id, createExpenseRequest)).thenReturn(expenseResponses);

        ResponseEntity<List<ExpenseResponse>> response = expenseController.updateExpense(id, createExpenseRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

}

