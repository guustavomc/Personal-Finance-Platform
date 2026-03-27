package com.budget.service;

import java.util.List;

import com.budget.dto.InvestmentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class InvestmentServiceClient {

    @Value("${investment.service.url:}")
    private String investmentServiceURL;

    @Autowired
    private HttpServletRequest request;

    private final RestClient restClient;

    @Autowired
    public InvestmentServiceClient(RestClient.Builder builder){
        this.restClient = builder.baseUrl(investmentServiceURL).build();
    }

    public List<InvestmentResponse> getInvestmentsByMonth(int year, int month){
        String uri = "/api/investment/report/monthly?year={year}&month={month}";

        String token = request.getHeader("Authorization");

        return restClient.get()
                .uri(uri, year, month)
                .header("Authorization", token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<InvestmentResponse>>() {
        });
    }
}
