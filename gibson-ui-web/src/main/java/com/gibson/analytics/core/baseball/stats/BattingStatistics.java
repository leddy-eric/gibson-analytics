package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

public interface BattingStatistics {

	/**
	 * Return the batting statistic for strike out rate.
	 * 
	 * @return
	 */
	public BigDecimal getStrikeOutRate();

	/**
	 * Return the batting statistic for walk rate.
	 * 
	 * @return
	 */
	public BigDecimal getWalkRate();

	/**
	 * Return the batting statistic for OBA.
	 * 
	 * @return
	 */
	public BigDecimal getOnBaseAverage();
	
	/**
	 * Return the players plate position. 
	 * 
	 * @return
	 */
	public Bats bats();
}
