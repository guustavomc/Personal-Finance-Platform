package com.example.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.expense.dto.CreateExpenseRequest;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository=expenseRepository;
    }

    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }

    public Expense saveExpense(CreateExpenseRequest expenseRequest){
        Expense expense = new Expense();
        expense.setCategory(expenseRequest.getCategory());
        expense.setDate(expenseRequest.getDate());
        expense.setDescription(expenseRequest.getDescription());
        expense.setValue(expenseRequest.getValue());

        return expenseRepository.save(expense);
    }



    
}
