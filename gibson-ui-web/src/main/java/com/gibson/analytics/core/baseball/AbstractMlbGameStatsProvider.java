package com.gibson.analytics.core.baseball;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
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

	public abstract GameStatistic createStatistics(Game game, List<Player> homeRoster, List<Player> awayRoster);
}
