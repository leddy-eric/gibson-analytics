package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.data.TeamStatistic;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.init.CsvPitcherConstants;
import com.gibson.analytics.init.CsvTeamConstants;

/**
 * Used to create Statistics instances.
 * 
 * @author leddy.eric
 *
 */
public class StatisticFactory {
	
	final static Logger log = LoggerFactory.getLogger(StatisticFactory.class);
	
	final static BigDecimal REGULATION_INNINGS = BigDecimal.valueOf(9);

	
	/**
	 * Returns a league average bullpen.
	 * @param team 
	 * 
	 * @return
	 */
	public static PitchingStatistics leagueAverageBullpen(MlbTeamLookup team) {
		return new DefaultPitchingStatistic(LeagueAverages.STRIKE_OUT_RATE, 
											LeagueAverages.WALK_RATE, 
											LeagueAverages.WEIGHTED_OBA,
											(9 - LeagueAverages.EXPECTED_INNINGS),
											LeagueAverages.FACTOR,
											LeagueAverages.RANK,
											Pitches.BULPEN,
											team);
	}
	
	/**
	 * Returns a league average starting pitcher.
	 * 
	 * @return
	 */
	public static PitchingStatistics leagueAveragePitcher(MlbTeamLookup team) {
		return new DefaultPitchingStatistic(LeagueAverages.STRIKE_OUT_RATE, 
											LeagueAverages.WALK_RATE, 
											LeagueAverages.WEIGHTED_OBA, 
											LeagueAverages.WEIGHTED_OBA,
											LeagueAverages.FACTOR,
											LeagueAverages.RANK,
											Pitches.RIGHT,
											team);
	}
	
	/**
	 * Returns a league average batter.
	 * 
	 * @return
	 */
	public static BattingStatistics leagueAverageBatter() {
		return new DefaultBattingStatistic(LeagueAverages.STRIKE_OUT_RATE, 
										   LeagueAverages.WALK_RATE, 
										   LeagueAverages.WEIGHTED_OBA, 
										   Bats.RIGHT);
	}

	/**
	 * 
	 * @param starter
	 * @return
	 */
	public static PitchingStatistics pitcherFrom(Player starter) {
		MlbTeamLookup team = MlbTeamLookup.lookupFromTeamName(starter.getTeam());
		if(isValid(starter)) {
			Map<String, BigDecimal> stats = 
					starter.getStatistics()
						.stream()
						.collect(Collectors.toMap(PlayerStatistic::getName, PlayerStatistic::getValue)); 
			
			BigDecimal soRate = stats.getOrDefault(CsvPitcherConstants.COLUMN_STRIKEOUT_RATE, LeagueAverages.BIG_DECIMAL_STRIKE_OUT_RATE);
			BigDecimal walkRate = stats.getOrDefault(CsvPitcherConstants.COLUMN_WALK_RATE, LeagueAverages.BIG_DECIMAL_WALK_RATE);
			BigDecimal weightedOBA = stats.getOrDefault(CsvPitcherConstants.COLUMN_WEIGHTED_OBP, LeagueAverages.BIG_DECIMAL_WEIGHTED_OBA);
			BigDecimal expectedInnings = stats.getOrDefault(CsvPitcherConstants.COLUMN_INN_PER_START, LeagueAverages.BIG_DECIMAL_EXPECTED_INNINGS);
			BigDecimal pitches = stats.getOrDefault(CsvPitcherConstants.COLUMN_THROWS, BigDecimal.ONE);
			
			return new DefaultPitchingStatistic(soRate.doubleValue(), 
												walkRate.doubleValue(), 
												weightedOBA.doubleValue(), 
												expectedInnings.doubleValue(), 
												LeagueAverages.FACTOR,
												LeagueAverages.RANK,
												Pitches.valueOf(pitches),
												team);
		}
		
		log.warn("Building a league avg starter ");
		return leagueAveragePitcher(team);
	}
	
	/**
	 * Builds PitchingStatistics from team and a starting pitcher.
	 * 
	 * @param team
	 * @param starter
	 * @return
	 */
	public static PitchingStatistics bullpenFrom(Team team, Player starter) {
		MlbTeamLookup mlbTeam = MlbTeamLookup.lookupFromTeamName(team.getName());
		if(isValid(team) && isValid(starter)) {
			Map<String, BigDecimal> stats = 
					team.getTeamStatistics()
						.stream()
						.collect(Collectors.toMap(TeamStatistic::getName, TeamStatistic::getValue)); 
			
			BigDecimal soRate = stats.getOrDefault(CsvTeamConstants.COLUMN_STRIKEOUT_RATE, LeagueAverages.BIG_DECIMAL_STRIKE_OUT_RATE);
			BigDecimal walkRate = stats.getOrDefault(CsvTeamConstants.COLUMN_WALK_RATE, LeagueAverages.BIG_DECIMAL_WALK_RATE);
			BigDecimal weightedOBA = stats.getOrDefault(CsvTeamConstants.COLUMN_WEIGHTED_OBA, LeagueAverages.BIG_DECIMAL_WEIGHTED_OBA);
			
			Optional<PlayerStatistic> startersInnings = starter.getStatistics()
															.stream()
															.filter(p -> CsvPitcherConstants.COLUMN_INN_PER_START.equals(p.getName()))
															.findFirst();
			
			BigDecimal expectedInnings = REGULATION_INNINGS.subtract(LeagueAverages.BIG_DECIMAL_EXPECTED_INNINGS);
			
			if(startersInnings.isPresent()) {
				expectedInnings = REGULATION_INNINGS.subtract(startersInnings.get().getValue());	
			}
			
			return new DefaultPitchingStatistic(soRate.doubleValue(), 
												walkRate.doubleValue(),
												weightedOBA.doubleValue(), 
												expectedInnings.doubleValue(),
												LeagueAverages.FACTOR,
												LeagueAverages.RANK,
												Pitches.BULPEN,
												mlbTeam);
		}
		
		log.warn("Building a league avg bullpen ");
		
		return leagueAverageBullpen(mlbTeam);
	}

	/**
	 * Validate the player has some statistics.
	 * 
	 * @param starter
	 * @return
	 */
	private static boolean isValid(Player player) {
		return (player != null) && (!player.getStatistics().isEmpty());
	}

	/**
	 * Validate the team has some statistics.
	 * 
	 * @param team
	 * @return
	 */
	private static boolean isValid(Team team) {
		return (team != null) && (!team.getTeamStatistics().isEmpty());
	}
	
}
