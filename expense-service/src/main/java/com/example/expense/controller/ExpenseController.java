package com.example.expense.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.expense.dto.ExpenseSummaryResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.expense.dto.CreateExpenseRequest;
import com.example.expense.dto.ExpenseResponse;
import com.example.expense.model.Expense;
import com.example.expense.service.ExpenseService;

@RestController
@RequestMapping("/api/expense")
@Validated
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(){
        List<ExpenseResponse> expenseResponseList = expenseService.findAllExpenses();
        return ResponseEntity.status(HttpStatus.OK).body(expenseResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseByID(@PathVariable("id") long id){
        ExpenseResponse expenseResponseWithId = new ExpenseResponse();

        expenseResponseWithId= expenseService.findExpenseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(expenseResponseWithId);

    }

    @GetMapping("/category")
    public ResponseEntity<List<ExpenseResponse>> getExpensesWithCategory(@RequestParam String category){
        List<ExpenseResponse> expenseWithRequestedCategory = new ArrayList<>();

        expenseWithRequestedCategory = expenseService.findExpensesByCategory(category);
        return ResponseEntity.status(HttpStatus.OK).body(expenseWithRequestedCategory);

    }
    
    @PostMapping
    public ResponseEntity<List<ExpenseResponse>> createExpense(@Valid @RequestBody CreateExpenseRequest expense){
        List<ExpenseResponse> response = expenseService.saveExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable("id") long id){
        expenseService.removeExpense(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<List<ExpenseResponse>> updateExpense(@PathVariable("id") long id, @RequestBody CreateExpenseRequest expense){
        List<ExpenseResponse> response= new ArrayList<>();
        response = expenseService.editExpenseById(id, expense);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
