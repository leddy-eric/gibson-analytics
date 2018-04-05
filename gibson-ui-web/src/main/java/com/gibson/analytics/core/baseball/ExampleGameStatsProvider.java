package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class ExampleGameStatsProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, Map<Integer, Map<String, BigDecimal>> homeRoster, Map<Integer, Map<String, BigDecimal>> awayRoster) {		
		// First batters stats
		Map<String, BigDecimal> firstBatter = homeRoster.get(new Integer(1));
		System.out.println("First Batter FinalOBP " + firstBatter.get("FinalOBP"));

		return new GameStatistic("rosterSize", Integer.toString(homeRoster.size()));
	}


}
