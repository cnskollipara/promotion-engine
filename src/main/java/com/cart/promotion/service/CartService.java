package com.cart.promotion.service;

import com.cart.promotion.beans.Sku;
import com.cart.promotion.engine.PromotionEngine;
import com.cart.promotion.mapper.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    PromotionEngine engine;

    public Long orderValuation(List<String> skuList) {
        Map<Sku, Integer> skuBuys = groupSkus(skuList);
        Long total = estimate(skuBuys);
        System.out.println(skuList + " => total : " + total);
        return total;
    }

    private Long estimate(Map<Sku, Integer> skuOrders) {
        Map<Sku,Long> skuTotals = engine.compute(skuOrders);
        return skuTotals.values().parallelStream().reduce(0l, Long::sum);
    }

    private Map<Sku, Integer> groupSkus(List<String> skuList) {
        Map<Sku, Integer> skuBuys = new HashMap<>();
        skuList.forEach(s -> {
            Sku sku = skuMapper.getSku(s);
            skuBuys.put(sku, skuBuys.getOrDefault(sku, 0) + 1);
        });
        return skuBuys;
    }
}
