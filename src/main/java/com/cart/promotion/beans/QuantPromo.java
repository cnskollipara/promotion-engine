package com.cart.promotion.beans;

public class QuantPromo extends Promotion{

    private int eligibleCnt;

    public QuantPromo(int quantity, int offer) {
        this.type = "Quant";
        this.eligibleCnt = quantity;
        this.offerPrice = offer;
    }

    @Override
    public long calculate(int quantity, int price) {
        int setCnt = quantity / eligibleCnt;
        int result = (setCnt * this.offerPrice) + (quantity % eligibleCnt) * price;
        return result;
    }
}
