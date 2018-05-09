package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;

import com.gibson.analytics.init.CsvPitcherConstants;


/**
 * Wrapper class for MLB Pitching statistics
 * 
 * @author leddy.eric
 *
 */
public class MlbPitcher {
	
	private BigDecimal onBasePercentage = BigDecimal.valueOf(.33);
	private BigDecimal slugging = BigDecimal.valueOf(.45);
	private BigDecimal groundBalls = BigDecimal.valueOf(.4);
	private BigDecimal rank = BigDecimal.valueOf(.5);
	private BigDecimal factor = BigDecimal.valueOf(.1);
	
	/**
	 * Create an MLB pitcher wrapper based on the defaults.
	 */
	public MlbPitcher() {
		// Use defaults
	}
	
	/**
	 * Create an MLB pitcher wrapper based on the player passed.
	 *  
	 * @param p
	 */
	public MlbPitcher(MlbPlayer p) {
		onBasePercentage = p.getStatisticOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_OBP_AGAINST, .33);
		slugging = p.getStatisticOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_SLG_AGAINST, .45);
		groundBalls = p.getStatisticOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_SLG_AGAINST, .4);
		rank = p.getStatisticOrDefault(CsvPitcherConstants.COLUMN_RANK, .5);
		factor = p.getStatisticOrDefault(CsvPitcherConstants.COLUMN_FACTOR, .1);
	}
	
	/**
	 * @return the onBasePercentage
	 */
	public BigDecimal getOnBasePercentage() {
		return onBasePercentage;
	}
	
	/**
	 * @param onBasePercentage the onBasePercentage to set
	 */
	public void setOnBasePercentage(BigDecimal onBasePercentage) {
		this.onBasePercentage = onBasePercentage;
	}
	
	/**
	 * @return the slugging
	 */
	public BigDecimal getSlugging() {
		return slugging;
	}
	
	/**
	 * @param slugging the slugging to set
	 */
	public void setSlugging(BigDecimal slugging) {
		this.slugging = slugging;
	}
	
	/**
	 * @return the groundBalls
	 */
	public BigDecimal getGroundBalls() {
		return groundBalls;
	}
	
	/**
	 * @param groundBalls the groundBalls to set
	 */
	public void setGroundBalls(BigDecimal groundBalls) {
		this.groundBalls = groundBalls;
	}

	/**
	 * @return the rank
	 */
	public BigDecimal getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(BigDecimal rank) {
		this.rank = rank;
	}

	/**
	 * @return the factor
	 */
	public BigDecimal getFactor() {
		return factor;
	}

	/**
	 * @param factor the factor to set
	 */
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}

}
