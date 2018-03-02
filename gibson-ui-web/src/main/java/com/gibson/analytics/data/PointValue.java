package com.gibson.analytics.data;

import java.math.BigDecimal;

/**
 * Consider this for a repo at some point 
 * @author leddy.eric
 *
 */
public class PointValue {


	private BigDecimal spread;
	private BigDecimal value;
	private BigDecimal winPercentage;
	
	/**
	 * 
	 * @param spread
	 * @param value
	 * @param winPercentage
	 * @throws NumberFormatException
	 */
	public PointValue(String spread, String value, String winPercentage) throws NumberFormatException {
		this(new BigDecimal(spread), new BigDecimal(value), new BigDecimal(winPercentage));
	}
	
	/**
	 * 
	 * @param spread
	 * @param value
	 * @param winPercentage
	 */
	protected PointValue(BigDecimal spread, BigDecimal value, BigDecimal winPercentage) {
		this.spread = spread;
		this.value = value;
		this.winPercentage = winPercentage;
	}
	
	/**
	 * @return the spread
	 */
	public BigDecimal getSpread() {
		return spread;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @return the winPercentage
	 */
	public BigDecimal getWinPercentage() {
		return winPercentage;
	}

}
