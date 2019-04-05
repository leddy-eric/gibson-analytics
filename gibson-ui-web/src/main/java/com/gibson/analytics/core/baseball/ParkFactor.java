package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;

import com.gibson.analytics.enums.MlbTeamLookup;

public class ParkFactor {
	private final BigDecimal factor;
	private final MlbTeamLookup team;
	
	/**
	 * Create the park factor.
	 * 
	 * @param factor
	 * @param team
	 */
	public ParkFactor(double factor, MlbTeamLookup team) {
		this(BigDecimal.valueOf(factor), team);
	}
	
	/**
	 * Create the park factor.
	 * 
	 * @param factor
	 * @param team
	 */
	public ParkFactor(BigDecimal factor, MlbTeamLookup team) {
		this.factor = factor;
		this.team = team;
	}

	/**
	 * @return the factor
	 */
	public BigDecimal getFactor() {
		return factor;
	}

	/**
	 * @return the team
	 */
	public MlbTeamLookup getTeam() {
		return team;
	}

}
