package com.navarone.rules.interfaces;

import java.math.BigDecimal;
import java.util.Map;

import com.navarone.rules.RuleApplication;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         These rules are applied after the final amount is arrived at
 *
 */
public interface IPostRules extends IRules {

	public Pair<RuleApplication, BigDecimal> applyRule(Map<String, ItemVO> catalogue);

	public void registerItem(Item item, Integer count);
}
