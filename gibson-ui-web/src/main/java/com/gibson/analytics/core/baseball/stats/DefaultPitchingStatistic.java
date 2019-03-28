package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

/**
 * A default implementation of PitchingStatistic used for league avverages and testing.
 *  
 * @author leddy.eric
 *
 */
public class DefaultPitchingStatistic implements PitchingStatistics {
	
	private final double strikeOutRate;
	private final double walkRate;
	private final double onBaseAverage;
	private final double projectedInnings;
	private final Pitches pitches;
	
	public DefaultPitchingStatistic(double strikeOutRate, double walkRate, double onBaseAverage, double projectedInnings, Pitches pitches) {
		this.strikeOutRate = strikeOutRate;
		this.walkRate = walkRate;
		this.onBaseAverage = onBaseAverage;
		this.projectedInnings =  projectedInnings;
		this.pitches = pitches;
	}

	@Override
	public BigDecimal getStrikeoutRate() {
		return BigDecimal.valueOf(strikeOutRate);
	}

	@Override
	public BigDecimal getWalkRate() {
		return BigDecimal.valueOf(walkRate);
	}

	@Override
	public BigDecimal getOnBaseAverage() {
		return BigDecimal.valueOf(onBaseAverage);
	}

	@Override
	public BigDecimal getProjectedInnings() {
		return BigDecimal.valueOf(projectedInnings);
	}

	@Override
	public Pitches pitches() {
		return pitches;
	}

}
