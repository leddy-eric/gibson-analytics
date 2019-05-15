package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

import com.gibson.analytics.enums.MlbTeamLookup;

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
	private final double factor;
	private final double rank;
	private final Pitches pitches;
	private final MlbTeamLookup team;
	
	public DefaultPitchingStatistic(double strikeOutRate, double walkRate, double onBaseAverage, double projectedInnings, double factor, double rank, Pitches pitches, MlbTeamLookup team) {
		this.strikeOutRate = strikeOutRate;
		this.walkRate = walkRate;
		this.onBaseAverage = onBaseAverage;
		this.projectedInnings =  projectedInnings;
		this.factor = factor;
		this.rank = rank;
		this.pitches = pitches;
		this.team = team;
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
	public BigDecimal getFactor() {
		return BigDecimal.valueOf(factor);
	}
	
	@Override
	public BigDecimal getRank() {
		return BigDecimal.valueOf(rank);
	}

	@Override
	public Pitches pitches() {
		return pitches;
	}

	@Override
	public MlbTeamLookup getTeam() {
		return team;
	}

}
