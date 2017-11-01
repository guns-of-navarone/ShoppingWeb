package com.navarone.rules;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.navarone.constants.ShopConstants;
import com.navarone.rules.interfaces.IPostRules;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.FreebieVO;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         This rule allows you to take N free items of X sku for M paid items
 *         of Y sku
 *
 */
public class FreebieRule implements IPostRules {

	private Map<String, FreebieVO> freebies = new HashMap<String, FreebieVO>();

	private Map<String, Integer> cartTotals = new HashMap<String, Integer>();

	public FreebieRule() {

	}

	@Override
	public void addCondition(String... string) throws Exception {
		if (validate(string)) {
			String thisSku = string[0];

			// prevent circular references due to configuration errors
			for (String sku : freebies.keySet()) {
				if (freebies.get(sku).getFreeSku().equals(thisSku)) {
					return;
				}
			}
			if (!freebies.containsKey(thisSku))
				freebies.put(thisSku,
						new FreebieVO(thisSku, Integer.parseInt(string[1]), string[2], Integer.parseInt(string[3])));
		}
	}

	/**
	 * This rule is applied in case of freebies. The format from config file is
	 * <br/>
	 * FREEBIE=mbp,1,vga,1 which means 1 vga free for 1 mbp purchased<br/>
	 * FREEBIE=mbp,2,vga,1 which means 1 vga free for 2 mbp purchased<br/>
	 * 
	 * @param item
	 * @param count
	 * @param catalogue
	 * @return
	 */
	@Override
	public Pair<RuleApplication, BigDecimal> applyRule(Map<String, ItemVO> catalogue) {
		BigDecimal result = BigDecimal.ZERO;
		for (String freebieName : freebies.keySet()) {
			FreebieVO freebie = freebies.get(freebieName);
			if (cartTotals.containsKey(freebie.getBuySku()) && catalogue.containsKey(freebie.getBuySku())) {
				int boughtQty = cartTotals.get(freebie.getBuySku());
				if (boughtQty > 0) {
					int buyFactor = boughtQty / freebie.getBuyQty();
					int freeQty = buyFactor * freebie.getFreeSkuQty();

					// upto 'freeQty' of freeSku are free
					if (freeQty > 0) {
						int purchasedFreeSkuQty = cartTotals.get(freebie.getFreeSku());
						if (freeQty >= purchasedFreeSkuQty) {
							result = result.add(catalogue.get(freebie.getFreeSku()).getPrice()
									.multiply(new BigDecimal(purchasedFreeSkuQty)));
						} else {
							result = result.add(
									catalogue.get(freebie.getFreeSku()).getPrice().multiply(new BigDecimal(freeQty)));
						}
						result = result.setScale(ShopConstants.DEFAULT_SCALE, ShopConstants.DEFAULT_ROUNDING);
						return new Pair<RuleApplication, BigDecimal>(RuleApplication.APPLIED, result);
					}
				}
			}
		}
		return new Pair<RuleApplication, BigDecimal>(RuleApplication.UNAPPLIED, result);
	}

	/**
	 * This method allows the rule to keep track of quantities so that the discount
	 * (free value) can be applied at the end. It should be called once per item.
	 * 
	 * @param item
	 * @param count
	 */
	@Override
	public void registerItem(Item item, Integer count) {
		cartTotals.put(item.getSku(), count);
	}

	@Override
	public Pair<RuleApplication, BigDecimal> defaultPrice(Item item, int count, Map<String, ItemVO> catalogue) {
		if (freebies.containsKey(item.getSku())) {
			BigDecimal result = catalogue.get(item.getSku()).getPrice().multiply(new BigDecimal(count));
			result = result.setScale(ShopConstants.DEFAULT_SCALE, ShopConstants.DEFAULT_ROUNDING);
			return new Pair<RuleApplication, BigDecimal>(RuleApplication.APPLIED, result);
		} else {
			return new Pair<RuleApplication, BigDecimal>(RuleApplication.UNAPPLIED, BigDecimal.ZERO);
		}
	}

	@Override
	public void clearState() {
		cartTotals.clear();
	}

	@Override
	public boolean validate(String... arguments) {
		try {
			if (arguments.length == 4 && arguments[0] != null && !arguments[0].isEmpty() && arguments[2] != null
					&& !arguments[2].isEmpty() && new Integer(arguments[1]) instanceof Integer
					&& new Integer(arguments[3]) instanceof Integer) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	@Override
	public String[] retrieveCondition(String sku) throws Exception {
		if (sku != null && freebies.containsKey(sku)) {
			String[] res = { freebies.get(sku).getBuySku(), String.valueOf(freebies.get(sku).getBuyQty()),
					freebies.get(sku).getFreeSku(), String.valueOf(freebies.get(sku).getFreeSkuQty()) };
			return res;
		}
		return null;
	}

	@Override
	public boolean deleteCondition(String sku) throws Exception {
		if (sku != null && freebies.containsKey(sku)) {
			freebies.remove(sku);
			return true;
		} else {
			return false;
		}
	}
}
