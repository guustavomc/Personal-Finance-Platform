package com.budget.service;

import com.budget.dto.*;
import com.budget.model.*;
import com.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseServiceClient expenseServiceClient;

    @Autowired
    private InvestmentServiceClient investmentServiceClient;

    public BudgetResponse saveBudget(CreateBudgetRequest createBudgetRequest){
        Budget budget = new Budget();
        budget.setName(createBudgetRequest.getName());
        budget.setBudgetPeriodType(createBudgetRequest.getBudgetPeriodType());
        budget.setStartDate(createBudgetRequest.getStartDate());

        budget.setEndDate(adjustBudgetEndDate(createBudgetRequest));

        budget.setBudgetStatus(BudgetStatus.ACTIVE);

        budget.setTotalPlannedAmount(createBudgetRequest.getTotalPlannedAmount());

        mapBudgetCategoryRequestToNewBudget(createBudgetRequest, budget);

        return mapBudgetToBudgetResponse(budgetRepository.save(budget));
    }

    private static void mapBudgetCategoryRequestToNewBudget(CreateBudgetRequest createBudgetRequest, Budget budget) {
        for(BudgetCategoryRequest category: createBudgetRequest.getCategories()){
            BudgetCategory newCategory = new BudgetCategory();
            newCategory.setCategoryName(category.getCategoryName());
            newCategory.setType(category.getType());
            newCategory.setPlannedAmount(category.getPlannedAmount());
            newCategory.setPercentageOfTotal(category.getPercentageOfTotal() != null ?
                    category.getPercentageOfTotal() : BigDecimal.ZERO);

            budget.addCategory(newCategory);
        }
    }

    private void updateBudgetCategoryActualValues(BudgetCategory category){
        List<BudgetItem> items =
                buildBudgetItemsTimeline(
                        category.getBudget().getStartDate().getYear(),
                        category.getBudget().getStartDate().getMonthValue());

        for(BudgetItem item: items){
            if(category.getCategoryName().equals(item.getBudgetItemCategory())){
                if (category.getType().equals(CategoryType.EXPENSE)){
                    category.setActualSpent(category.getActualSpent().add(item.getAmount()));
                }
                if (category.getType().equals(CategoryType.INVESTMENT)){
                    category.setActualInvested(category.getActualInvested().add(item.getAmount()));
                }
            }
        }

    }

    private List<BudgetItem> buildBudgetItemsTimeline(int year, int month){
        List<BudgetItem> expenses = getExpensesFromMonth(year, month);
        List<BudgetItem> investments = getInvestmentsFromMonth(year, month);

        return Stream
                .concat(expenses.stream(), investments.stream())
                .sorted((a,b) -> a.getDate().compareTo(b.getDate()))
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

    private LocalDate adjustBudgetEndDate(CreateBudgetRequest createBudgetRequest) {
        LocalDate endDate;
        if (createBudgetRequest.getEndDate()==null){
            endDate = createBudgetRequest.getBudgetPeriodType().calculateEndDate(createBudgetRequest.getStartDate());
        }
        else{
            endDate = createBudgetRequest.getEndDate();
        }
        return endDate;
    }

    private BudgetResponse mapBudgetToBudgetResponse(Budget budget){
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setUserId(budget.getUserId());
        response.setName(budget.getName());
        response.setBudgetPeriodType(budget.getBudgetPeriodType());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        response.setBudgetStatus(budget.getBudgetStatus());
        response.setTotalPlannedAmount(budget.getTotalPlannedAmount());
        response.setTotalActualSpent(budget.getTotalActualSpent());
        response.setTotalActualInvested(budget.getTotalActualInvested());
        response.setCategories(budget.getCategories());
        response.setCreatedAt(budget.getCreatedAt());
        response.setUpdatedAt(budget.getUpdatedAt());

        return response;
    }


}
