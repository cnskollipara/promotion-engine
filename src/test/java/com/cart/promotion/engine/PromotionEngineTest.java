package com.cart.promotion.engine;

import com.cart.promotion.beans.*;
import com.cart.promotion.mapper.SkuMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PromotionEngineTest {

    @InjectMocks
    PromotionEngine promotionEngine;

    @Mock
    SkuMapper skuMapper;

    @Before
    public void setup() {
        Sku aSku = new Sku("A", 50);
        Sku bSku = new Sku("B", 30);
        Sku cSku = new Sku("C", 20);
        Sku dSku = new Sku("D", 15);

        Promotion aPromo = new QuantPromo(3, 130);
        when(skuMapper.getSku("A")).thenReturn(aSku);
        when(skuMapper.getPromo(aSku)).thenReturn(aPromo);

        Promotion bPromo = new QuantPromo(2, 45);
        when(skuMapper.getSku("B")).thenReturn(bSku);
        when(skuMapper.getPromo(bSku)).thenReturn(bPromo);

        Set<Sku> skuSets = new HashSet<>();
        skuSets.add(cSku);
        skuSets.add(dSku);
        Promotion cNdCombo = new ComboPromo(skuSets, 30);

        when(skuMapper.getSku("C")).thenReturn(cSku);
        when(skuMapper.getPromo(cSku)).thenReturn(cNdCombo);

        when(skuMapper.getSku("D")).thenReturn(dSku);
        when(skuMapper.getPromo(dSku)).thenReturn(cNdCombo);
    }

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
