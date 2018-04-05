package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.repository.PlayerRepository;

public abstract class AbstractMlbGameStatsProvider implements GameStatisticsProvider {

	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	MlbRosterService service;
	
	@Override
	public GameStatistic createStatistics(Game game) {
		Lineup lineup = service.findActiveLineup(game);
		return createStatistics(game, lineup.getHome(),  lineup.getAway());
	}


	@Override
	public boolean providesFor(String league) {
		return SupportedLeagues.MLB.name().equals(league);
	}

	public GameStatistic createStatistics(Game game, List<Player> homeRoster, List<Player> awayRoster) {
		return createStatistics(game, extractStatisticsByPlayer(homeRoster), extractStatisticsByPlayer(awayRoster));
	}
	
	public abstract GameStatistic createStatistics(Game game, Map<Integer, Map<String, BigDecimal>> homeRoster,	Map<Integer, Map<String, BigDecimal>> awayRoster);
	
	
	private Map<Integer, Map<String, BigDecimal>> extractStatisticsByPlayer(List<Player> roster) {
		Map<Integer, Map<String, BigDecimal>> statNameValueMap = new HashMap<>();
		AtomicInteger index = new AtomicInteger(1);

		for (Player player : roster) {
			Map<String, BigDecimal> playerMap = new HashMap<>(); 
			List<PlayerStatistic> statistics = player.getStatistics();

			for (PlayerStatistic stat : statistics) {
				BigDecimal value = extractValueAsDecimal(stat.getValue());
				playerMap.put(stat.getName(), value);
			}

			statNameValueMap.put(index.getAndIncrement(), playerMap);
		}

		return statNameValueMap;
	}

	private BigDecimal extractValueAsDecimal(String value) {
		BigDecimal v = new BigDecimal(0);
		
		if(StringUtils.hasText(value)) {
			try {
				v = new BigDecimal(value);
			} catch (Exception e) {
				//System.out.println("Could not parse big decimal (" +value +") "+e.getMessage());
			}			
		}
		
		return v;
	}
}
