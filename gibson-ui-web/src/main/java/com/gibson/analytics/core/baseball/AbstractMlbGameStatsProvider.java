package com.gibson.analytics.core.baseball;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.repository.PlayerRepository;

public abstract class AbstractMlbGameStatsProvider implements GameStatisticsProvider {

	@Autowired
	PlayerRepository playerRepository;
	
	@Override
	public GameStatistic createStatistics(Game game) {
		String awayTeamName = game.getAway().getName();
		String homeTeamName = game.getHome().getName();
		
		return createStatistics(game, playerRepository.findByTeam(homeTeamName),  playerRepository.findByTeam(awayTeamName));
	}


	@Override
	public boolean providesFor(String league) {
		return SupportedLeagues.MLB.name().equals(league);
	}

	public abstract GameStatistic createStatistics(Game game, List<Player> homeRoster, List<Player> awayRoster);
}
