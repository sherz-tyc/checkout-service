package com.noths.demo.checkout.processor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyList;

import static com.noths.demo.checkout.MockedShoppingCarts.createPromotionalItem;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.promotion.DiscountBasedOnSpending;
import com.noths.demo.checkout.promotion.MultiBuyDiscount;

/**
 * Primary business logic of this processor is to ensure discounts
 * applicable to total cost of items is applied at last, other
 * product-based discounts take priority.
 */
@TestInstance(Lifecycle.PER_CLASS)
public class DefaultDiscountProcessorTest {
	
	@Mock
	MultiBuyDiscount mockedMultiBuy;
	
	@Mock
	DiscountBasedOnSpending mockedDiscountBasedOnSpending;
	
	DefaultDiscountProcessor target;
	
	@BeforeAll
	public void setUpMockito() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@BeforeEach
	public void setUpPromotions() {
		target = new DefaultDiscountProcessor();
	}
	
	@Test
	public void discountBasedOnSpendingWillBeAppliedLast() {
		target.addPromotion(mockedMultiBuy);
		target.addPromotion(mockedDiscountBasedOnSpending);
		
		BigDecimal expectedItemPrice = new BigDecimal("54.00");
		List<Item> items = Arrays.asList(createPromotionalItem(8l, "65.00", null));
		
		when(mockedMultiBuy.apply(anyList())).thenReturn(
				Arrays.asList(createPromotionalItem(8l, "65.00", "60.00")));
		when(mockedDiscountBasedOnSpending.apply(anyList())).thenReturn(
				Arrays.asList(createPromotionalItem(8l, "65.00", "54.00")));
		
		List<Item> actualItemList = target.applyDiscount(items);
		
		assertEquals(expectedItemPrice, actualItemList.get(0).getPromotionalPrice());
	}
	
	@Test
	public void whenNoPromotionIsAppliedExpectNoPromotionalPrice() {
		
		BigDecimal expectedItemPrice = null;
		List<Item> items = Arrays.asList(createPromotionalItem(8l, "65.00", null));
		
		List<Item> actualItemList = target.applyDiscount(items);
		
		assertEquals(expectedItemPrice, actualItemList.get(0).getPromotionalPrice());
	}
	
	@Test
	public void promotionIsSucesssfullyAdded() {
		int expectedNumOfItems = 1;
		
		target.addPromotion(new MultiBuyDiscount(1l, 2l, BigDecimal.ONE));
		
		assertEquals(expectedNumOfItems, target.getPromotions().size());
	}
	
	@Test
	public void promotionIsSucesssfullyRemoved() {
		target.addPromotion(mockedMultiBuy);
		target.addPromotion(mockedDiscountBasedOnSpending);
		
		int expectedNumOfItems = 1;
		target.removePromotion(mockedDiscountBasedOnSpending);
		
		assertEquals(expectedNumOfItems, target.getPromotions().size());
	}
}
