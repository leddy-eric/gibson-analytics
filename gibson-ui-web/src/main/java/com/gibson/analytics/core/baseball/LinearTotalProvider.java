package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Player;

@Component
public class LinearTotalProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, Map<Player, Map<String, BigDecimal>> homeRoster, Map<Player, Map<String, BigDecimal>> awayRoster) {
		return new GameStatistic("Linear Total", "1");
	}

}