package com.noths.demo.checkout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.noths.demo.checkout.MockedShoppingCarts.buyMultipleOfSameItem;
import static com.noths.demo.checkout.MockedShoppingCarts.combineMultiBuyAndMinimumSpendingDiscount;
import static com.noths.demo.checkout.MockedShoppingCarts.createPromotionalItem;
import static com.noths.demo.checkout.MockedShoppingCarts.minimumSpendingDiscount;
import static com.noths.demo.checkout.MockedShoppingCarts.mixtureOfItemsTotallingExactlySixty;
import static com.noths.demo.checkout.MockedShoppingCarts.multiBuyPromotion;
import static com.noths.demo.checkout.MockedShoppingCarts.travelcardHolderAndTshirtTotallingLessThanSixty;
import static com.noths.demo.checkout.MockedShoppingCarts.uniqueItemsTotallingMoreThanSixty;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.processor.DefaultDiscountProcessor;
import com.noths.demo.checkout.processor.DiscountProcessor;
import com.noths.demo.checkout.promotion.Promotion;
import com.noths.demo.checkout.service.Checkout;
import com.noths.demo.checkout.service.CheckoutImpl;

public class CheckoutIntegrationTest {
	
	@DisplayName("Checkout feature integration test")
	@MethodSource
    @ParameterizedTest(name = "{0}")
    void checkoutReturnsCorrectValues(String description, List<Item> simulatedCart,
    		Set<Promotion> promotions, Double expectedTotal) {
 	   
		DiscountProcessor processor = new DefaultDiscountProcessor();
		promotions.stream().forEach(processor::addPromotion);
 	   	Checkout checkout = new CheckoutImpl(processor);
 	   	simulatedCart.forEach(checkout::scan);
 	   	
        assertEquals(expectedTotal, checkout.total());
    }
    
    static Stream<Arguments> checkoutReturnsCorrectValues() {
        return Stream.of(
        		noItemMultiPromotion(),
        		itemsEligibleForMultiBuyPromotionOnly(),
        		itemsEligibleForTotalSpendingDiscountPromotionOnly(),
        		itemsEligibleForMultiplePromotion(),
        		oneIneligibleItemInCartMultiPromotion(),
        		hundredItemsOnMultiBuyPromotion(),
        		itemsTotallingOnExactlyMinimumSpendThreshold(),
        		allItemsWithSmallPrices()
        );
    }
    
    private static Arguments noItemMultiPromotion() {
        return Arguments.of("GIVEN no item in cart and multiple promotions"
        			+ "WHEN get total THEN expect 0.0",
        		Collections.emptyList(), 
        		combineMultiBuyAndMinimumSpendingDiscount(), 0d);
    }
    
    private static Arguments itemsEligibleForMultiBuyPromotionOnly() {
        return Arguments.of("GIVEN items eligible for only multi-buy promotion"
        			+ "WHEN get total THEN expect total of item promotional prices",
        		twoItemsEligibleForMultiBuyPromotion(),
        		combineMultiBuyAndMinimumSpendingDiscount(), 36.95d);
    }
    
    
    private static Arguments itemsEligibleForTotalSpendingDiscountPromotionOnly() {
    	return Arguments.of("GIVEN items eligible for only 10% discount having spent over £60"
    				+ "WHEN get total THEN expect total of item promotional prices",
    			uniqueItemsTotallingMoreThanSixty(),
    			combineMultiBuyAndMinimumSpendingDiscount(), 66.78d);
    }
    
    private static Arguments itemsEligibleForMultiplePromotion() {
    	return Arguments.of("GIVEN items eligible for both multi-buy and 10% discount"
        			+ "WHEN get total THEN expect total of item promotional prices",
				itemInCartEligibleForMultiplePromotions(),
        		combineMultiBuyAndMinimumSpendingDiscount(), 73.76d);
    }
    
    private static Arguments oneIneligibleItemInCartMultiPromotion() {
        return Arguments.of("GIVEN only one ineligible item and multiple promotions"
        			+ "WHEN get total THEN expect item unit price",
        		oneItemInCartOnly(),
        		combineMultiBuyAndMinimumSpendingDiscount(), 9.25d);
    }

    
    private static Arguments hundredItemsOnMultiBuyPromotion() {
    	return Arguments.of("GIVEN only Multi-Buy promotion and 100 qualifying items"
    				+ "WHEN get total THEN expect promotional price total",
    			buyMultipleOfSameItem(1l, new BigDecimal("9.25"), 100),
            	multiBuyPromotion(), 850.00d);
    }
    
    private static Arguments itemsTotallingOnExactlyMinimumSpendThreshold() {
    	return Arguments.of("GIVEN only promotion based on minimum spending and items total "
					+ "reaches minimum WHEN get total THEN expect promotional price total",
				mixtureOfItemsTotallingExactlySixty(),
				minimumSpendingDiscount(), 54.00d);
    }
    
    private static Arguments allItemsWithSmallPrices() {
    	return Arguments.of("GIVEN six items priced at the minimum currency"
					+ "WHEN get total THEN expect unit price total",
				allSmallPricedItems(),
        		combineMultiBuyAndMinimumSpendingDiscount(), 0.06d);
    }
    
    private static List<Item> oneItemInCartOnly() {
 	   return Arrays.asList(createPromotionalItem(1l, "9.25", null));
    }
    
    private static List<Item> twoItemsEligibleForMultiBuyPromotion() {
 	   return travelcardHolderAndTshirtTotallingLessThanSixty();
    }
    
    private static List<Item> itemInCartEligibleForMultiplePromotions() {
    	return Arrays.asList(createPromotionalItem(1l, "9.25", null),
    			createPromotionalItem(1l, "9.25", null),
    			createPromotionalItem(2l, "45.00", null),
    			createPromotionalItem(3l, "19.95", null));
    }
    
    private static List<Item> allSmallPricedItems() {
 	   return Arrays.asList(createPromotionalItem(6l, "0.01", null),
 			   createPromotionalItem(6l, "0.01", null),
 			   createPromotionalItem(6l, "0.01", null),
 			   createPromotionalItem(6l, "0.01", null),
 			   createPromotionalItem(6l, "0.01", null),
 			   createPromotionalItem(6l, "0.01", null));
    }
    

}
