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

    public ExpenseResponse getExpenseById(Long id){
        ExpenseResponse response = new ExpenseResponse();

        Expense expense= expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with ID " + id + " not found"));

        return mapToExpenseResponse(expense);
    }

    public void deleteExpense(Long id){
        if(!expenseRepository.existsById(id)){
            throw new RuntimeException("Expense with ID " + id + " not found");
        }
        else{
            try{
                expenseRepository.deleteById(id);
            }
            catch(Exception e){
                throw new RuntimeException("Failed to delete Expense with ID " + id);
            }

        }
    }

    public ExpenseResponse updateExpenseById(long id, CreateExpenseRequest expense){
        if(!expenseRepository.existsById(id)){
            throw new RuntimeException("Expense with ID " + id + " not found");
        }

        Expense expenseWithId= expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task with ID " + id + " not found"));

        expenseWithId.setCategory(expenseWithId.getCategory());
        expenseWithId.setDate(expenseWithId.getDate());
        expenseWithId.setDescription(expenseWithId.getDescription());
        expenseWithId.setValueSpent(expenseWithId.getValueSpent());

        return mapToExpenseResponse(expenseWithId);
    }



    
}
