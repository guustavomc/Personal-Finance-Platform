package com.budget.service;

import com.budget.dto.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

@Service
public class ExpenseServiceClient {

    @Value("${expense.service.url:}")
    private String expenseServiceURL;

    private final RestClient restClient;
    @Autowired
    public ExpenseServiceClient(RestClient.Builder builder){
        this.restClient = builder.baseUrl(expenseServiceURL).build();
    }

    public List<ExpenseResponse> getExpensesByMonth(int year, int month){
        String uri = "/api/expense/report/monthly?year={year}&month={month}";

        return restClient.get()
                .uri(uri, year, month)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ExpenseResponse>>() {
        });
    }


}
