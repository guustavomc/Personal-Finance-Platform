package com.example.investment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.investment.dto.PortfolioEvent;
import com.example.investment.repository.WithdrawalRepository;
import org.springframework.stereotype.Service;
import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;

@Service
public class PortfolioSummaryService {

    private InvestmentRepository investmentRepository;
    private WithdrawalRepository withdrawalRepository;

    public PortfolioSummaryService(InvestmentRepository investmentRepository, WithdrawalRepository withdrawalRepository){
        this.investmentRepository=investmentRepository;
        this.withdrawalRepository=withdrawalRepository;
    }

    public PortfolioSummaryResponse getPortfolioSummary(){
        PortfolioSummaryResponse response = new PortfolioSummaryResponse();

        Map<String, AssetHolding> currentHoldingMap = getStringAssetHoldingMap();

        List<AssetHolding> assetList = new ArrayList<>(currentHoldingMap.values());
        BigDecimal totalAmount = getAssetListTotalAmountInvested(assetList);

        response.setTotalAmount(totalAmount);
        response.setAssetList(assetList);
        response.setNumberOfAssets(assetList.size());
        response.setPortfolioEvents(buildPortfolioEventTimeline());
        return response;
    }

    private static BigDecimal getAssetListTotalAmountInvested(List<AssetHolding> assetList) {
        return assetList.stream()
                .map(AssetHolding::getTotalAmountInvested)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, AssetHolding> getStringAssetHoldingMap() {
        List<Investment> investments = investmentRepository.findAll();
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

    private List<PortfolioEvent> buildPortfolioEventTimeline(){
        return Stream
                .concat(mapInvestmentListToPortfolioEventList().stream(), mapWithdrawalListToPortfolioEventList()
                        .stream())
                .sorted((a,b) -> a.getDate().compareTo(b.getDate())).toList();
    }

    private List<PortfolioEvent> mapInvestmentListToPortfolioEventList(){
        return investmentRepository.findAll().stream()
                .map(inv -> new PortfolioEvent(
                        inv.getInvestmentType(),
                        inv.getAssetSymbol(),
                        inv.getAmountInvested(),
                        inv.getQuantity(),
                        inv.getInvestmentDate(),
                        inv.getCurrency(),
                        inv.getFee(),
                        "INVESTMENT",
                        inv.getAssetTag()))
                .toList();
    }

    private List<PortfolioEvent> mapWithdrawalListToPortfolioEventList(){
        return withdrawalRepository.findAll().stream()
                .map(withdrawal -> new PortfolioEvent(
                        withdrawal.getInvestmentType(),
                        withdrawal.getAssetSymbol(),
                        withdrawal.getProceeds(),
                        withdrawal.getQuantity(),
                        withdrawal.getWithdrawalDate(),
                        withdrawal.getCurrency(),
                        withdrawal.getFee(),
                        "WITHDRAWAL",
                        withdrawal.getAssetTag()))
                .toList();
    }
}
