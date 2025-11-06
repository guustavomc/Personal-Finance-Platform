package com.example.expense.controller;

import com.example.expense.dto.ExpenseResponse;
import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/expense/report")
@Validated
public class ExpenseReportController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/summary/monthly")
    public ResponseEntity<ExpenseSummaryResponse> getMonthlyExpenseSummaryResponse(@RequestParam int year, @RequestParam int month){
        ExpenseSummaryResponse expenseSummaryResponse = new ExpenseSummaryResponse();

        expenseSummaryResponse = expenseService.findExpenseSummaryByMonth(year, month);

        return ResponseEntity.status(HttpStatus.OK).body(expenseSummaryResponse);
    }

    @GetMapping("/summary/annual")
    public ResponseEntity<ExpenseSummaryResponse> getAnnualExpenseSummaryResponse(@RequestParam int year){
        ExpenseSummaryResponse expenseSummaryResponse = new ExpenseSummaryResponse();

        expenseSummaryResponse = expenseService.findExpenseSummaryByYear(year);

        return ResponseEntity.status(HttpStatus.OK).body(expenseSummaryResponse);
    }


    @GetMapping("/detailed/monthly")
    public ResponseEntity<List<ExpenseResponse>> getMonthlyExpenses(@RequestParam int year, @RequestParam int month){
        List<ExpenseResponse> expensesFromRequestedMonth = new ArrayList<>();

        expensesFromRequestedMonth = expenseService.findExpensesByMonth(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(expensesFromRequestedMonth);

    }

    @GetMapping("/detailed/annual")
    public ResponseEntity<List<ExpenseResponse>> getAnnualExpenses(@RequestParam int year){
        List<ExpenseResponse> expensesFromRequestedYear = new ArrayList<>();
        expensesFromRequestedYear = expenseService.findExpensesByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(expensesFromRequestedYear);
    }
}
