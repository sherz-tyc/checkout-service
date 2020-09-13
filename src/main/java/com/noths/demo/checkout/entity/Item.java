package com.noths.demo.checkout.entity;

import java.math.BigDecimal;

/**
 * This acts as a wrapper class for {@link Product}, it provides
 * additional functionality designed for checkout purpose such 
 * as holding a promotional price.
 *
 */
public class Item implements Cloneable {

	private Product product;
	private BigDecimal promotionalPrice;

	public Item(Product product) {
		this.product = product;
	}

	public Long getSKU() {
		return this.product.getSku();
	}

	public BigDecimal getPrice() {
		return this.product.getUnitPrice();
	}

	public BigDecimal getPromotionalPrice() {
		return this.promotionalPrice;
	}

	public void setPromotionalPrice(BigDecimal promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Item i = (Item) obj;
		return getSKU() != 0l ? getSKU() == i.getSKU() : i.getSKU() == 0;
	}

	@Override
	public int hashCode() {
		return getSKU() != 0l ? Long.valueOf(getSKU()).hashCode() : 0;
	}

	@Override
	public Item clone() {
		Item item = null;
	    try {
	    	item = (Item) super.clone();
	    } catch (CloneNotSupportedException e) {
	    	item = new Item(this.product);
	    }
	    item.product = (Product) this.product.clone();
	    return item;
	}

}
