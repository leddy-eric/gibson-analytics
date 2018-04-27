package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;

public abstract class AbstractMlbGameStatsProvider implements GameStatisticsProvider {

	@Autowired
	MlbRosterService service;
	
	@Autowired
	MlbParkFactorService parkService;

	@Override
	public GameStatistic createStatistics(Game game) {
		Lineup lineup = service.findActiveLineup(game);
		return createStatistics(game, lineup.getHome(),  lineup.getAway());
	}


	@Override
	public boolean providesFor(String league) {
		return SupportedLeagues.MLB.name().equals(league);
	}
	
	/**
	 * Lookup method for park factors, should go in team statistics eventually.
	 * 
	 * @param game
	 * @return
	 */
	public BigDecimal getHomeParkFactor(Game game) {
		return parkService.findParkFactor(game.getHome().getName());
	}

	public GameStatistic createStatistics(Game game, List<Player> homeRoster, List<Player> awayRoster) {
		return createStatistics(game, extractStatisticsByPlayer(homeRoster), extractStatisticsByPlayer(awayRoster));
	}

	public abstract GameStatistic createStatistics(Game game, Map<Player, Map<String, BigDecimal>> homeRoster,	Map<Player, Map<String, BigDecimal>> awayRoster);


	private Map<Player, Map<String, BigDecimal>> extractStatisticsByPlayer(List<Player> roster) {
		LinkedHashMap<Player, Map<String, BigDecimal>> statNameValueMap = new LinkedHashMap<>();

		for (Player player : roster) {
			Map<String, BigDecimal> playerMap = new HashMap<>(); 
			List<PlayerStatistic> statistics = player.getStatistics();
			if(statistics != null) {
				for (PlayerStatistic stat : statistics) {
					BigDecimal value = extractValueAsDecimal(stat.getValue());
					playerMap.put(stat.getName(), value);
				}	
			}

			statNameValueMap.put(player, playerMap);
		}

		return statNameValueMap;
	}

	private BigDecimal extractValueAsDecimal(String value) {
		if(StringUtils.hasText(value)) {
			try {
				return new BigDecimal(value);
			} catch (Exception e) {
				//System.out.println("Could not parse big decimal (" +value +") "+e.getMessage());
			}			
		}

		return BigDecimal.valueOf(0);
	}
}
