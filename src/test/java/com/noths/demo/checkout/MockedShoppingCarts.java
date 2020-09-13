package com.noths.demo.checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.entity.Product;
import com.noths.demo.checkout.promotion.DiscountBasedOnSpending;
import com.noths.demo.checkout.promotion.MultiBuyDiscount;
import com.noths.demo.checkout.promotion.Promotion;

public class MockedShoppingCarts {
	
    public static Item aTravelCardHolder() {
    	return createItem(1l, new BigDecimal("9.25"));
    }

    public static Item aPairOfCufflinks() {
    	return createItem(2l, new BigDecimal("45.00"));
    }
    
    public static Item aKidsTShirt() {
    	return createItem(3l, new BigDecimal("19.95"));
    }
    
    public static Item aSquawkingRubberChicken() {
    	return createItem(4l, new BigDecimal("9.99"));
    }
    
    public static Item aPinkElephant() {
    	return createItem(5l, new BigDecimal("25.00"));
    }
    
    public static Item aSpursShirt() {
    	return createItem(6l, new BigDecimal("0.01"));
    }
    
    public static List<Item> uniqueItemsTotallingMoreThanSixty() {
    	return Arrays.asList(aTravelCardHolder(), aPairOfCufflinks(), aKidsTShirt());
    }
    
    public static List<Item> twoTravelCardsOnly() {
    	return Arrays.asList(aTravelCardHolder(), aTravelCardHolder());
    }
    
    public static List<Item> twoTravelCardsAndMixtureOfOthers() {
    	return Arrays.asList(aTravelCardHolder(), aPairOfCufflinks(), aKidsTShirt(), 
    			aTravelCardHolder(), aSquawkingRubberChicken());
    }
    
    public static List<Item> goToTownOnTheseTravelcardHolders() {
    	List<Item> result = new ArrayList<>();
    	result.add(aSquawkingRubberChicken());
    	result.addAll(buyMultipleOfSameItem(1l, new BigDecimal("9.25"), 5));
    	return result;
    }
    
    public static List<Item> noMoreTravelcardHoldersInCart() {
    	return Arrays.asList(aKidsTShirt(), aPairOfCufflinks(), aKidsTShirt(), 
    			aSquawkingRubberChicken());
    }
    
    public static List<Item> mixtureOfItemsTotallingMoreThanSixty() { // total = 83.45
    	return Arrays.asList(aTravelCardHolder(), aPairOfCufflinks(), aKidsTShirt(), 
    			aTravelCardHolder());
    }
    
    public static List<Item> travelcardHolderAndTshirtTotallingLessThanSixty() { // total = 38.45
    	return Arrays.asList(aTravelCardHolder(), aKidsTShirt(), aTravelCardHolder());
    }
    
    public static List<Item> mixtureOfItemsTotallingOnePenceShortOfSixty() {
    	return Arrays.asList(aPinkElephant(), aPinkElephant(), aSquawkingRubberChicken());
    }
    
    public static List<Item> mixtureOfItemsTotallingExactlySixty() {
    	return Arrays.asList(aPinkElephant(), aPinkElephant(), aSquawkingRubberChicken(),
    			aSpursShirt());
    }
    
    public static List<Item> bigSpendingSpreeOnCuffLinks() { // total = 5400
    	return buyMultipleOfSameItem(2l, new BigDecimal("45.00"), 120);
    }
    
    public static List<Item> cartTotallingOnePence() {
    	return Arrays.asList(aSpursShirt());
    }
    
    public static Item createPromotionalItem(Long sku, String unitPriceStr, 
    		String promotionalPriceStr) {
    	
    	Item promoItem = createItem(sku, new BigDecimal(unitPriceStr));
    	if (promotionalPriceStr != null && promotionalPriceStr.length() > 0) {
    		promoItem.setPromotionalPrice(new BigDecimal(promotionalPriceStr));
    	}
    	return promoItem;
    }
    
    public static Set<Promotion> combineMultiBuyAndMinimumSpendingDiscount() {
    	return new HashSet<>(Arrays.asList(new MultiBuyDiscount(1l, 2l, new BigDecimal("8.50")),
    			new DiscountBasedOnSpending(new BigDecimal("60"), 10d)));
    }

    public static Set<Promotion> multiBuyPromotion() {
    	return new HashSet<>(Arrays.asList(new MultiBuyDiscount(1l, 2l, new BigDecimal("8.50"))));
    }

    public static Set<Promotion> minimumSpendingDiscount() {
    	return new HashSet<>(Arrays.asList(new DiscountBasedOnSpending(new BigDecimal("60"), 10d)));
    }
    
    public static List<Item> buyMultipleOfSameItem(Long sku, BigDecimal unitPrice, int quantity) {
    	List<Item> result = new ArrayList<Item>();
    	for (int i=0; i < quantity; i++) {
    		result.add(createItem(sku, unitPrice));
    	}
    	return result;
    	
    }
    
    public static Item createItem(Long sku, BigDecimal unitPrice) {
		return new Item(new Product(sku, unitPrice));
	}
}
