package com.example.expense.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @PostMapping
    public Expense creatExpense(@RequestBody CreateExpenseRequest expense){
        return expenseService.saveExpense(expense);
    }


}
