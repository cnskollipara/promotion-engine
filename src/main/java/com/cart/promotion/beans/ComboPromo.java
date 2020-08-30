package com.cart.promotion.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

@Getter
@Setter
public class ComboPromo extends Promotion {

    private Set<Sku> comboSkus;

    public ComboPromo(Set<Sku> combos, int offer) {
        this.type = "Combo";
        this.comboSkus = combos;
        this.offerPrice = offer;
    }

    public long calculate(Map<Sku, Integer> skuBuys, int quantity, int price) {
        OptionalInt optionalCnt = comboSkus.stream().map(s -> skuBuys.getOrDefault(s, 0)).mapToInt(x -> x).min();
        int setCnt = optionalCnt.orElse(0);
        int proportion = comboSkus.size();
        int result = setCnt * (offerPrice/proportion);
        int residueCnt = quantity-setCnt;
        if(residueCnt > 0) {
            result += residueCnt * price;
        }
        //pairSku.minus(setCnt);
        return result;
    }
}
