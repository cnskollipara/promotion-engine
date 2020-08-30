package com.cart.promotion.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Sku {
    Character id;
    int price;
    int count = 0;
    Promotion promotion;

    public Sku(Character id, int price, Promotion promotion) {
        this.id = id;
        this.price = price;
        this.promotion = promotion;
    }

    public void add(int i) {
        this.count += i;
    }

    public void add() {
        this.count++;
    }

    public void minus(int i) {
        if (this.count - i < 0) {
            this.count = 0;
        }else {
            this.count -= i;
        }
    }

    public void minus() {
        minus(1);
    }

    public long getSkuTotal() {
        long tot = promotion.calculate(count, price);
        System.out.println("Sku id : " + id + " with count : " + count + " estimates to : " + tot + " under the Promotion : " + promotion.type);
        return tot;
    }

    public void reset() {
        this.count = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return Objects.equals(id, sku.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
