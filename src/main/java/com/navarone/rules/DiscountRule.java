package com.navarone.rules;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.navarone.constants.ShopConstants;
import com.navarone.rules.interfaces.IPreRules;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         This rule allows you to take a discount (amount, not percentage) from
 *         the sale price
 *
 */
public class DiscountRule implements IPreRules {

	private Map<String, Pair<Integer, BigDecimal>> discounted = new HashMap<String, Pair<Integer, BigDecimal>>();

	public DiscountRule() {

	}

	@Override
	public void addCondition(String... string) throws Exception {
		if (validate(string)) {
			discounted.put(string[0], new Pair<Integer, BigDecimal>(Integer.parseInt(string[2]),
					BigDecimal.valueOf(Double.valueOf(string[1]))));
		}
	}

	@Override
	public Pair<RuleApplication, BigDecimal> applyRule(Item item, Integer count, Map<String, ItemVO> catalogue) {
		if (catalogue.containsKey(item.getSku()) && discounted.containsKey(item.getSku())
				&& count >= discounted.get(item.getSku()).getKey()) {
			BigDecimal result = catalogue.get(item.getSku()).getPrice()
					.subtract(discounted.get(item.getSku()).getValue()).multiply(new BigDecimal(count));
			result = result.setScale(ShopConstants.DEFAULT_SCALE, ShopConstants.DEFAULT_ROUNDING);
			return new Pair<RuleApplication, BigDecimal>(RuleApplication.APPLIED, result);
		} else {
			return new Pair<RuleApplication, BigDecimal>(RuleApplication.UNAPPLIED, BigDecimal.ZERO);
		}

	}

	@Override
	public Pair<RuleApplication, BigDecimal> defaultPrice(Item item, int count, Map<String, ItemVO> catalogue) {
		BigDecimal result = BigDecimal.ZERO;
		if (catalogue.containsKey(item.getSku()))
			result = catalogue.get(item.getSku()).getPrice().multiply(new BigDecimal(count));
		result = result.setScale(ShopConstants.DEFAULT_SCALE, ShopConstants.DEFAULT_ROUNDING);
		return new Pair<RuleApplication, BigDecimal>(RuleApplication.UNAPPLIED, result);
	}

	@Override
	public void clearState() {

	}

	@Override
	public boolean validate(String... arguments) {
		try {
			if (arguments.length == 3 && arguments[0] != null && !arguments[0].isEmpty() && arguments[2] != null
					&& new Integer(arguments[2]) instanceof Integer && arguments[1] != null
					&& BigDecimal.valueOf(Double.valueOf(arguments[1])) instanceof BigDecimal) {
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
		if (sku != null && discounted.containsKey(sku)) {
			String[] res = { sku, String.valueOf(discounted.get(sku).getValue()),
					String.valueOf(discounted.get(sku).getKey()) };
			return res;
		}
		return null;
	}

	@Override
	public boolean deleteCondition(String sku) throws Exception {
		if (sku != null && discounted.containsKey(sku)) {
			discounted.remove(sku);
			return true;
		} else {
			return false;
		}
	}
}
