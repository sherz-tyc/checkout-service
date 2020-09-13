package com.noths.demo.checkout.promotion;

import java.math.BigDecimal;
import java.util.List;

import com.noths.demo.checkout.entity.Item;

/**
 * Apply a given promotional price for applicable product if quantity ordered is equal or 
 * greater than minimum quantity threshold.
 */
public class MultiBuyDiscount implements Promotion {
	
	private Long targetSKU;
	private Long minQuantity;
	private BigDecimal promotionalPrice;
	
	/**
	 * @param targetSKU applicable product.
	 * @param minQuantity minimum order quantity to qualify for this promotion.
	 * @param promotionalPrice the promotional price of product if qualify for discount.
	 */
	public MultiBuyDiscount(Long targetSKU, Long minQuantity, BigDecimal promotionalPrice) {
		this.targetSKU = targetSKU;
		this.minQuantity = minQuantity;
		this.promotionalPrice = promotionalPrice;
	}

	@Override
	public boolean isApplicable(List<Item> items) {
		
		return getNumberOfItemsMatchingTargetSKU(items) >= this.minQuantity;
	}
	
	private Long getNumberOfItemsMatchingTargetSKU(List<Item> items) {
		return items.stream()
					.filter(item -> item.getSKU() == this.targetSKU)
					.count();
	}

	/**
	 * Items are eligible when:
	 * item SKU is equal to promotion target SKU; AND
	 * minimum order quantity is reached
	 */
	@Override
	public List<Item> apply(List<Item> items) {
		if (isApplicable(items)) {
			
			items.stream()
				.filter(item -> item.getSKU() == this.targetSKU)
				.forEach(item -> item.setPromotionalPrice(this.promotionalPrice));
		} 
		return items;
		
	}

}
