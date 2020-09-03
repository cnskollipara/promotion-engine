package com.cart.promotion.beans;

import static com.cart.promotion.beans.PromotionType.Quantitative;

public class QuantPromo extends Promotion{

    private int buys;

    public QuantPromo(int buys, int offerPrice) {
        this.type = Quantitative;
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
