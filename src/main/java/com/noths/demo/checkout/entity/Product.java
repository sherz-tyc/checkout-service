package com.noths.demo.checkout.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Base class for representing products, simple POJO with essential attributes
 * that identifies any product.
 */
@AllArgsConstructor
@Getter
public class Product implements Cloneable {
	
	private Long sku;
	private BigDecimal unitPrice;
	
	@Override
	public Object clone() {
	    try {
	        return (Product) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return new Product(this.sku, this.unitPrice);
	    }
	}
	
}
