package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

public interface PitchingStatistics {

	/**
	 * Return the pitching statistic for strikeout rate.
	 * 
	 * @return
	 */
	public BigDecimal getStrikeoutRate();

	/**
	 * Return the pitching statistic for walk rate.
	 * 
	 * @return
	 */
	public BigDecimal getWalkRate();

	/**
	 * Return the pitching statistic for OBA.
	 * 
	 * @return
	 */
	public BigDecimal getOnBaseAverage();
	
	/**
	 * Return the projected innings for this opponent.
	 * 
	 * @return
	 */
	public BigDecimal getProjectedInnings(); 
	
	/**
	 * Return the pitchers throwing.
	 * 
	 * @return
	 */
	public Pitches pitches();
}
