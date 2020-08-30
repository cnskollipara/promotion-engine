package com.cart.promotion.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Promotion {
    protected String type;
    protected int offerPrice;
}
