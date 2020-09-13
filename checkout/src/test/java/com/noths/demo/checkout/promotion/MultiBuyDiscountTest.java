package com.noths.demo.checkout.promotion;

import static com.noths.demo.checkout.MockedShoppingCarts.goToTownOnTheseTravelcardHolders;
import static com.noths.demo.checkout.MockedShoppingCarts.noMoreTravelcardHoldersInCart;
import static com.noths.demo.checkout.MockedShoppingCarts.uniqueItemsTotallingMoreThanSixty;
import static com.noths.demo.checkout.MockedShoppingCarts.twoTravelCardsAndMixtureOfOthers;
import static com.noths.demo.checkout.MockedShoppingCarts.twoTravelCardsOnly;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.noths.demo.checkout.entity.Item;

public class MultiBuyDiscountTest {
	
	@DisplayName("Promotional Price is set on items eligible for Multi-Buy promotion...")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void promotionAppliedAccordingly(String description, Long minQuantity, String promotionalPrice, 
    		List<Item> items, Long expectedNumOfUpdatedItems) {
		
		// Given pre conditions as input arguments
		Long targetSKU = 1l;
		BigDecimal promotionalPriceBigDec = new BigDecimal(promotionalPrice);
		final MultiBuyDiscount target = 
				new MultiBuyDiscount(targetSKU, minQuantity, promotionalPriceBigDec);
		
		// When attempt to apply promotion
		items = target.apply(items);
		
		// Then expect promotional price of only eligible items are updated
		Long actualNumOfItemsUpdated = items.stream()
			.filter(item -> item.getPromotionalPrice() != null && 
				item.getPromotionalPrice().compareTo(promotionalPriceBigDec) == 0)
			.count();
        
		assertEquals(expectedNumOfUpdatedItems, actualNumOfItemsUpdated);
    }

    static Stream<Arguments> promotionAppliedAccordingly() {
        return Stream.of(
        		noItems(),
        		singleApplicableItemNotHittingMinimumQuantity(),
        		twoEligibleItemsHittingMinimumQuantity(),
        		twoEligibleItemsHittingMinimumQuantityAmongstOthers(),
        		fiveEligibleItemsHittingMinimumQuantityAmongstOthers(),
        		noApplicableItemsInCart()
        );
    }
    
    private static Arguments noItems() {
        return Arguments.of("given no items in cart", 2l, "8.50",
        		Collections.emptyList(), 0l);
    }

    private static Arguments singleApplicableItemNotHittingMinimumQuantity() {
        return Arguments.of("given 1 applicable item but not reaching minimum quantity for promotion",
        		2l, "8.50", uniqueItemsTotallingMoreThanSixty(), 0l);
    }
    
    private static Arguments twoEligibleItemsHittingMinimumQuantity() {
    	return Arguments.of("given 2 eligible items reaching minimum quantity for promotion",
        		2l, "8.50", twoTravelCardsOnly(), 2l);
    }
    
    private static Arguments twoEligibleItemsHittingMinimumQuantityAmongstOthers() {
    	return Arguments.of("given 2 eligible items amongst others reaching minimum quantity for promotion",
        		2l, "8.50", twoTravelCardsAndMixtureOfOthers(), 2l);
    }
    
    private static Arguments fiveEligibleItemsHittingMinimumQuantityAmongstOthers() {
    	return Arguments.of("given 5 eligible items reaching minimum quantity for promotion",
        		2l, "8.50", goToTownOnTheseTravelcardHolders(), 5l);
    }
    
    private static Arguments noApplicableItemsInCart() {
    	return Arguments.of("no eligible items in cart",
        		2l, "8.50", noMoreTravelcardHoldersInCart(), 0l);
    }

}
