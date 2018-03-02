package com.gibson.analytics.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.core.ScoreboardProvider;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Scoreboard;

@Service
public class ScoreboardServiceImpl implements ScoreboardService {
	
	@Autowired
	BaseballAPI baseballApi;
	
	@Autowired
	List<ScoreboardProvider> scoreboardProviders;
	
	@Autowired
	StatisticsService statisticsService;
	
	@Autowired(required=false)
	OddsService oddsService;
	
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.service.ScoreboardService#getScoreboard()
	 */
	@Override
	public List<Scoreboard> getScoreboard() {
		return getScoreboard(LocalDate.now());
	}
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.service.ScoreboardService#getScoreboard(java.time.LocalDate)
	 */
	@Override
	public List<Scoreboard> getScoreboard(LocalDate date) {
		List<Scoreboard> fullScoreboard = new ArrayList<>();
		
		for (ScoreboardProvider provider : scoreboardProviders) {
			Scoreboard scoreboard = provider.getScoreboard(date);
			scoreboard.setLeague(provider.getLeagueIdentifier());

			for (Game game : scoreboard.getGames()) {
				List<GameStatistic> statistics = statisticsService.createStatistics(game);
				game.setGameStatistics(statistics);
				
				if(oddsService != null) {
					oddsService.createOdds(game);					
				}
			}

			fullScoreboard.add(scoreboard);
		}
		
		return fullScoreboard;
	}

}
