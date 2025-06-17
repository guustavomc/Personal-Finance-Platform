package com.example.expense.controller;

import java.util.ArrayList;
import java.util.List;

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
    public List<ExpenseResponse> getAllExpenses(){
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public ExpenseResponse getExpenseByID(@PathVariable("id") long id){
        return expenseService.getExpenseById(id);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ExpenseResponse>> getMonthlyExpense(@RequestParam int year,@RequestParam int month){
        List<ExpenseResponse> expensesFromRequestedMonth = new ArrayList<>();
        try{
            expensesFromRequestedMonth = expenseService.getExpenseByMonth(year, month);
            return  ResponseEntity.status(HttpStatus.OK).body(expensesFromRequestedMonth);
        }
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(expensesFromRequestedMonth);
        }
    }
    
    @PostMapping
    public Expense creatExpense(@RequestBody CreateExpenseRequest expense){
        return expenseService.saveExpense(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable("id") long id){
        try{
            expenseService.deleteExpense(id);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable("id") long id, @RequestBody CreateExpenseRequest expense){
        ExpenseResponse response= new ExpenseResponse();
        try{
            response = expenseService.updateExpenseById(id, expense);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


    }



}
