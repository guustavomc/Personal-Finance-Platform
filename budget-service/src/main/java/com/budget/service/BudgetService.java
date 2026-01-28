package com.budget.service;

import com.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseServiceClient expenseServiceClient;

    @Autowired
    private InvestmentServiceClient investmentServiceClient;




}
