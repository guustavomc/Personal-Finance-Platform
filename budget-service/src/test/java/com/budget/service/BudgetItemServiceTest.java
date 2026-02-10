package com.budget.service;

import com.budget.dto.BudgetItem;
import com.budget.dto.ExpenseResponse;
import com.budget.dto.InvestmentResponse;
import com.budget.model.Budget;
import com.budget.model.BudgetCategory;
import com.budget.model.BudgetPeriodType;
import com.budget.model.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetItemServiceTest {

    @Mock
    private ExpenseServiceClient expenseServiceClient;

    @Mock
    private InvestmentServiceClient investmentServiceClient;

    @InjectMocks
    private BudgetItemService budgetItemService;

    @BeforeEach
    public void setup(){
    }

    @Test
    void getBudgetItemsForSpecificBudget_ReturnBudgetItemList(){
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("February");
        budget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budget.setStartDate(LocalDate.of(2026, 2, 1));
        budget.setEndDate(LocalDate.of(2026, 2, 28));
        budget.setTotalPlannedAmount(BigDecimal.valueOf(1000));
        budget.setTotalActualSpent(BigDecimal.ZERO);
        budget.setTotalActualInvested(BigDecimal.ZERO);


        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setBudget(budget);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));
        category1.setActualSpent(BigDecimal.ZERO);
        category1.setActualInvested(BigDecimal.ZERO);

        BudgetCategory category2 = new BudgetCategory();
        category2.setId(3L);
        category2.setBudget(budget);
        category2.setCategoryName("Stock");
        category2.setType(CategoryType.INVESTMENT);
        category2.setPlannedAmount(BigDecimal.valueOf(1000));
        category2.setActualSpent(BigDecimal.ZERO);
        category2.setActualInvested(BigDecimal.ZERO);


        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        budget.setCategories(categories);

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setDescription("Food");
        expenseResponse.setCategory("Food");
        expenseResponse.setValueSpent(BigDecimal.valueOf(1000));
        expenseResponse.setDate(LocalDate.of(2026, 2, 1));
        expenseResponse.setPaymentMethod("Cash");
        expenseResponse.setTotalPurchaseValue(BigDecimal.valueOf(1000));
        expenseResponse.setNumberOfInstallments(1);
        expenseResponse.setCurrentInstallment(1);

        List<ExpenseResponse> expenseList = new ArrayList<>();
        expenseList.add(expenseResponse);

        InvestmentResponse investmentResponse = new InvestmentResponse();
        investmentResponse.setInvestmentType("Stock");
        investmentResponse.setAmountInvested(BigDecimal.valueOf(1000));
        investmentResponse.setInvestmentDate(LocalDate.of(2026, 2, 1));
        investmentResponse.setAssetSymbol("NVDA");
        investmentResponse.setAssetTag("NVDA");
        investmentResponse.setQuantity(BigDecimal.valueOf(1));
        investmentResponse.setCurrency("USD");

        List<InvestmentResponse> investmentList = new ArrayList<>();
        investmentList.add(investmentResponse);


        when(expenseServiceClient.getExpensesByMonth(2026, 02)).thenReturn(expenseList);
        when(investmentServiceClient.getInvestmentsByMonth(2026, 02)).thenReturn(investmentList);


        List<BudgetItem> budgetItems= budgetItemService.getBudgetItemsForSpecificBudget(budget);

        assertEquals(2, budgetItems.size());
        assertEquals("Food", budgetItems.get(0).getDescription());
        assertEquals("Stock", budgetItems.get(1).getDescription());

    }


}
