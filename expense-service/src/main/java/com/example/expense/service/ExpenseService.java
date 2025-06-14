package com.example.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.expense.dto.CreateExpenseRequest;
import com.example.expense.dto.ExpenseResponse;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository=expenseRepository;
    }

    public List<ExpenseResponse> getAllExpenses(){
        return expenseRepository.findAll().stream().map(expense -> mapToExpenseResponse(expense)).toList();
    }

    public Expense saveExpense(CreateExpenseRequest expenseRequest){
        Expense expense = new Expense();
        expense.setCategory(expenseRequest.getCategory());
        expense.setDate(expenseRequest.getDate());
        expense.setDescription(expenseRequest.getDescription());
        expense.setValueSpent(expenseRequest.getValueSpent());

        return expenseRepository.save(expense);
    }

    public ExpenseResponse mapToExpenseResponse(Expense expense){
        ExpenseResponse response = new ExpenseResponse();

        response.setCategory(expense.getCategory());
        response.setDate(expense.getDate());
        response.setDescription(expense.getDescription());
        response.setId(expense.getId());
        response.setValueSpent(expense.getValueSpent());

        return response;
    }



    
}
