package com.example.expense.service;

import java.time.LocalDate;
import java.util.List;

import com.example.expense.exception.ExpenseNotFoundException;
import org.jetbrains.annotations.NotNull;
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

    public List<ExpenseResponse> findAllExpenses(){
        return expenseRepository.findAll().stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
    }

    public ExpenseResponse saveExpense(CreateExpenseRequest expenseRequest){
        try {
            return mapExpenseToExpenseResponse(saveVerifiedExpense(expenseRequest));
        }
        catch (Exception e){
            throw new RuntimeException("Failed to Save Expense");
        }
    }

    public Expense saveVerifiedExpense(CreateExpenseRequest expenseRequest){
        return expenseRepository.save(mapExpenseRequestToExpense(expenseRequest));
    }

    private Expense mapExpenseRequestToExpense(CreateExpenseRequest expenseRequest) {
        Expense expense = new Expense();
        expense.setCategory(expenseRequest.getCategory());
        expense.setDate(expenseRequest.getDate());
        expense.setDescription(expenseRequest.getDescription());
        expense.setValueSpent(expenseRequest.getValueSpent());

        return expense;
    }

    public ExpenseResponse mapExpenseToExpenseResponse(Expense expense){
        ExpenseResponse response = new ExpenseResponse();
        response.setCategory(expense.getCategory());
        response.setDate(expense.getDate());
        response.setDescription(expense.getDescription());
        response.setId(expense.getId());
        response.setValueSpent(expense.getValueSpent());
        return response;
    }

    public ExpenseResponse findExpenseById(Long id){
        Expense expense = findVerifiedExpense(id);
        return mapExpenseToExpenseResponse(expense);
    }

    public Expense findVerifiedExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + id + " not found"));
    }

    public List<ExpenseResponse> findExpensesByCategory(String requestedCategory){
        try {
            return expenseRepository.findByCategory(requestedCategory).stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
        }
        catch (Exception e){
            throw new ExpenseNotFoundException(String.format("Failed to find expenses with category %s", requestedCategory));
        }
    }

    public List<ExpenseResponse> findExpensesByMonth(int year, int month){
        LocalDate startDate = LocalDate.of(year, month,1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        try {
            return expenseRepository.findByDateBetween(startDate, endDate).stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
        }
        catch (Exception e){
            throw new ExpenseNotFoundException(String.format("Failed to find expenses from Year %d, Month %d", year, month));

        }
    }

    public void removeExpense(Long id){
        if(!expenseRepository.existsById(id)){
            throw new ExpenseNotFoundException("Expense with ID " + id + " not found");
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

    public ExpenseResponse editExpenseById(long id, CreateExpenseRequest expense){
        Expense expenseWithId = updateVerifiedExpenseById(id, expense);
        return mapExpenseToExpenseResponse(expenseWithId);
    }

    private Expense updateVerifiedExpenseById(long id, CreateExpenseRequest request) {
        Expense expense = findVerifiedExpense(id);
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        expense.setValueSpent(request.getValueSpent());
        return expenseRepository.save(expense);
    }


}
