package com.cart.promotion.engine;

import com.cart.promotion.beans.Sku;
import com.cart.promotion.mapper.SkuMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class PromotionEngineTest {

    @Autowired
    PromotionEngine promotionEngine;

    @Autowired
    SkuMapper skuMapper;

    @Test
    public void test_no_promo_applicable() {
        Map<Sku, Integer> skuQuantMap = new HashMap<>();
        skuQuantMap.put(skuMapper.getSku("A"), 1);
        skuQuantMap.put(skuMapper.getSku("B"), 1);
        skuQuantMap.put(skuMapper.getSku("C"), 1);
        Map<Sku, Long> skuTotals = promotionEngine.compute(skuQuantMap);
        Assert.assertNotNull("sku totals not to be empty", skuTotals);
        Assert.assertEquals("number of sku level totals to be matched with sku quantities", skuQuantMap.size(), skuTotals.size());
        Map<Sku, Long> expectedSkuTotals = new HashMap<>();
        expectedSkuTotals.put(skuMapper.getSku("A"), 50l);
        expectedSkuTotals.put(skuMapper.getSku("B"), 30l);
        expectedSkuTotals.put(skuMapper.getSku("C"), 20l);
        for (Map.Entry<Sku, Long> entry : skuTotals.entrySet()) {
            Sku sku = entry.getKey();
            Long expectedValue = expectedSkuTotals.get(sku);
            Assert.assertEquals("Expected sku total matched", expectedValue, entry.getValue());
        }
    }

    @Test
    public void test_quantitative_promo_applicable() {
        Map<Sku, Integer> skuQuantMap = new HashMap<>();
        skuQuantMap.put(skuMapper.getSku("A"), 5);
        skuQuantMap.put(skuMapper.getSku("B"), 5);
        skuQuantMap.put(skuMapper.getSku("C"), 1);
        Map<Sku, Long> skuTotals = promotionEngine.compute(skuQuantMap);
        Assert.assertNotNull("sku totals not to be empty", skuTotals);
        Assert.assertEquals("number of sku level totals to be matched with sku quantities", skuQuantMap.size(), skuTotals.size());
        Map<Sku, Long> expectedSkuTotals = new HashMap<>();
        expectedSkuTotals.put(skuMapper.getSku("A"), 230l);
        expectedSkuTotals.put(skuMapper.getSku("B"), 120l);
        expectedSkuTotals.put(skuMapper.getSku("C"), 20l);
        for (Map.Entry<Sku, Long> entry : skuTotals.entrySet()) {
            Sku sku = entry.getKey();
            Long expectedValue = expectedSkuTotals.get(sku);
            Assert.assertEquals("Expected sku total matched", expectedValue, entry.getValue());
        }
    }

    @Test
    public void test_combo_promo_applicable() {
        Map<Sku, Integer> skuQuantMap = new HashMap<>();
        skuQuantMap.put(skuMapper.getSku("A"), 3);
        skuQuantMap.put(skuMapper.getSku("B"), 5);
        skuQuantMap.put(skuMapper.getSku("C"), 1);
        skuQuantMap.put(skuMapper.getSku("D"), 1);
        Map<Sku, Long> skuTotals = promotionEngine.compute(skuQuantMap);
        Assert.assertNotNull("sku totals not to be empty", skuTotals);
        Assert.assertEquals("count of sku level totals to be matched with sku quantities of the cart", skuQuantMap.size(), skuTotals.size());
        Map<Sku, Long> expectedSkuTotals = new HashMap<>();
        expectedSkuTotals.put(skuMapper.getSku("A"), 130l);
        expectedSkuTotals.put(skuMapper.getSku("B"), 120l);
        expectedSkuTotals.put(skuMapper.getSku("C"), 15l);
        expectedSkuTotals.put(skuMapper.getSku("D"), 15l);
        for (Map.Entry<Sku, Long> entry : skuTotals.entrySet()) {
            Sku sku = entry.getKey();
            Long expectedValue = expectedSkuTotals.get(sku);
            Assert.assertEquals("sku total matched", expectedValue, entry.getValue());
        }
    }
}
