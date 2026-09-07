package com.patrones.u1.OCP;
// NoDiscount.java
public class NoDiscount implements DiscountStrategy {
    public double apply(double total) { return total; }
}
