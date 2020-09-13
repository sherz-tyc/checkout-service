package com.noths.demo.checkout.service;

import java.util.List;

import com.noths.demo.checkout.entity.Item;

public interface Checkout {
	
	public void scan(Item item);
	public Double total();
	public List<Item> getCheckoutItems();
}
