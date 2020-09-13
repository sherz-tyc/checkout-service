package com.noths.demo.checkout.utils;

import java.math.BigDecimal;
import java.util.List;

import com.noths.demo.checkout.entity.Item;

public final class CheckoutUtils {
	
	public static BigDecimal calculateCartTotal(List<Item> items) {
		return items.stream()
				.map(item -> item.getPromotionalPrice() == null? 
						item.getPrice() : item.getPromotionalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public static BigDecimal calculateCartSubTotal(List<Item> items) {
		return items.stream()
				.map(item -> item.getPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
}
