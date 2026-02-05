package com.budget.service;

import com.budget.dto.BudgetItem;
import com.budget.dto.ExpenseResponse;
import com.budget.dto.InvestmentResponse;
import com.budget.model.Budget;
import com.budget.model.BudgetCategory;
import com.budget.model.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BudgetItemService {

    @Autowired
    private ExpenseServiceClient expenseServiceClient;

    @Autowired
    private InvestmentServiceClient investmentServiceClient;

    public List<BudgetItem> getBudgetItemsForSpecificBudget(Budget budget){
        return buildBudgetItemsTimeline(budget.getStartDate().getYear(), budget.getStartDate().getMonthValue());
    }

    public List<BudgetItem> buildBudgetItemsTimeline(int year, int month){
        List<BudgetItem> expenses = getExpensesFromMonth(year, month);
        List<BudgetItem> investments = getInvestmentsFromMonth(year, month);

        return Stream
                .concat(expenses.stream(), investments.stream())
                .sorted(Comparator.comparing(BudgetItem::getDate))
                .toList();
    }

    private List<BudgetItem> getExpensesFromMonth(int year, int month){
        List<ExpenseResponse> expenses = expenseServiceClient.getExpensesByMonth(year, month);
        return  expenses.stream().map(item -> mapExpenseResponseToBudgetItem(item)).toList();
    }

    private List<BudgetItem> getInvestmentsFromMonth(int year, int month){
        List<InvestmentResponse> investments = investmentServiceClient.getInvestmentsByMonth(year, month);
        return investments.stream().map(item -> mapInvestmentResponseToBudgetItem(item)).toList();
    }

    private BudgetItem mapExpenseResponseToBudgetItem(ExpenseResponse expenseResponse){
        BudgetItem item = new BudgetItem();
        item.setType(CategoryType.EXPENSE);
        item.setDescription(expenseResponse.getDescription());
        item.setBudgetItemCategory(expenseResponse.getCategory());
        item.setAmount(expenseResponse.getValueSpent());
        item.setDate(expenseResponse.getDate());
        item.setAssetSymbol("");
        item.setAssetTag("");
        item.setQuantity(BigDecimal.valueOf(0));
        item.setCurrency(expenseResponse.getCurrency());
        item.setAlternateAmount(BigDecimal.valueOf(0));
        item.setAlternateCurrency("");
        item.setPaymentMethod(expenseResponse.getPaymentMethod());
        item.setTotalPurchaseValue(expenseResponse.getTotalPurchaseValue());
        item.setNumberOfInstallments(expenseResponse.getNumberOfInstallments());
        item.setCurrentInstallment(expenseResponse.getCurrentInstallment());
        return item;

    }

    private BudgetItem mapInvestmentResponseToBudgetItem(InvestmentResponse investmentResponse){
        BudgetItem item = new BudgetItem();
        item.setType(CategoryType.INVESTMENT);
        item.setDescription("");
        item.setBudgetItemCategory(investmentResponse.getInvestmentType());
        item.setAmount(investmentResponse.getAmountInvested());
        item.setDate(investmentResponse.getInvestmentDate());
        item.setAssetSymbol(investmentResponse.getAssetSymbol());
        item.setAssetTag(investmentResponse.getAssetTag());
        item.setQuantity(investmentResponse.getQuantity());
        item.setCurrency(investmentResponse.getCurrency());
        item.setAlternateAmount(BigDecimal.valueOf(0));
        item.setAlternateCurrency(investmentResponse.getAlternateCurrency());
        item.setPaymentMethod("");
        item.setTotalPurchaseValue(investmentResponse.getAmountInvested());
        item.setNumberOfInstallments(1);
        item.setCurrentInstallment(1);
        return item;

    }


}
