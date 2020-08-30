package com.cart.promotion.service;

import com.cart.promotion.beans.ComboPromo;
import com.cart.promotion.beans.QuantPromo;
import com.cart.promotion.beans.Sku;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static Map<Character, Sku> skuMap = new HashMap<>();
    private static Map<Character, Integer> quantOffer = new HashMap<>();
    private static Map<Character, Integer> comboOffer = new HashMap<>();

    static {
        skuMap.put('A', new Sku('A', 50, new QuantPromo(3, 130)));
        skuMap.put('B', new Sku('B', 30, new QuantPromo(2, 45)));
        skuMap.put('C', new Sku('C', 20, null));
        skuMap.put('D', new Sku('D', 15, null));
        skuMap.get('C').setPromotion(new ComboPromo(skuMap.get('D'), 30));
        skuMap.get('D').setPromotion(new ComboPromo(skuMap.get('C'), 30));
    }

    public Long orderValuation(List<Character> skuList) {
        Set<Sku> orderedSkus = groupSkus(skuList);
        Long total = estimate(orderedSkus);
        return total;
    }

    private Long estimate(Set<Sku> skus) {
        return skus.stream().mapToLong(Sku :: getSkuTotal).sum();
    }

    private Set<Sku> groupSkus(List<Character> skuList) {
        Set<Sku> skus = new HashSet<>();
        skuList.forEach(s -> {
            Sku sku = skuMap.get(s);
            sku.add();
            skus.add(sku);
        });
        return skus;
    }

    public static void main(String[] args) {
        CartService cartService = new CartService();
        List<Character> items = Arrays.asList('A', 'B', 'C');
        //List<Character> items = Arrays.asList('A', 'B', 'C', 'B', 'A', 'A', 'B', 'A', 'A', 'B', 'B');
        //List<Character> items = Arrays.asList('A', 'B', 'C', 'D', 'A', 'A', 'B', 'B', 'B', 'B');
        System.out.println("Total order value : " + cartService.orderValuation(items));
    }
}
