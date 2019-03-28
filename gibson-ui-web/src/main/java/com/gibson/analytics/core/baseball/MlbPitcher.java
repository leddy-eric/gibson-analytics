package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.gibson.analytics.core.baseball.stats.LeagueAverages;
import com.gibson.analytics.core.baseball.stats.Pitches;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.init.CsvPitcherConstants;


/**
 * Wrapper class for MLB Pitching statistics
 * 
 * @author leddy.eric
 *
 */
public class MlbPitcher implements PitchingStatistics {
	
	private Player player;
	private Map<String, BigDecimal> statistics = new HashMap<>();
	
	/**
	 * Convenience constructor for a league average pitcher.
	 * 
	 */
	public MlbPitcher() {
		this(new Player());
	}
	
	/**
	 * Create an MLB pitcher wrapper based on the player passed.
	 *  
	 * @param p
	 */
	public MlbPitcher(Player player) {
		this.setPlayer(player);
		this.statistics = player.getStatistics()
						   .stream()
						   .collect(Collectors.toMap(PlayerStatistic::getName, PlayerStatistic::getValue)); 
				
	}	

	@Deprecated
	public BigDecimal getFactor() {
		// TODO - Figure out where factor goes.
		return BigDecimal.ONE;
	}

	@Deprecated
	public BigDecimal getRank() {
		// TODO - Figure out where factor goes.
		return BigDecimal.ONE;
	}

	@Override
	public BigDecimal getOnBaseAverage() {
		return getStatisticOrDefault(CsvPitcherConstants.COLUMN_WEIGHTED_OBP, LeagueAverages.WEIGHTED_OBA);
	}

	@Override
	public BigDecimal getProjectedInnings() {
		return getStatisticOrDefault(CsvPitcherConstants.COLUMN_INN_PER_START, LeagueAverages.EXPECTED_INNINGS);
	}


	@Override
	public BigDecimal getStrikeoutRate() {
		return getStatisticOrDefault(CsvPitcherConstants.COLUMN_STRIKEOUT_RATE, LeagueAverages.STRIKE_OUT_RATE);
	}


	@Override
	public BigDecimal getWalkRate() {
		return getStatisticOrDefault(CsvPitcherConstants.COLUMN_WALK_RATE, LeagueAverages.WALK_RATE);
	}
	
	/**
	 * 
	 * @param statisticName
	 * @param value
	 * @return
	 */
	protected BigDecimal getStatisticOrDefault(String statisticName, double value) {
		return statistics.getOrDefault(statisticName, BigDecimal.valueOf(value));
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public Pitches pitches() {
		// TODO Auto-generated method stub
		return Pitches.RIGHT;
	}

}
