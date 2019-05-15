package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

/**
 * Constants for league averages.
 * 
 * @author leddy.eric
 *
 */
public interface LeagueAverages {
	
	public static final double STRIKE_OUT_RATE = .227;
	public static final double WALK_RATE = .098;
	public static final double WEIGHTED_OBA = .378;
	public static final double EXPECTED_INNINGS = 5.4;
	public static final double FACTOR = 1;
	public static final double RANK = .5;
	
	// Big Decimals
	public static final BigDecimal BIG_DECIMAL_STRIKE_OUT_RATE = BigDecimal.valueOf(STRIKE_OUT_RATE);
	public static final BigDecimal BIG_DECIMAL_WALK_RATE = BigDecimal.valueOf(WALK_RATE);
	public static final BigDecimal BIG_DECIMAL_WEIGHTED_OBA = BigDecimal.valueOf(WEIGHTED_OBA);
	public static final BigDecimal BIG_DECIMAL_EXPECTED_INNINGS = BigDecimal.valueOf(EXPECTED_INNINGS);
	public static final BigDecimal BIG_DECIMAL_FACTOR = BigDecimal.valueOf(FACTOR);
	public static final BigDecimal BIG_DECIMAL_RANK = BigDecimal.valueOf(RANK);
	

}
