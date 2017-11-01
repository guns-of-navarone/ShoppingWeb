package com.navarone.rules.interfaces;

import java.math.BigDecimal;
import java.util.Map;

import com.navarone.rules.RuleApplication;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         These rules are applied during the calculation
 *
 */
public interface IPreRules extends IRules {

	public Pair<RuleApplication, BigDecimal> applyRule(Item item, Integer count, Map<String, ItemVO> catalogue);
}
