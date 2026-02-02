package com.budget.service;

import java.util.List;

import com.budget.dto.ExpenseResponse;

import main.java.com.budget.dto.InvestmentResponse;

@Component
public class InvestmentServiceClient {

    @Value("${investment.service.url}")
    private String investmentServiceURL;

    private final RestClient restClient;

    public InvestmentServiceClient(RestClient.Builder builder){
        this.restClient = builder.baseUrl(investmentServiceURL).build();
    }

    public List<ExpenseResponse> getInvestmentsByMonth(int year, int month){
        String uri = "/api/investment/report/monthly?year={year}&month={month}";

        return restClient.get()
                .uri(uri, year, month)
                .retrieve()
                .body(new ParameterizedTypeReference<List<InvestmentResponse>>() {
        });
    }
}
