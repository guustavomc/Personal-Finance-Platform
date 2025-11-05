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
    void getExpenseByID_ReturnNotFound(){
        long id = 1L;
        ExpenseResponse expenseResponse = new ExpenseResponse();

        when(expenseService.findExpenseById(id)).thenThrow(new ExpenseNotFoundException("Expense with ID 1 not found"));

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseController.getExpenseByID(id));
        assertEquals("Expense with ID 1 not found", exception.getMessage());
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

        when(expenseService.findExpensesByCategory(category)).thenThrow(new ExpenseNotFoundException("Failed to find expenses with category Health"));

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseController.getExpensesWithCategory(category));
        assertEquals("Failed to find expenses with category Health", exception.getMessage());

    }

    @Test
    void getMonthlyExpenseSummaryResponse_ReturnMonthlySummaryResponse_WithMonthlyDetails(){
        ExpenseSummaryResponse summaryResponse = new ExpenseSummaryResponse();

        summaryResponse.setTotalExpenses(BigDecimal.valueOf(3000));

        summaryResponse.setAverageDailyExpense(BigDecimal.valueOf(100));

        summaryResponse.setHighestSpentCategory("Market");

        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        summaryResponse.setDetailedExpenses(expenseResponseList);

        Map<String, BigDecimal> totalPerCategory = new HashMap<>();
        summaryResponse.setTotalPerCategory(totalPerCategory);

        int year = 2025;
        int month = 9;

        when(expenseService.findExpenseSummaryByMonth(year, month)).thenReturn(summaryResponse);

        ResponseEntity<ExpenseSummaryResponse> response = expenseController.getMonthlyExpenseSummaryResponse(year, month);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(3000), response.getBody().getTotalExpenses());
        assertEquals(BigDecimal.valueOf(100), response.getBody().getAverageDailyExpense());
        assertEquals("Market", response.getBody().getHighestSpentCategory());
        assertEquals(0, response.getBody().getDetailedExpenses().size());
        assertEquals(0, response.getBody().getTotalPerCategory().size());


    }

    @Test
    void getAnnualExpenseSummaryResponse_ReturnAnnualSummaryResponse_WithAnnualDetails(){
        ExpenseSummaryResponse summaryResponse = new ExpenseSummaryResponse();

        summaryResponse.setTotalExpenses(BigDecimal.valueOf(3000));

        summaryResponse.setAverageDailyExpense(BigDecimal.valueOf(100));

        summaryResponse.setHighestSpentCategory("Market");

        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        summaryResponse.setDetailedExpenses(expenseResponseList);

        Map<String, BigDecimal> totalPerCategory = new HashMap<>();
        summaryResponse.setTotalPerCategory(totalPerCategory);

        int year = 2025;

        when(expenseService.findExpenseSummaryByYear(year)).thenReturn(summaryResponse);

        ResponseEntity<ExpenseSummaryResponse> response = expenseController.getAnnualExpenseSummaryResponse(year);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(3000), response.getBody().getTotalExpenses());
        assertEquals(BigDecimal.valueOf(100), response.getBody().getAverageDailyExpense());
        assertEquals("Market", response.getBody().getHighestSpentCategory());
        assertEquals(0, response.getBody().getDetailedExpenses().size());
        assertEquals(0, response.getBody().getTotalPerCategory().size());


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

        when(expenseService.findExpensesByMonth(year, month)).thenThrow(new ExpenseNotFoundException("Failed to find expenses from Year 2023, Month 6"));

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseController.getMonthlyExpenses(year, month));
        assertEquals("Failed to find expenses from Year 2023, Month 6", exception.getMessage());
    }

    @Test
    void getAnnualExpenses_ReturnExpenseResponseList_WithYearlyExpenses(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        int year = 2025;

        when(expenseService.findExpensesByYear(year)).thenReturn(expenseResponseList);

        ResponseEntity<List<ExpenseResponse>> response = expenseController.getAnnualExpenses(year);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAnnualExpenses_ReturnNotFound(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        int year = 2025;

        when(expenseService.findExpensesByYear(year)).thenThrow(new ExpenseNotFoundException("Failed to find expenses from Year 2025"));

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseController.getAnnualExpenses(year));
        assertEquals("Failed to find expenses from Year 2025", exception.getMessage());
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
    void deleteExpense_ReturnNotFound(){
        long id = 1L;
        ExpenseResponse expenseResponse = new ExpenseResponse();

        doThrow(new ExpenseNotFoundException("Expense with ID " + id + " not found")).when(expenseService).removeExpense(id);

        assertThrows(RuntimeException.class, () -> expenseController.deleteExpense(id));
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

    @Test
    void updateExpense_Failure_ReturnsNotFoundException() {
        long id = 1L;
        CreateExpenseRequest createExpenseRequest = new CreateExpenseRequest();
        ExpenseResponse expenseResponse = new ExpenseResponse();

        when(expenseService.editExpenseById(id, createExpenseRequest)).thenThrow(new ExpenseNotFoundException("Expense with ID " + id + " not found"));

        ExpenseNotFoundException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseController.updateExpense(id, createExpenseRequest));

        assertEquals("Expense with ID 1 not found", exception.getMessage());

    }


}

