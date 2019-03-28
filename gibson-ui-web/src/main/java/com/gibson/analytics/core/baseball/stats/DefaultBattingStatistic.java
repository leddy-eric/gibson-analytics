package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

/**
 * Default implementation of BattingStatistics to be used in testing.
 * 
 * @author leddy.eric
 *
 */
public class DefaultBattingStatistic implements BattingStatistics {
	
	private final double strikeOutRate;
	private final double walkRate;
	private final double onBaseAverage;
	private final Bats bats;
	
	public DefaultBattingStatistic(double strikeOutRate, double walkRate, double onBaseAverage, Bats bats) {
		this.strikeOutRate = strikeOutRate;
		this.walkRate = walkRate;
		this.onBaseAverage = onBaseAverage;
		this.bats = bats;
	}

	@Override
	public BigDecimal getStrikeOutRate() {
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
	public Bats bats() {
		return bats;
	}

}
