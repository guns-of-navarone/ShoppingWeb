package com.navarone.rules.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.navarone.constants.ShopConstants;
import com.navarone.exception.FatalShopException;
import com.navarone.rules.Rule;
import com.navarone.rules.RuleApplication;
import com.navarone.rules.configure.Configurer;
import com.navarone.rules.interfaces.IPostRules;
import com.navarone.rules.interfaces.IPreRules;
import com.navarone.rules.interfaces.IRules;
import com.navarone.shop.Item;
import com.navarone.util.Pair;
import com.navarone.vo.ItemVO;

/**
 * @author Craig<br/>
 *         This class contains a set of rules which when applied to a checkout,
 *         will apply offers to a shopping cart to help get a total
 *
 */
public class PricingRules {

	private static final Logger LOGGER = Logger.getLogger("PricingRules.java");

	private Map<Rule, IRules> rules = new HashMap<Rule, IRules>();

	private List<IPostRules> listPost = new ArrayList<IPostRules>();

	private Map<String, ItemVO> catalogue = new HashMap<String, ItemVO>();

	public PricingRules(String pathToRuleConfig, String pathToCatalogueConfig) throws FatalShopException {
		init(pathToRuleConfig, pathToCatalogueConfig);
	}

	private void init(String pathToRuleConfig, String pathToCatalogueConfig) throws FatalShopException {
		Configurer configurer = Configurer.getInstance();

		// configure catalogue, mandatory
		try {
			catalogue = configurer.initCatalogue(pathToCatalogueConfig, catalogue);
		} catch (Exception ex) {
			// ex.printStackTrace();
			// LOGGER.log(Level.SEVERE, ShopConstants.FATAL_CATALOGUE_EXCEPTION, ex);
			throw new FatalShopException(ShopConstants.FATAL_CATALOGUE_EXCEPTION);
		}

		// configure rules, optional
		try {
			Pair<List<IPostRules>, Map<Rule, IRules>> pair = new Pair<List<IPostRules>, Map<Rule, IRules>>(listPost,
					rules);
			pair = configurer.initRules(pathToRuleConfig, pair);
		} catch (Exception ex) {
			// ex.printStackTrace();
			LOGGER.log(Level.WARNING, "Error - Rules not found. Default price will be applied.", ex);
		}
	}

	/**
	 * Calculates the price given the input and the count
	 * 
	 * @param item
	 * @param integer
	 * @return
	 */
	public BigDecimal determinePrice(Item item, Integer count) {
		BigDecimal result = BigDecimal.ZERO;
		boolean appliedRule = false;

		for (IRules rule : rules.values()) {
			if (rule instanceof IPreRules) {
				Pair<RuleApplication, BigDecimal> pair = ((IPreRules) rule).applyRule(item, count, catalogue);
				if (RuleApplication.APPLIED == pair.getKey()) {
					appliedRule = true;
					result = result.add(pair.getValue());
				}
			} else if (rule instanceof IPostRules) {
				Pair<RuleApplication, BigDecimal> pair = rule.defaultPrice(item, count, catalogue);
				if (RuleApplication.APPLIED == pair.getKey()) {
					appliedRule = true;
					result = result.add(pair.getValue());
				}
			}
		}
		if (appliedRule)
			return result;
		else
			return this.defaultPrice(item, count);
	}

	private BigDecimal defaultPrice(Item item, Integer count) {
		if (catalogue.containsKey(item.getSku()))
			return catalogue.get(item.getSku()).getPrice().multiply(new BigDecimal(count));
		else
			return BigDecimal.ZERO;
	}

	/**
	 * Apply the freebies by allowing a discount on the final value
	 * 
	 * @param result
	 * @return
	 */
	public BigDecimal applyPostRules(BigDecimal result) {
		if (!listPost.isEmpty()) {
			for (IPostRules postRule : listPost) {
				Pair<RuleApplication, BigDecimal> pair = postRule.applyRule(catalogue);
				if (RuleApplication.APPLIED == pair.getKey())
					result = result.subtract(pair.getValue());
			}
		}
		this.clearState();
		return result;
	}

	/**
	 * Clears transactional state
	 */
	private void clearState() {
		for (IRules rule : rules.values()) {
			rule.clearState();
		}

	}

	public void register(Item item, int count) {
		for (IPostRules post : listPost) {
			post.registerItem(item, count);
		}
	}
}
