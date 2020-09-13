package com.noths.demo.checkout.processor;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.promotion.DiscountBasedOnSpending;
import com.noths.demo.checkout.promotion.Promotion;
import com.noths.demo.checkout.service.Checkout;

/**
 * In practice, Promotions should be stored in persistent form, rather
 * than an in-memory {@link Set}.
 *
 */
public class DefaultDiscountProcessor implements DiscountProcessor {
	
	private Set<Promotion> promotions;
	
	public DefaultDiscountProcessor() {
		this.promotions = new HashSet<>();
	}

	/**
	 * The input {@link List} of {@link Item} is evaluated against each
	 * promotion scheme (if any).
	 * The default requirement is to apply any discounts affecting the 
	 * checkout total at the very last; other product-based promotional 
	 * discounts take priority.
	 * 
	 * @param items {@link List} of {@link Item} in {@link Checkout}
	 * @return Same list of items with updated promotional prices where
	 * applicable
	 */
	@Override
	public List<Item> applyDiscount(final List<Item> items) {
		
		clearExistingPromotions(items);
		List<Item> discountedItems = items.stream()
				.map(Item::clone).collect(Collectors.toList());
		
		/* First, evaluate product-based discount(s) */
		for(Promotion promo : this.promotions.stream().filter(promo -> 
			!(promo instanceof DiscountBasedOnSpending)).collect(Collectors.toList())) {
			
			discountedItems = promo.apply(discountedItems);
		}
		
		/* Lastly, evaluate discount(s) which affects the checkout total price */
		for(Promotion promo : this.promotions.stream().filter(promo -> 
			(promo instanceof DiscountBasedOnSpending)).collect(Collectors.toList())) {
			
			discountedItems = promo.apply(discountedItems);
		}
			
		return discountedItems;
	}
	
	private void clearExistingPromotions(List<Item> items) {
		items.stream().forEach(item -> item.setPromotionalPrice(null));
	}
	
	public Set<Promotion> getPromotions() {
		return Collections.unmodifiableSet(this.promotions);
	}

	@Override
	public void addPromotion(Promotion promotion) {
		this.promotions.add(promotion);
	}

	@Override
	public void removePromotion(Promotion promotion) {
		this.promotions.remove(promotion);
		
	}

}
