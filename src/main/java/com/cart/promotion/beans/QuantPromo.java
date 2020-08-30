package com.cart.promotion.beans;

public class QuantPromo extends Promotion{

    private int buys;

    public QuantPromo(int buys, int offerPrice) {
        this.type = "Quant";
        this.buys = buys;
        this.offerPrice = offerPrice;
    }

    //@Override
    public long calculate(int quantity, Sku sku) {
        int setCnt = quantity / buys;
        int result = (setCnt * this.offerPrice) + (quantity % buys) * sku.price;
        return result;
    }
}
