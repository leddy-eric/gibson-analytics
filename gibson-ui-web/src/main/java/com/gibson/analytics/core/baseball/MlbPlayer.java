package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gibson.analytics.core.baseball.stats.Bats;
import com.gibson.analytics.core.baseball.stats.BattingStatistics;
import com.gibson.analytics.core.baseball.stats.LeagueAverages;
import com.gibson.analytics.core.baseball.stats.Pitches;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.init.CsvPlayerConstants;

/**
 * Wrapper class for Player, adds convenience methods for accessing statistic values.
 * 
 * @author leddy.eric
 *
 */
public class MlbPlayer implements BattingStatistics {
	final static Logger log = LoggerFactory.getLogger(MlbPlayer.class);

	private static final String POSITION_PITCHER = "P";

	private Player player;
	private Map<String, BigDecimal> statistics = new HashMap<>();

	/**
	 * Create an MlbPlayer with the specified order in the lineup.
	 * 
	 * @param player
	 * @param order
	 */
	MlbPlayer(Player player) {
		this.player = player;
		this.statistics = 
				player.getStatistics()
					  .stream()
					  .collect(Collectors.toMap(PlayerStatistic::getName, PlayerStatistic::getValue));
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
	 * 
	 * @return
	 */
	@Override
	public boolean isPitcher() {
		return POSITION_PITCHER.equals(player.getPosition());
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
	public BigDecimal getStrikeOutRate() {
		return this.getStatisticOrDefault(CsvPlayerConstants.COLUMN_STRIKEOUT_RATE,	LeagueAverages.STRIKE_OUT_RATE);
	}

	@Override
	public BigDecimal getWalkRate() {
		return this.getStatisticOrDefault(CsvPlayerConstants.COLUMN_WALK_RATE,	LeagueAverages.WALK_RATE);
	}

	@Override
	public BigDecimal getOnBaseAverage() {
		return this.getStatisticOrDefault(CsvPlayerConstants.COLUMN_WEIGHTED_OBA, LeagueAverages.WEIGHTED_OBA);
	}

	@Override
	public Bats bats() {
		return Bats.valueOf(this.getStatisticOrDefault(CsvPlayerConstants.COLUMN_BATS, 1));
	}
	
	public String toString() {
		return Optional.of(player.getName()).orElse("League Average");
	}

}
