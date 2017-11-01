package com.navarone.rules.configure;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.navarone.constants.ShopConstants;
import com.navarone.rules.BuyPayRule;
import com.navarone.rules.DiscountRule;
import com.navarone.rules.FreebieRule;
import com.navarone.rules.Rule;
import com.navarone.rules.interfaces.IPostRules;
import com.navarone.rules.interfaces.IRules;
import com.navarone.util.Pair;
import com.navarone.vo.ItemVO;

/**
 * @author Craig</br>
 *         Responsible for configuring the rules for this instance
 */
public class Configurer {

	private static final Logger LOGGER = Logger.getLogger("Configurer.java");

	private static Configurer self = null;

	private Configurer() {

	}

	public static Configurer getInstance() {
		if (self == null) {
			self = new Configurer();
		}
		return self;
	}

	/**
	 * Scans rules with formats like the below
	 * 
	 * BUY_X_PAY_Y=atv,3,2
	 * 
	 * DISCOUNTED=ipd,50
	 * 
	 * FREEBIE=mbp,1,vga,1
	 */
	public Pair<List<IPostRules>, Map<Rule, IRules>> initRules(String path,
			Pair<List<IPostRules>, Map<Rule, IRules>> pair) {

		List<IPostRules> list = pair.getKey();
		Map<Rule, IRules> rules = pair.getValue();
		String line = null;
		String[] splitLine = null;
		String[] arguments = null;
		Scanner scan = null;
		try {
			scan = new Scanner(new File(path));

			while (scan.hasNextLine()) {
				line = scan.nextLine().trim();
				if (!(line.isEmpty() || line.startsWith(ShopConstants.HASH)) && line.contains(ShopConstants.EQUALS)) {
					splitLine = line.split(ShopConstants.EQUALS);

					if (splitLine != null && splitLine.length == 2) {

						arguments = splitLine[1].split(ShopConstants.COMMA);
						switch (splitLine[0]) {

						case ShopConstants.BUY_X_PAY_Y: {

							try {
								if (arguments.length == 3) {
									BuyPayRule temp = (BuyPayRule) rules.get(Rule.BUY_X_PAY_Y);
									if (temp == null) {
										temp = new BuyPayRule();
										rules.put(Rule.BUY_X_PAY_Y, temp);
									}
									temp.addCondition(arguments);
								}
							} catch (Exception ex) {
								LOGGER.log(Level.WARNING, "Exception for rule " + Rule.BUY_X_PAY_Y.toString(), ex);
							}

							break;
						}
						case ShopConstants.DISCOUNTED: {

							try {
								if (arguments.length == 3) {
									DiscountRule temp = (DiscountRule) rules.get(Rule.DISCOUNTED);
									if (temp == null) {
										temp = new DiscountRule();
										rules.put(Rule.DISCOUNTED, temp);
									}
									temp.addCondition(arguments);
								}
							} catch (Exception ex) {
								LOGGER.log(Level.WARNING, "Exception for rule " + Rule.DISCOUNTED, ex);
							}

							break;
						}
						case ShopConstants.FREEBIE: {

							try {
								if (arguments.length == 4) {
									FreebieRule temp = (FreebieRule) rules.get(Rule.FREEBIE);
									if (temp == null) {
										temp = new FreebieRule();
										rules.put(Rule.FREEBIE, temp);
										list.add(temp);
									}
									temp.addCondition(arguments);
								}
							} catch (Exception ex) {
								LOGGER.log(Level.WARNING, "Exception for rule " + Rule.FREEBIE, ex);
							}

							break;
						}
						}
					}
				}

				splitLine = null;
			}

		} catch (Exception ex) {
			LOGGER.info("Suppressing [" + ex.getMessage() + "] rules are not mandatory.");
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return pair;
	}

	/**
	 * If any sku is repeated in the file, the first is configured.
	 * 
	 * ex. ipd,Super iPad,549.99
	 */
	public Map<String, ItemVO> initCatalogue(String path, Map<String, ItemVO> catalogue) throws FileNotFoundException {
		String line = null;
		Scanner scan = null;
		String[] splitLine = null;
		scan = new Scanner(new File(path));
		ItemVO temp = null;
		try {
			while (scan.hasNextLine()) {
				line = scan.nextLine().trim();
				if (!(line.isEmpty() || line.startsWith(ShopConstants.HASH)) && line.contains(",")) {
					splitLine = line.split(ShopConstants.COMMA);
					if (splitLine.length == 3) {
						for (String arg : splitLine) {
							arg = trim(arg);
						}
						temp = new ItemVO(splitLine[0], splitLine[1], BigDecimal.valueOf(Double.valueOf(splitLine[2])));
						catalogue.put(splitLine[0], temp);
					}
				}
			}
		} finally {
			if (scan != null) {
				scan.close();
			}

		}
		return catalogue;
	}

	private String trim(String arg) {
		if (arg != null) {
			arg = arg.trim();
		}
		return arg;
	}
}
