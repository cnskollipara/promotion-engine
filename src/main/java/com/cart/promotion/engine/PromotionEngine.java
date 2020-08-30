package com.cart.promotion.engine;

import com.cart.promotion.beans.ComboPromo;
import com.cart.promotion.beans.Promotion;
import com.cart.promotion.beans.QuantPromo;
import com.cart.promotion.beans.Sku;
import com.cart.promotion.mapper.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

@Component
public class PromotionEngine {

    @Autowired
    SkuMapper skuMapper;


    public Long compute(Map<Sku, Integer> skuOrders) {
        Map<Sku, Long> skuTotals = new HashMap<>();
        skuOrders.forEach( (sku, cnt) -> {
            Promotion promotion = skuMapper.getPromo(sku);
            long val = 0l;
            switch ((promotion.getType())) {
                case "Quant" :
                    QuantPromo quantPromo = (QuantPromo) promotion;
                    val = quantPromo.calculate(cnt, sku);
                    break;
                case "Combo" :
                    ComboPromo comboPromo = (ComboPromo) promotion;
                    val = comboPromo.calculate(skuOrders, cnt, sku);
            }
            skuTotals.put(sku, val);
        });
        return skuTotals.values().stream().mapToLong(x -> x).sum();
    }
}
