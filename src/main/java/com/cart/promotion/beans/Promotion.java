package com.cart.promotion.beans;

public abstract class Promotion {
    protected String type;
    protected int offerPrice;

    public abstract long calculate(int n, int price);
}
