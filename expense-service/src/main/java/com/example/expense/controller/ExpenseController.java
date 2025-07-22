package com.example.expense.controller;

import java.util.ArrayList;
import java.util.List;

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
        List<ExpenseResponse> expenseResponseList = new ArrayList<>();
        try {
            expenseResponseList = expenseService.findAllExpenses();
            return ResponseEntity.status(HttpStatus.OK).body(expenseResponseList);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(expenseResponseList);
        }
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

    /*@GetMapping("/summary")
    public ResponseEntity<List<ExpenseResponse>> getMonthlyExpenses(@RequestParam int year,@RequestParam int month){
        List<ExpenseResponse> expensesFromRequestedMonth = new ArrayList<>();
        try{
            expensesFromRequestedMonth = expenseService.findExpensesByMonth(year, month);
            return ResponseEntity.status(HttpStatus.OK).body(expensesFromRequestedMonth);
        }
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(expensesFromRequestedMonth);
        }
    }*/

    @GetMapping("/detailed/monthly")
    public ResponseEntity<List<ExpenseResponse>> getMonthlyExpenses(@RequestParam int year,@RequestParam int month){
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
    
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody CreateExpenseRequest expense){
        ExpenseResponse response = new ExpenseResponse();
        try {
            response = expenseService.saveExpense(expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable("id") long id){
        expenseService.removeExpense(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable("id") long id, @RequestBody CreateExpenseRequest expense){
        ExpenseResponse response= new ExpenseResponse();
        response = expenseService.editExpenseById(id, expense);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }






}
