package com.navarone.rules.interfaces;

import java.math.BigDecimal;
import java.util.Map;

import com.navarone.rules.RuleApplication;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         The rule interface, which specifies a default price method, a method
 *         to add a condition, to validate it and a method to clear transaction
 *         state
 */
public interface IRules {

	public void addCondition(String... arguments) throws Exception;
	
	public String[] retrieveCondition(String sku) throws Exception;
	
	public boolean deleteCondition(String sku) throws Exception;

	public Pair<RuleApplication, BigDecimal> defaultPrice(Item item, int count, Map<String, ItemVO> catalogue);

	public boolean validate(String... arguments);

	public void clearState();
}
