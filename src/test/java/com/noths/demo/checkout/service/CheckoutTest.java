package com.noths.demo.checkout.service;

import static com.noths.demo.checkout.MockedShoppingCarts.aSquawkingRubberChicken;
import static com.noths.demo.checkout.MockedShoppingCarts.createPromotionalItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.noths.demo.checkout.entity.Item;
import com.noths.demo.checkout.processor.DiscountProcessor;

@TestInstance(Lifecycle.PER_CLASS)
public class CheckoutTest {

	@Mock
	private DiscountProcessor discountProcessor;

	private CheckoutImpl checkout;

	@BeforeAll
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@BeforeEach
	public void resetCheckout() {
		checkout = new CheckoutImpl(discountProcessor);
	}

	@Test
	public void givenAllPromotionalItems_whenGetCheckoutTotal_expectCalculatedTotal() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(allItemInCartOnPromotion());
		Double expectedTotal = 72.00d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void givenNoPromotionalItems_whenGetCheckoutTotal_expectCalculatedTotal() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(noItemsEligibleForPromotion());
		Double expectedTotal = 83.45d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void givenMixtureOfPromotionalItems_whenGetCheckoutTotal_thenReturnCalculatedTotal() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(mixtureOfPromotionalItems());
		Double expectedTotal = 103.96d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void givenNoItemsInCart_whenGetCheckoutTotal_thenReturnTotalOfZero() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(Collections.emptyList());
		Double expectedTotal = 0d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void given6000ItemsInCart_whenGetCheckoutTotal_thenReturnCalculatedTotal() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(bigOrderOfItems());
		Double expectedTotal = 103960.00d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void givenSmallPricedItemsInCart_whenGetCheckoutTotal_thenReturnCalculatedTotal() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(allSmallPricedItems());
		Double expectedTotal = 0.06d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void givenOneSmallPricedItemOnly_whenGetCheckoutTotal_thenReturnTotal() {
		when(discountProcessor.applyDiscount(anyList())).thenReturn(oneSmallPricedItemOnly());
		Double expectedTotal = 0.01d;

		assertEquals(expectedTotal, checkout.total());
	}

	@Test
	public void givenCheckoutIsEmpty_whenScanNewItem_thenReturnCheckoutListHasOneElement() {
		int expectedSize = 1;
		checkout.scan(aSquawkingRubberChicken());

		assertEquals(expectedSize, checkout.getCheckoutItems().size());
	}

	@Test
	public void givenCheckoutIsEmpty_whenScanTwoOfSameItem_thenReturnCheckoutListHasTwoElements() {
		int expectedSize = 2;
		checkout.scan(aSquawkingRubberChicken());
		checkout.scan(aSquawkingRubberChicken());

		assertEquals(expectedSize, checkout.getCheckoutItems().size());
	}

	@Test
	public void givenMixtureOfPromotionalItems_whenGetCheckoutSubTotal_thenReturnCalculatedTotalBasePrices() {
		for (Item item : mixtureOfPromotionalItems()) {
			checkout.scan(item);
		}
		Double expectedTotal = 108.46d;

		assertEquals(expectedTotal, checkout.subTotal());
	}

	private static List<Item> allItemInCartOnPromotion() {
		return Arrays.asList(createPromotionalItem(1l, "9.25", "8.50"), createPromotionalItem(1l, "9.25", "8.50"),
				createPromotionalItem(2l, "45.00", "40.00"), createPromotionalItem(3l, "19.95", "15.00"));
	}

	private static List<Item> noItemsEligibleForPromotion() {
		return Arrays.asList(createPromotionalItem(1l, "9.25", null), createPromotionalItem(1l, "9.25", null),
				createPromotionalItem(2l, "45.00", null), createPromotionalItem(3l, "19.95", null));
	}

	private static List<Item> mixtureOfPromotionalItems() {
		return Arrays.asList(createPromotionalItem(1l, "9.25", "8.50"), createPromotionalItem(1l, "9.25", "8.50"),
				createPromotionalItem(2l, "45.00", null), createPromotionalItem(3l, "19.95", null),
				createPromotionalItem(4l, "25.00", "22.00"), createPromotionalItem(6l, "0.01", null));
	}

	private static List<Item> oneSmallPricedItemOnly() {
		return Arrays.asList(createPromotionalItem(6l, "0.01", null));
	}

	private static List<Item> allSmallPricedItems() {
		return Arrays.asList(createPromotionalItem(6l, "0.01", null), createPromotionalItem(6l, "0.01", null),
				createPromotionalItem(6l, "0.01", null), createPromotionalItem(6l, "0.01", null),
				createPromotionalItem(6l, "0.01", null), createPromotionalItem(6l, "0.01", null));
	}

	private static List<Item> bigOrderOfItems() {
		List<Item> bigOrder = new ArrayList<>(6000);
		for (int i = 0; i < 1000; i++) {
			bigOrder.addAll(mixtureOfPromotionalItems());
		}
		return bigOrder;
	}
}
