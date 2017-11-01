package com.navarone.rules;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.navarone.constants.ShopConstants;
import com.navarone.rules.interfaces.IPreRules;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.BuyPayVO;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         This rule allows you to pay for fewer items than you buy
 *
 */
public class BuyPayRule implements IPreRules {

	private Map<String, BuyPayVO> buyXPayY = new HashMap<String, BuyPayVO>();

	public BuyPayRule() {

	}

	@Override
	public void addCondition(String... string) throws Exception {
		if (validate(string))
			buyXPayY.put(string[0], new BuyPayVO(Integer.parseInt(string[1]), Integer.parseInt(string[2])));
	}

	@Override
	public Pair<RuleApplication, BigDecimal> applyRule(Item item, Integer count, Map<String, ItemVO> catalogue) {
		BigDecimal temp = BigDecimal.ZERO;
		if (buyXPayY.containsKey(item.getSku()) && catalogue.containsKey(item.getSku())) {
			BuyPayVO offer = buyXPayY.get(item.getSku());
			if (count >= offer.getBuy()) {
				int offerQty = count / offer.getBuy();
				int regularQty = count % offer.getBuy();
				temp = catalogue.get(item.getSku()).getPrice().multiply(new BigDecimal(offerQty * offer.getPay()));
				temp = temp.add(catalogue.get(item.getSku()).getPrice().multiply(new BigDecimal(regularQty)));
				temp = temp.setScale(ShopConstants.DEFAULT_SCALE, ShopConstants.DEFAULT_ROUNDING);
				return new Pair<RuleApplication, BigDecimal>(RuleApplication.APPLIED, temp);
			}
		}
		return new Pair<RuleApplication, BigDecimal>(RuleApplication.UNAPPLIED, temp);
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
	public boolean validate(String... args) {
		try {
			if (args.length == 3 && args[0] != null && !args[0].isEmpty()
					&& (Integer.parseInt(args[1])) > (Integer.parseInt(args[2]))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	@Override
	public String[] retrieveCondition(String sku) throws Exception {
		if (sku != null && buyXPayY.containsKey(sku)) {
			String[] res = { sku, String.valueOf(buyXPayY.get(sku).getBuy()),
					String.valueOf(buyXPayY.get(sku).getPay()) };
			return res;
		}
		return null;
	}

	@Override
	public boolean deleteCondition(String sku) throws Exception {
		if (sku != null && buyXPayY.containsKey(sku)) {
			buyXPayY.remove(sku);
			return true;
		} else {
			return false;
		}
	}
}
