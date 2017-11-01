package com.navarone.web;

import java.io.File;

import com.navarone.constants.ShopConstants;
import com.navarone.exception.FatalShopException;
import com.navarone.rules.pricing.PricingRules;
import com.navarone.shop.Item;

/**
 * @author Craig<br/>
 *         A test class provided to do checkouts
 *
 */
public class Shopper {

	/**
	 * Usage Shopper /directory/of/config/files.<br/>
	 * 
	 * We can reuse the PricingRules, since it is initialized once for the
	 * application
	 * 
	 * @param args
	 * @throws FatalShopException
	 */
	public static void main(String[] args) throws FatalShopException {
		if (args.length != 1)
			System.err.println("Usage Shopper /directory/of/config/files.");
		PricingRules rules = new PricingRules(args[0] + File.separator + ShopConstants.RULES_FILE,
				args[0] + File.separator + ShopConstants.CATALOG_FILE);
		doFirstCheckout(rules);
		doSecondCheckout(rules);
		doThirdCheckout(rules);

		// Changing the order of the scans shouldn't change the totals, here are the
		// re-ordered ones
		doFourthCheckout(rules);
		doFifthCheckout(rules);
		doSixthCheckout(rules);
	}

	private static void doFirstCheckout(PricingRules rules) {
		Checkout checkout = new Checkout(rules);
		Item item = new Item("atv");
		checkout.scan(item);
		checkout.scan(item);
		checkout.scan(item);

		item = new Item("vga");
		checkout.scan(item);

		checkout.total();
	}

	private static void doSecondCheckout(PricingRules rules) {
		Checkout checkout = new Checkout(rules);
		Item item = new Item("atv");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("atv");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		checkout.total();

	}

	private static void doThirdCheckout(PricingRules rules) {
		Checkout checkout = new Checkout(rules);
		Item item = new Item("mbp");
		checkout.scan(item);

		item = new Item("vga");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		checkout.total();

	}

	private static void doFourthCheckout(PricingRules rules) {
		Checkout checkout = new Checkout(rules);
		Item item = new Item("vga");
		checkout.scan(item);

		item = new Item("atv");
		checkout.scan(item);
		checkout.scan(item);
		checkout.scan(item);

		checkout.total();

	}

	private static void doFifthCheckout(PricingRules rules) {
		Checkout checkout = new Checkout(rules);
		Item item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("atv");
		checkout.scan(item);

		item = new Item("atv");
		checkout.scan(item);

		checkout.total();
	}

	private static void doSixthCheckout(PricingRules rules) {
		Checkout checkout = new Checkout(rules);
		Item item = new Item("mbp");
		checkout.scan(item);

		item = new Item("ipd");
		checkout.scan(item);

		item = new Item("vga");
		checkout.scan(item);

		checkout.total();
	}

}
