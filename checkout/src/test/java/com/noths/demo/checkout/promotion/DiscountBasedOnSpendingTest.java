package com.noths.demo.checkout.promotion;

import static com.noths.demo.checkout.MockedShoppingCarts.bigSpendingSpreeOnCuffLinks;
import static com.noths.demo.checkout.MockedShoppingCarts.mixtureOfItemsTotallingExactlySixty;
import static com.noths.demo.checkout.MockedShoppingCarts.travelcardHolderAndTshirtTotallingLessThanSixty;
import static com.noths.demo.checkout.MockedShoppingCarts.mixtureOfItemsTotallingMoreThanSixty;
import static com.noths.demo.checkout.MockedShoppingCarts.mixtureOfItemsTotallingOnePenceShortOfSixty;
import static com.noths.demo.checkout.MockedShoppingCarts.cartTotallingOnePence;
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
import com.noths.demo.checkout.utils.CheckoutUtils;

public class DiscountBasedOnSpendingTest {
	
	@DisplayName("A Percentage Discount is applied to sub-total if achieved minimum spend threshold...")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void promotionAppliedAccordingly(String description, String minSpending, double discountPercentage, 
    		List<Item> items, String expectedTotalAfterDiscountStr) {
		
		BigDecimal expectedTotalAfterDiscount = new BigDecimal(expectedTotalAfterDiscountStr);
		// Given pre conditions as input arguments
		final DiscountBasedOnSpending target = 
				new DiscountBasedOnSpending(new BigDecimal(minSpending), discountPercentage);
		
		// When attempt to apply promotion
		items = target.apply(items);
		
		// Then expect promotional price of only eligible items are applied
		BigDecimal actualTotalAfterDiscount = CheckoutUtils.calculateCartTotal(items);
        
		assertEquals(expectedTotalAfterDiscount.stripTrailingZeros(), actualTotalAfterDiscount.stripTrailingZeros());
    }

    static Stream<Arguments> promotionAppliedAccordingly() {
        return Stream.of(
        		noItems(),
        		itemsTotallingMoreThanSixty(),
        		itemsTotallingLessThanSixty(),
        		itemsTotallingExactlySixty(),
        		itemsTotallingOnePenceShortOfSixty(),
        		cartTotallingOverFiveThousand(),
        		cartTotallingOnePenceOnly()
        );
    }
    
    private static Arguments noItems() {
        return Arguments.of("given no items in cart", "60", "10", Collections.emptyList(), "0");
    }

    private static Arguments itemsTotallingMoreThanSixty() {
        return Arguments.of("given cart total is more than minimum spend to qualify for 10% promotion",
        		"60", "10", mixtureOfItemsTotallingMoreThanSixty(), "75.105");
    }
    
    private static Arguments itemsTotallingLessThanSixty() {
    	return Arguments.of("given cart total is less than minimum spend and should not qualify for promotion",
    			"60", "10", travelcardHolderAndTshirtTotallingLessThanSixty(), "38.45");
    }
    
    private static Arguments itemsTotallingExactlySixty() {
    	return Arguments.of("given cart total is same as minimum spend and should qualify for 10% promotion",
    			"60", "10", mixtureOfItemsTotallingExactlySixty(), "54.00");
    }
    
    private static Arguments itemsTotallingOnePenceShortOfSixty() {
    	return Arguments.of("given cart total is 59.99 - should not qualify for promotion",
    			"60", "10", mixtureOfItemsTotallingOnePenceShortOfSixty(), "59.99");
    }
    private static Arguments cartTotallingOverFiveThousand() {
    	return Arguments.of("given cart total is 5400.00 - expect 10% promotion",
    			"60", "10", bigSpendingSpreeOnCuffLinks(), "4860.00");
    }
    
    private static Arguments cartTotallingOnePenceOnly() {
    	return Arguments.of("given cart total is 0.01 - should not qualify for promotion",
    			"60", "10", cartTotallingOnePence(), "0.01");
    }

}
