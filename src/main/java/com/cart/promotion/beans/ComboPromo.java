package com.cart.promotion.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComboPromo extends Promotion {

    private Sku pairSku;

    public ComboPromo(Sku sku, int offer) {
        this.type = "Combo";
        this.pairSku = sku;
        this.offerPrice = offer;
    }

    @Override
    public long calculate(int quantity, int price) {
        int setCnt = Math.min(quantity, pairSku.count);
        int result = setCnt * offerPrice;
        int residueCnt = quantity-setCnt;
        if(residueCnt > 0) {
            result += residueCnt * price;
        }
        pairSku.minus(setCnt);
        return result;
    }
}
