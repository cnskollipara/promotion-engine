package com.cart.promotion.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    CartService cartService;

    @BeforeAll
    public static void init() {
        //initialization
    }

    @Test
    public void test_no_promo_applied() {
        assertEquals(cartService.orderValuation(Arrays.asList("A", "B", "C")), 100);
    }

    @Test
    public void test_quantitative_promos_applied() {
        assertEquals(cartService.orderValuation(Arrays.asList("A", "B", "C", "B", "A", "A", "B", "A", "A", "B", "B")), 370);
    }

    @Test
    public void test_quantitative_and_combo_promos_applied() {
        assertEquals(cartService.orderValuation(Arrays.asList("A", "B", "C", "D", "A", "A", "B", "B", "B", "B")), 280);
    }
}
