package com.noths.demo.checkout.promotion;

import java.math.BigDecimal;
import java.util.List;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.utils.CheckoutUtils;

/**
 * Apply a given percentage discount on the calculated total of given {@link Item} list
 * if the total is equal or greater than given minimum spend threshold.
 */
public class DiscountBasedOnSpending implements Promotion {
	
	private BigDecimal minSpending;
	private double discountPercentage;
	
	/**
	 * @param minSpending minimum amount spent in order to qualify for this promotional discount.
	 * @param discountPercentage amount of discount to be taken off the total, e.g. 10d = 10% off
	 */
	public DiscountBasedOnSpending(BigDecimal minSpending, double discountPercentage) {
		this.minSpending = minSpending;
		this.discountPercentage = discountPercentage;
	}

	@Override
	public boolean isApplicable(List<Item> items) {
		
		return CheckoutUtils.calculateCartTotal(items).compareTo(this.minSpending) >= 0;
	}
	
	/**
	 * Items are eligible when:
	 * The sum of Promotional Prices of existing items is equal or 
	 * greater than the minSpending threshold.
	 * 
	 * @param items List of {@link Item} representing shopping cart.
	 */
	@Override
	public List<Item> apply(final List<Item> items) {
		
		if (isApplicable(items)) {
			
			items.stream()
				.forEach(item -> {
					item.setPromotionalPrice(
							BigDecimal.valueOf((100d - this.discountPercentage) / 100d).multiply(
								item.getPromotionalPrice() == null? item.getPrice() : item.getPromotionalPrice()));
					});
		} 
		return items;
	}

}
