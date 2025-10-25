package com.example.investment.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.investment.model.AssetHolding;

public class PortfolioSummaryResponse {

    private BigDecimal totalAmountInvested;

    private List<AssetHolding> assetList;
    

    public BigDecimal getTotalAmountInvested() {
        return totalAmountInvested;
    }

    public List<AssetHolding> getAssetList() {
        return assetList;
    }

    public void setTotalAmountInvested(BigDecimal totalAmountInvested) {
        this.totalAmountInvested = totalAmountInvested;
    }

    public void setAssetList(List<AssetHolding> assetList) {
        this.assetList = assetList;
    }
}
