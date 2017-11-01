package com.navarone.constants;

import java.math.RoundingMode;

/**
 * @author Craig<br/>
 *         Stores a set of constants which may be referred in the application
 */
public interface ShopConstants {

	public static final String FATAL_CATALOGUE_EXCEPTION = "Fatal error - Catalogue not found. Exiting.";
	public static final String HASH = "#";
	public static final String COMMA = ",";
	public static final String BUY_X_PAY_Y = "BUY_X_PAY_Y";
	public static final String DISCOUNTED = "DISCOUNTED";
	public static final String FREEBIE = "FREEBIE";
	public static final String CATALOG_FILE = "catalogue.properties";
	public static final String RULES_FILE = "rules.properties";
	public static final String EQUALS = "=";
	public static final int DEFAULT_SCALE = 2;
	public static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;
	
}
