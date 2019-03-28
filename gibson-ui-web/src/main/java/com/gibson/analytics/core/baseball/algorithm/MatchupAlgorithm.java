package com.gibson.analytics.core.baseball.algorithm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.gibson.analytics.core.baseball.stats.Bats;
import com.gibson.analytics.core.baseball.stats.BattingStatistics;
import com.gibson.analytics.core.baseball.stats.Pitches;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;

/***
 * 
 * @author leddy.eric
 *
 */
public final class MatchupAlgorithm {
	
	final static Logger log = LoggerFactory.getLogger(MatchupAlgorithm.class);
	
	private static MathContext DEFAULT_CONTEXT = MathContext.DECIMAL32;
	
	/**
	 * Convenience method for runs vs opposing pitcher
	 * 
	 * @param batters
	 * @param pitcher
	 * @param parkFactor
	 * 
	 * @return 54.2*((statsAverage*parkFactor)-0.006)^2.2*(inningsPerStart/9)
	 */
	public static double runsVsOpossingPitching(List<BattingStatistics> batters, PitchingStatistics pitcher, double parkFactor) {
		return runsVsOpossingPitching(batters, pitcher, BigDecimal.valueOf(parkFactor));
	}
	
	/**
	 * Calculate runs vs opposing pitcher.
	 * 
	 * @param batters
	 * @param pitcher
	 * @param parkFactor
	 * 
	 * @return 54.2*((statsAverage*parkFactor)-0.006)^2.2*(inningsPerStart/9)
	 */
	public static double runsVsOpossingPitching(List<? extends BattingStatistics> batters, PitchingStatistics pitcher, BigDecimal parkFactor) {
		DoubleSummaryStatistics statsVsBullpen = 
				batters
					.stream()
					.mapToDouble(p -> weightedOBA(p, pitcher))
					.collect(DoubleSummaryStatistics::new,
							 DoubleSummaryStatistics::accept,
							 DoubleSummaryStatistics::combine);
		
		double statsAverage = statsVsBullpen.getAverage();
		log.info("statsAverage :" + statsAverage);
		
		// 54.2*((statsAverage*parkFactor)-0.006)^2.2*(inningsPerStart/9)
		double parkAdjusted = ((statsAverage * parkFactor.doubleValue()) - .006);
		
		double a = 54.2;
		double b = Math.pow(parkAdjusted, 2.2);
		double c = pitcher
					.getProjectedInnings()
					.divide(BigDecimal.valueOf(9), DEFAULT_CONTEXT)
					.doubleValue();
		
		return a * b * c;
	}

	
	/**
	 * Calculates the weighted OBA for this particular matchup.
	 * 
	 * @param batter
	 * @param pitcher
	 * @return
	 */
	public static double weightedOBA(BattingStatistics batter, PitchingStatistics pitcher) {
		BigDecimal matchupAdjustment = matchupAdjustment(batter, pitcher);
		
		// Calculate EffSORatevsStart		
		BigDecimal effectiveStrikeoutRate = 
				batter.getStrikeOutRate()
				.multiply(pitcher.getStrikeoutRate())
				.divide(BigDecimal.valueOf(0.223), DEFAULT_CONTEXT);
	
		
		// Calculate EffWalkRatevsStarter		
		BigDecimal effectiveWalkRate = 
				batter.getWalkRate()
				.multiply(pitcher.getWalkRate())
				.divide(BigDecimal.valueOf(0.085), DEFAULT_CONTEXT);

		
		// Calculate EffwOBAvsStarter
		BigDecimal effectiveOBA = 
				batter.getOnBaseAverage()
				.multiply(pitcher.getOnBaseAverage())
				.divide(BigDecimal.valueOf(0.362), DEFAULT_CONTEXT)
				.multiply(matchupAdjustment);
		
		double totalWeightedOBA = totalWeightedOBA(effectiveWalkRate, effectiveStrikeoutRate, effectiveOBA);

		log.info(String.format("[EffSORatevsStart, EffWalkRatevsStarter, EffwOBAvsStarter, totalWeightedOBA] = [%1$.8f, %2$.8f, %3$.8f, %4$.8f]", 
				effectiveStrikeoutRate.doubleValue(),
				effectiveWalkRate.doubleValue(),
				effectiveOBA.doubleValue(),
				totalWeightedOBA));
		
		return totalWeightedOBA;
	}
	
	/**
	 * Gets the head to head adjustment.
	 * 
	 * @param b
	 * @param p
	 * @return
	 */
	protected static BigDecimal matchupAdjustment(BattingStatistics b, PitchingStatistics p) {
		return matchupAdjustment(b.bats(), p.pitches());
	}

	/**
	 * 
	 * @param bats
	 * @param pitches
	 * @return
	 */
	protected static BigDecimal matchupAdjustment(Bats bats, Pitches pitches) {
		Assert.notNull(bats, "Bats must be a non null value");
		Assert.notNull(pitches, "Pitches must be a non null value");
		if(Pitches.BULPEN != pitches) {
			if(Bats.LEFT == bats) {
				if(Pitches.LEFT == pitches) {
					return BigDecimal.valueOf(.938);
				}
				
				return BigDecimal.valueOf(1.012);
				
			} else if(Bats.RIGHT == bats) {
				
				if(Pitches.LEFT == pitches) {
					return BigDecimal.valueOf(1.012);
				}
				
				return BigDecimal.valueOf(.993);
			}			
		}

		// Switch hitters and Bullpen
		return BigDecimal.ONE;
	}

	/**
	 * Calculate the totalWeightedOBA
	 * 
	 * @param effectiveWalkRate
	 * @param effectiveStrikeoutRate
	 * @param effectiveOBA
	 * 
	 * @return total = effectiveWalkRate * .7 + (1 - effectiveStrikeoutRate - effectiveWalkRate) * effectiveOBA
	 */
	protected static double totalWeightedOBA(BigDecimal effectiveWalkRate, BigDecimal effectiveStrikeoutRate, BigDecimal effectiveOBA) {
		BigDecimal firstOp = effectiveWalkRate.multiply(BigDecimal.valueOf(.7));
		BigDecimal secondOp = BigDecimal.ONE.subtract(effectiveStrikeoutRate).subtract(effectiveWalkRate).multiply(effectiveOBA);
		
		return firstOp.add(secondOp).doubleValue();
	}

	/**
	 * Calculate the exponential line.
	 * 
	 * @param adjustedTotalRunsAway
	 * @param adjustedTotalRunsHome
	 * 
	 * @return exponentialLine = ((adjustedTotalRunsAway + adjustedTotalRunsHome) / 1)^0.284
	 */
	public static double exponentialLine(double adjustedTotalRunsAway, double adjustedTotalRunsHome) {
		return Math.pow(((adjustedTotalRunsAway + adjustedTotalRunsHome) / 1), 0.284);
	}
	
	/**
	 * Calculate the win percentage.
	 * 
	 * @param adjustedTotalRunsAway
	 * @param adjustedTotalRunsHome
	 * @return
	 */
	public static double winPercentage(double adjustedTotalRunsAway, double adjustedTotalRunsHome) {
		double expLine = exponentialLine(adjustedTotalRunsAway, adjustedTotalRunsHome);
		double max = Math.max(adjustedTotalRunsAway, adjustedTotalRunsHome);
		
		return Math.pow(max, expLine) / (Math.pow(adjustedTotalRunsAway, expLine) + Math.pow(adjustedTotalRunsHome, expLine));
		
	}

	/**
	 * Calculate the line give the projected runs.
	 * 
	 * @param adjustedTotalRunsAway
	 * @param adjustedTotalRunsHome
	 * 
	 * @return line = winPercentage * 100 / 1 - winPercentage
	 */
	public static double line(double adjustedTotalRunsAway, double adjustedTotalRunsHome) {
		double winPercentage = winPercentage(adjustedTotalRunsAway, adjustedTotalRunsHome);
		double line = (winPercentage * 100 / (1 - winPercentage));
		
		if(adjustedTotalRunsHome > adjustedTotalRunsAway) {
			return (-1 * line);
		}
		
		return line;
	}
	
	/**
	 * Calculates the adjusted total accounting for the opponents fielding and team base running
	 * 
	 * @param teamRunTotal
	 * @param teamBaseRunningAdjustment
	 * @param opponentFieldingAdjustment
	 * 
	 * @return teamRunTotal + teamBaseRunningAdjustment - opponentFieldingAdjustment * 9
	 */
	public static double adjustedTotalRuns(double teamRunTotal, double teamBaseRunningAdjustment, double opponentFieldingAdjustment) {
		return teamRunTotal +  teamBaseRunningAdjustment - opponentFieldingAdjustment * 9;
	}

	/**
	 * Convenience method for adjustedTotalRuns.
	 * 
	 * @param total
	 * @param calculateTeamBsR
	 * @param calculateTeamDefPerInn
	 * @return
	 */
	public static double adjustedTotalRuns(double total, BigDecimal teamBsR, BigDecimal opponentDefensePerInning) {
		return adjustedTotalRuns(total, teamBsR.doubleValue(), opponentDefensePerInning.doubleValue());
	}
}
