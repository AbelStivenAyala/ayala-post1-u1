// RegularDiscount.java
package com.patrones.u1.OCP;
public class RegularDiscount implements DiscountStrategy {
    public double apply(double total) { return total * 0.95; }
}
