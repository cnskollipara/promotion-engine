package com.cart.promotion.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Sku {
    String id;
    Integer price;

    public Sku(String id, int price) {
        this.id = id;
        this.price = price;
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
