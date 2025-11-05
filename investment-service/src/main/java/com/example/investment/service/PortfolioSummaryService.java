package com.example.investment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;

@Service
public class PortfolioSummaryService {

    private InvestmentRepository investmentRepository;

    public PortfolioSummaryService(InvestmentRepository investmentRepository){
        this.investmentRepository=investmentRepository;
    }

    public PortfolioSummaryResponse getPortfolioSummary(){
        PortfolioSummaryResponse response = new PortfolioSummaryResponse();

        Map<String, AssetHolding> currentHoldingMap = getStringAssetHoldingMap(
                investmentRepository.findAll());

        List<AssetHolding> assetList = new ArrayList<>(currentHoldingMap.values());
        BigDecimal totalAmount = getAssetListTotalAmountInvested(assetList);

        response.setAssetList(assetList);
        response.setTotalAssets(assetList.size());
        response.setTotalAmountInvested(totalAmount);
        return response;
    }

    private static BigDecimal getAssetListTotalAmountInvested(List<AssetHolding> assetList) {
        return assetList.stream()
                .map(AssetHolding::getTotalAmountInvested)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, AssetHolding> getStringAssetHoldingMap(List<Investment> investments) {
        Map<String, AssetHolding> currentHoldingMap = investments.stream()
                .collect(Collectors.groupingBy(
                        investment -> investment.getAssetSymbol() + '|' +investment.getInvestmentType(),
                        Collectors.reducing(
                                new AssetHolding(), this::mapInvestmentToHolding,this::mergeHoldings)));
        return currentHoldingMap;
    }


    private AssetHolding mapInvestmentToHolding(Investment investment){
        AssetHolding holding = new AssetHolding();
        holding.setInvestmentType(investment.getInvestmentType());
        holding.setAssetSymbol(investment.getAssetSymbol());
        holding.setTotalAmountInvested(investment.getAmountInvested());
        holding.setTotalQuantity(investment.getQuantity());
        holding.setPrimaryCurrency(investment.getCurrency());
        holding.setAlternateTotalAmountInvested(investment.getAlternateAmount());
        holding.setAlternateCurrency(investment.getAlternateCurrency());
        
        return holding;
    }

    private AssetHolding mergeHoldings(AssetHolding firstAsset, AssetHolding secondAsset) {
        firstAsset.setTotalQuantity(firstAsset.getTotalQuantity().add(secondAsset.getTotalQuantity()));
        firstAsset.setTotalAmountInvested(firstAsset.getTotalAmountInvested().add(secondAsset.getTotalAmountInvested()));
        return firstAsset;
    }
}
