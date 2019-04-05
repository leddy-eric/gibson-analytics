package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gibson.analytics.core.baseball.stats.LeagueAverages;
import com.gibson.analytics.core.baseball.stats.Pitches;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.init.CsvPitcherConstants;


/**
 * Wrapper class for MLB Pitching statistics
 * 
 * @author leddy.eric
 *
 */
public class MlbPitcher implements PitchingStatistics {
	
	private Optional<Player> player = Optional.empty();
	private Map<String, BigDecimal> statistics = new HashMap<>();
	
	/**
	 * Convenience constructor for a league average pitcher.
	 * 
	 */
	public MlbPitcher() {
		this(null);
	}
	
	/**
	 * Create an MLB pitcher wrapper based on the player passed.
	 *  
	 * @param p
	 */
	public MlbPitcher(Player player) {
		this.setPlayer(player);
		
		if(this.player.isPresent()) {
			this.statistics = player.getStatistics()
					   .stream()
					   .collect(Collectors.toMap(PlayerStatistic::getName, PlayerStatistic::getValue)); 	
		}
				
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
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = Optional.ofNullable(player);
	}
	
	/**
	 * 
	 */
	public Player getPlayer() {
		return player.orElse(new Player());
	}

	@Override
	public Pitches pitches() {
		return Pitches.valueOf(getStatisticOrDefault(CsvPitcherConstants.COLUMN_THROWS, 1));
	}

	public String getName() {
		if(player.isPresent()) {
			Optional<String> name = Optional.ofNullable(player.get().getName());
			
			return name.orElse("League Average");
		}
		
		return "League Average";
	}

	@Override
	public MlbTeamLookup getTeam() {
		if(player.isPresent()) {
			return MlbTeamLookup.lookupFromTeamName(player.get().getTeam());
		}
		
		System.out.println("WARN ****** USING UNKNOWN ********** WARN");
		return MlbTeamLookup.UNKNOWN;
	}

}
