package com.budget.service;

import com.budget.dto.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExpenseServiceClient {

    @Value("${expense.service.url}")
    private String expenseServiceURL;

    private final RestTemplate restTemplate;

    @Autowired
    public ExpenseServiceClient(RestTemplateBuilder builder){
        this.restTemplate = builder.build();
    }

    public List<ExpenseResponse> getExpensesByMonth(int year, int month){
        String url = String.format(
                "%s/api/expense/report/monthly?year=%s&month=%s",
                expenseServiceURL, year, month);
    }


}
