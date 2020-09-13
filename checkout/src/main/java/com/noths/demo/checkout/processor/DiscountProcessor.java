package com.noths.demo.checkout.processor;

import java.util.List;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.promotion.Promotion;

public interface DiscountProcessor {
	public List<Item> applyDiscount(final List<Item> items);
	
	public void addPromotion(Promotion promotion);
	
	public void removePromotion(Promotion promotion);

}
