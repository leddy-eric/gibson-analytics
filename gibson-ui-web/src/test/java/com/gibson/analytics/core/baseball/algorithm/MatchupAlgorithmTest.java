package com.gibson.analytics.core.baseball.algorithm;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.Test;

import com.gibson.analytics.core.baseball.stats.Bats;
import com.gibson.analytics.core.baseball.stats.BattingStatistics;
import com.gibson.analytics.core.baseball.stats.DefaultBattingStatistic;
import com.gibson.analytics.core.baseball.stats.DefaultPitchingStatistic;
import com.gibson.analytics.core.baseball.stats.Pitches;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;
import com.gibson.analytics.core.baseball.stats.StatisticFactory;

public class MatchupAlgorithmTest {

	public BattingStatistics leagueAverageBatter = StatisticFactory.leagueAverageBatter();
	public PitchingStatistics leagueAveragePitcher = StatisticFactory.leagueAveragePitcher();
	public PitchingStatistics leagueAverageBullpen = StatisticFactory.leagueAveragePitcher();
	
	
	@Test
	public void testRunsVsStarter() {
		// Starter
		PitchingStatistics pitcher = new DefaultPitchingStatistic(.127947857, .048834833, 0.385062383, 6, Pitches.RIGHT);
		
		// Lineup
		BattingStatistics[] batters = 
				new BattingStatistics[] {
						new DefaultBattingStatistic(.22305243, .082667626, .365973176, Bats.RIGHT),
						new DefaultBattingStatistic(.250313743, .081625556, .356436747, Bats.RIGHT),
						new DefaultBattingStatistic(.238738238, .057022076, .333052952, Bats.RIGHT),
						new DefaultBattingStatistic(.270214114, .089139694, .427589495, Bats.RIGHT),
						new DefaultBattingStatistic(.261077044, .101513326, .374331954, Bats.RIGHT),
						new DefaultBattingStatistic(.216748768, .084783584, .358440741, Bats.LEFT),
						new DefaultBattingStatistic(.162599763, .0685197, .311191965, Bats.SWITCH),
						new DefaultBattingStatistic(.266880786, .084987817, .369399005, Bats.SWITCH),
						new DefaultBattingStatistic(.23415064, .066262526, .33737031, Bats.RIGHT)};
		
		List<BattingStatistics> lineup = new ArrayList<>(Arrays.asList(batters));

	
		double runsVsStarter = MatchupAlgorithm.runsVsOpossingPitching(lineup, pitcher, 0.987);
		
		assertEquals("Runs Vs Starter failure", 3.213022707, runsVsStarter , .00001);
	}
	
	@Test
	public void testRunsVsBullpen() {
		// Starter
		PitchingStatistics pitcher = new DefaultPitchingStatistic(0.287929572, 0.074157929, 0.361064668, 3, Pitches.BULPEN);
		
		// Lineup
		BattingStatistics[] batters = 
				new BattingStatistics[] {
						new DefaultBattingStatistic(.22305243, .082667626, .365973176, Bats.RIGHT),
						new DefaultBattingStatistic(.250313743, .081625556, .356436747, Bats.RIGHT),
						new DefaultBattingStatistic(.238738238, .057022076, .333052952, Bats.RIGHT),
						new DefaultBattingStatistic(.270214114, .089139694, .427589495, Bats.RIGHT),
						new DefaultBattingStatistic(.261077044, .101513326, .374331954, Bats.RIGHT),
						new DefaultBattingStatistic(.216748768, .084783584, .358440741, Bats.LEFT),
						new DefaultBattingStatistic(.162599763, .0685197, .311191965, Bats.SWITCH),
						new DefaultBattingStatistic(.266880786, .084987817, .369399005, Bats.SWITCH),
						new DefaultBattingStatistic(.23415064, .066262526, .33737031, Bats.RIGHT)};
		
		List<BattingStatistics> lineup = new ArrayList<>(Arrays.asList(batters));

		
		double runsVsBullpen = MatchupAlgorithm.runsVsOpossingPitching(lineup, pitcher, 0.987);
		
		assertEquals("Runs Vs Bullpen failure", 0.950547578, runsVsBullpen , .00001);
	}
	
	@Test
	public void testMatchupAdjustments() {
		assertEquals("R vs Bullpen", 1.00, MatchupAlgorithm.matchupAdjustment(Bats.RIGHT, Pitches.BULPEN).doubleValue(), 0);
		assertEquals("R vs L", 1.012, MatchupAlgorithm.matchupAdjustment(Bats.RIGHT, Pitches.LEFT).doubleValue(), 0);
		assertEquals("R vs R", 0.993, MatchupAlgorithm.matchupAdjustment(Bats.RIGHT, Pitches.RIGHT).doubleValue(), 0);
		
		assertEquals("L vs Bullpen", 1.00, MatchupAlgorithm.matchupAdjustment(Bats.LEFT, Pitches.BULPEN).doubleValue(), 0);
		assertEquals("L vs L", 0.938, MatchupAlgorithm.matchupAdjustment(Bats.LEFT, Pitches.LEFT).doubleValue(), 0);
		assertEquals("L vs R", 1.012, MatchupAlgorithm.matchupAdjustment(Bats.LEFT, Pitches.RIGHT).doubleValue(), 0);
		
		assertEquals("SWITCH vs Bullpen", 1.0, MatchupAlgorithm.matchupAdjustment(Bats.SWITCH, Pitches.BULPEN).doubleValue(), 0);
		assertEquals("SWITCH vs L", 1.0, MatchupAlgorithm.matchupAdjustment(Bats.SWITCH, Pitches.LEFT).doubleValue(), 0);
		assertEquals("SWITCH vs R", 1.0, MatchupAlgorithm.matchupAdjustment(Bats.SWITCH, Pitches.RIGHT).doubleValue(), 0);

	}
	
	@Test
	public void testSpecificWeightedOBA() {
		BattingStatistics batter =  new DefaultBattingStatistic(.22305243, .082667626, .365973176, Bats.RIGHT);
		PitchingStatistics pitcher = new DefaultPitchingStatistic(.127947857, .048834833, 0.385062383, 5.4, Pitches.RIGHT);
		
		double weightedOBA = MatchupAlgorithm.weightedOBA(batter, pitcher);
		
		assertEquals("Specific OBA failed", 0.351978644, weightedOBA, .00001);
	}
	
	@Test
	public void testExponentialLine() {
		assertEquals("Exponential Line", 1.773642473, MatchupAlgorithm.exponentialLine(4.057683987, 3.463548523), .00001);
	}
	
	@Test
	public void testWinPercentage() {
		assertEquals("Win percentage", 0.56974254, MatchupAlgorithm.winPercentage(4.057683987, 3.463548523), .00001);
	}
	
	@Test
	public void testLine() {
		assertEquals("Line away runs max", 132.4189798, MatchupAlgorithm.line(4.057683987, 3.463548523), .00001);
		assertEquals("Line home runs max", -132.4189798, MatchupAlgorithm.line(3.463548523, 4.057683987), .00001);
	}
	
	@Test
	public void testTotalWeightedOBA() {
		double totalWeightedOBA = 
				MatchupAlgorithm.totalWeightedOBA(BigDecimal.valueOf(0.04749482),
												  BigDecimal.valueOf(0.1279779), 
												  BigDecimal.valueOf(0.3865636791));
		
		assertEquals("TotalWeightedOBA Failed ", 0.351978644, totalWeightedOBA, .0000001);
	}
	
	@Test
	public void adjustedTotalRuns() {
		// Example BsR per game
		double teamBaseRunningAdjustment = 
				DoubleStream.of(0.00144000871941915, 
								0.00349908493443602, 
								-0.00298, 
								0.0000306306306306306, 
								0.00372987440990469).sum();
		// Example DefPerInn
		double opponentFieldingAdjustment = 
				DoubleStream.of(0.00497450946937383, 
								0.0032757948906451, 
								-0.0000389063259478909, 
								0.000765889218267034, 
								-0.00440428133176495, 
								0.00702691932319292, 
								0.000800729927007299).sum();
		
		
		
		double totalRuns = (3.213022707 + 0.950547578);
		
		assertEquals("Ajusted total runs", 4.057683987, 
				MatchupAlgorithm.adjustedTotalRuns(totalRuns , teamBaseRunningAdjustment, opponentFieldingAdjustment), .00001);
	}
	

}
