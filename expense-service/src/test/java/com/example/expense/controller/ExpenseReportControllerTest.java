package com.example.expense.controller;

import com.example.expense.dto.ExpenseResponse;
import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseReportControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseReportController expenseReportController;

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

        ResponseEntity<ExpenseSummaryResponse> response = expenseReportController.getMonthlyExpenseSummaryResponse(year, month);
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

        ResponseEntity<ExpenseSummaryResponse> response = expenseReportController.getAnnualExpenseSummaryResponse(year);
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

        ResponseEntity<List<ExpenseResponse>> response = expenseReportController.getMonthlyExpenses(year, month);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAnnualExpenses_ReturnExpenseResponseList_WithYearlyExpenses(){
        ExpenseResponse expenseResponse = new ExpenseResponse();
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        expenseResponseList.add(expenseResponse);
        int year = 2025;

        when(expenseService.findExpensesByYear(year)).thenReturn(expenseResponseList);

        ResponseEntity<List<ExpenseResponse>> response = expenseReportController.getAnnualExpenses(year);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }


}
