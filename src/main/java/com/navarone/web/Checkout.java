package com.navarone.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.navarone.rules.pricing.PricingRules;
import com.navarone.shop.Item;

/**
 * @author Craig<br/>
 *         The checkout, which scans and totals
 *
 */
public class Checkout {

	private static final Logger LOGGER = Logger.getLogger("Checkout.java");

	private PricingRules pricingRules;

	private Map<Item, Integer> shoppingCart = new HashMap<Item, Integer>();

	public Checkout(PricingRules rules) {
		this.pricingRules = rules;
	}

	public void scan(Item item) {
		if (shoppingCart.containsKey(item)) {
			shoppingCart.put(item, shoppingCart.get(item) + 1);
		} else {
			shoppingCart.put(item, 1);
		}
	}

	public BigDecimal total() {
		BigDecimal result = BigDecimal.ZERO;
		for (Item item : shoppingCart.keySet()) {
			pricingRules.register(item, shoppingCart.get(item));
			result = result.add(pricingRules.determinePrice(item, shoppingCart.get(item)));
		}

		// freebies applied as a subtraction at the end
		result = pricingRules.applyPostRules(result);
		LOGGER.log(Level.INFO, "Your final total: " + result);
		return result;
	}
}
