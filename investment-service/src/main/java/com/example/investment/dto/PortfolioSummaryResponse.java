package com.example.investment.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.investment.model.AssetHolding;

public class PortfolioSummaryResponse {

    private List<PortfolioEvent> portfolioEvents;

    private List<AssetHolding> assetList;


    private BigDecimal totalAmount;

    private int numberOfAssets;

    public List<PortfolioEvent> getPortfolioEvents() {
        return portfolioEvents;
    }

    public List<AssetHolding> getAssetList() {
        return assetList;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public int getNumberOfAssets() {
        return numberOfAssets;
    }

    public void setPortfolioEvents(List<PortfolioEvent> portfolioEvents) {
        this.portfolioEvents = portfolioEvents;
    }

    public void setAssetList(List<AssetHolding> assetList) {
        this.assetList = assetList;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setNumberOfAssets(int numberOfAssets) {
        this.numberOfAssets = numberOfAssets;
    }
}
