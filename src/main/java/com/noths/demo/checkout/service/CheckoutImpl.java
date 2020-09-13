package com.noths.demo.checkout.service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.processor.DiscountProcessor;
import com.noths.demo.checkout.utils.CheckoutUtils;

public class CheckoutImpl implements Checkout {
	
	private final List<Item> items;
	private DiscountProcessor discountProcessor;
	
	/**
	 * As total is calculated with applicable discounts, a discount processor is
	 * used to apply discounts.
	 * 
	 * @param discountProcessor processor which handles discount applications.
	 */
	public CheckoutImpl(DiscountProcessor discountProcessor) {
		this.discountProcessor = discountProcessor;
		items = new ArrayList<>();
	}

	@Override
	public void scan(final Item item) {
		this.items.add(item);
	}
	
	@Override
	public List<Item> getCheckoutItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * @return sum of {@link Item} prices, taking in account any eligible discounts
	 */
	@Override
	public Double total() {
		return CheckoutUtils.calculateCartTotal(getDiscountedState())
			.setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
		
	}

	/**
	 * @return sum of {@link Item} base prices, without any discounts.
	 */
	public Double subTotal() {
		return CheckoutUtils.calculateCartSubTotal(getCheckoutItems())
			.setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
	}
	
	public List<Item> getDiscountedState() {
		return this.discountProcessor.applyDiscount(this.items);
	}

}
