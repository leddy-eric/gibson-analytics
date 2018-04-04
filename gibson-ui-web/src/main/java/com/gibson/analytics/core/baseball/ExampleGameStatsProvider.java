package com.gibson.analytics.core.baseball;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Player;

@Component
public class ExampleGameStatsProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, List<Player> homeRoster, List<Player> awayRoster) {
		return new GameStatistic("rosterSize", Integer.toString(homeRoster.size()));
	}

}
