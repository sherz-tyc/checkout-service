package com.noths.demo.checkout.promotion;

import java.util.List;

import com.noths.demo.checkout.entity.Item;

public interface Promotion {
	
	public boolean isApplicable(List<Item> items);
	
	public List<Item> apply(List<Item> items);

}
