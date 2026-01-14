package com.example.expense.controller;

import com.example.expense.dto.ExpenseResponse;
import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.service.ExpenseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
@Validated
public class ExpenseReportController {

    @Autowired
    private ExpenseReportService expenseReportService;

    @GetMapping("/summary/monthly")
    public ResponseEntity<ExpenseSummaryResponse> getMonthlyExpenseSummaryResponse(@RequestParam int year, @RequestParam int month){
        ExpenseSummaryResponse expenseSummaryResponse = new ExpenseSummaryResponse();

        expenseSummaryResponse = expenseReportService.findExpenseSummaryByMonth(year, month);

        return ResponseEntity.status(HttpStatus.OK).body(expenseSummaryResponse);
    }

    @GetMapping("/summary/annual")
    public ResponseEntity<ExpenseSummaryResponse> getAnnualExpenseSummaryResponse(@RequestParam int year){
        ExpenseSummaryResponse expenseSummaryResponse = new ExpenseSummaryResponse();

        expenseSummaryResponse = expenseReportService.findExpenseSummaryByYear(year);

        return ResponseEntity.status(HttpStatus.OK).body(expenseSummaryResponse);
    }


    @GetMapping("/report/monthly")
    public ResponseEntity<List<ExpenseResponse>> getMonthlyExpenses(@RequestParam int year, @RequestParam int month){
        List<ExpenseResponse> expensesFromRequestedMonth = new ArrayList<>();

        expensesFromRequestedMonth = expenseReportService.findExpensesByMonth(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(expensesFromRequestedMonth);

    }

    @GetMapping("/report/annual")
    public ResponseEntity<List<ExpenseResponse>> getAnnualExpenses(@RequestParam int year){
        List<ExpenseResponse> expensesFromRequestedYear = new ArrayList<>();
        expensesFromRequestedYear = expenseReportService.findExpensesByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(expensesFromRequestedYear);
    }

    @GetMapping("/total/monthly")
    public ResponseEntity<BigDecimal> getMonthlyTotalSpent(@RequestParam int year, @RequestParam int month){
        BigDecimal total = expenseReportService.findTotalSpentByMonth(year, month);
        return  ResponseEntity.status(HttpStatus.OK).body(total);

    }

    @GetMapping("/total/annual")
    public ResponseEntity<BigDecimal> getAnnualTotalSpent(@RequestParam int year){
        BigDecimal total = expenseReportService.findTotalSpentByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(total);
    }
}
