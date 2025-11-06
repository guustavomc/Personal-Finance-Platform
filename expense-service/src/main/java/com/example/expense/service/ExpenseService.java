package com.example.expense.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.exception.ExpenseNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.core.Local;
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

    public List<ExpenseResponse> saveExpense(CreateExpenseRequest expenseRequest){
        List<Expense> savedExpenses= saveVerifiedExpense(expenseRequest);
        return savedExpenses.stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
    }

    public List<Expense> saveVerifiedExpense(CreateExpenseRequest expenseRequest){
        List<Expense> expenses = new ArrayList<>();
        if(expenseRequest.getNumberOfInstallments() ==1){
            Expense expense = expenseRepository.save(mapExpenseRequestToExpense(expenseRequest, 1));
            expenses.add(expense);
        }
        else {
            String purchaseId = UUID.randomUUID().toString();
            for(int currentInstallment=1; currentInstallment<=expenseRequest.getNumberOfInstallments(); currentInstallment++){

                Expense expense = mapExpenseRequestToExpense(expenseRequest, currentInstallment);
                expense.setPurchaseId(purchaseId);
                expenseRepository.save(expense);
                expenses.add(expense);
            }
        }
        return expenses;
    }

    private Expense mapExpenseRequestToExpense(CreateExpenseRequest expenseRequest, int currentInstallment) {
        Expense expense = new Expense();
        expense.setDescription(expenseRequest.getDescription());
        expense.setCategory(expenseRequest.getCategory());


        BigDecimal installmentValue = expenseRequest.getTotalPurchaseValue().divide(BigDecimal.valueOf(expenseRequest.getNumberOfInstallments()),RoundingMode.HALF_UP);
        expense.setValueSpent(installmentValue);

        LocalDate installmentDate = expenseRequest.getDate().plusMonths(currentInstallment-1);
        expense.setDate(installmentDate);

        expense.setPaymentMethod(expenseRequest.getPaymentMethod());
        expense.setTotalPurchaseValue(expenseRequest.getTotalPurchaseValue());
        expense.setNumberOfInstallments(expenseRequest.getNumberOfInstallments());
        expense.setCurrentInstallment(currentInstallment);
        expense.setCurrency(expenseRequest.getCurrency());
        return expense;
    }

    public ExpenseResponse mapExpenseToExpenseResponse(Expense expense){
        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setCategory(expense.getCategory());
        response.setValueSpent(expense.getValueSpent());
        response.setDate(expense.getDate());
        response.setPaymentMethod(expense.getPaymentMethod());
        response.setTotalPurchaseValue(expense.getTotalPurchaseValue());
        response.setNumberOfInstallments(expense.getNumberOfInstallments());
        response.setCurrentInstallment(expense.getCurrentInstallment());
        response.setCurrency(expense.getCurrency());
        return response;
    }

    public ExpenseResponse findExpenseById(Long id){
        Expense expense = findVerifiedExpense(id);
        return mapExpenseToExpenseResponse(expense);
    }

    public Expense findVerifiedExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(String.format("Failed to find expense with id %d", id)));
    }

    public List<ExpenseResponse> findExpensesByCategory(String requestedCategory){
        try {
            return expenseRepository.findByCategory(requestedCategory).stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
        }
        catch (Exception e){
            throw new ExpenseNotFoundException(String.format("Failed to find expenses with category %s", requestedCategory));
        }
    }







    public void removeExpense(Long id){
        if(!expenseRepository.existsById(id)){
            throw new ExpenseNotFoundException(String.format("Failed to find expense with id %d", id));
        }
        else{
            removeVerifiedExpense(id);
        }
    }

    private void removeVerifiedExpense(Long id){
        if(findVerifiedExpense(id).getNumberOfInstallments() ==1){
            removeVerifiedExpenseWithSingleInstallment(id);
        }
        else{
            removeVerifiedExpenseWithMultipleInstallments(id);
        }
    }

    private void removeVerifiedExpenseWithSingleInstallment(Long id) {
        expenseRepository.deleteById(id);
    }

    private void removeVerifiedExpenseWithMultipleInstallments(Long id) {
        String purchaseId = findVerifiedExpense(id).getPurchaseId();
        List<Expense> expensesWithPurchaseID = expenseRepository.findByPurchaseId(purchaseId);
        expenseRepository.deleteAll(expensesWithPurchaseID);
    }

    public List<ExpenseResponse> editExpenseById(long id, CreateExpenseRequest createExpenseRequest){
        List<Expense> savedExpenses= updateVerifiedExpenseById(id,createExpenseRequest);
        return  savedExpenses.stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
    }

    private List<Expense> updateVerifiedExpenseById(long id, CreateExpenseRequest createExpenseRequest) {
        String purchaseID = findVerifiedExpense(id).getPurchaseId();
        List<Expense> expensesWithPurchaseID = expenseRepository.findByPurchaseId(purchaseID);
        expenseRepository.deleteAll(expensesWithPurchaseID);
        return saveVerifiedExpense(createExpenseRequest);
    }
}
