package com.gibson.analytics.core.basketball;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.NbaTeam;
import com.gibson.analytics.repository.NbaTeamRepository;

public abstract class AbstractNbaGameStatsProvider implements GameStatisticsProvider {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NbaTeamRepository repository;
	
	/**
	 * 
	 * @param game
	 * @return
	 */
	public GameStatistic createStatistics(Game game) {
		log.info("Adding statistics for "+game.getId());

		Optional<NbaTeam> awayTeam = repository.findById(game.getAway().getName());
		Optional<NbaTeam> homeTeam = repository.findById(game.getHome().getName());

		if(awayTeam.isPresent() && homeTeam.isPresent()) {
			NbaTeam away = awayTeam.get();
			NbaTeam home = homeTeam.get();
			
			return createStatistics(home, away);
		}
		
		return new GameStatistic("N/A", "N/A");
	}

	
	@Override
	public boolean providesFor(String league) {
		return SupportedLeagues.NBA.name().equals(league);
	}
	
	/**
	 * To be implemented by concrete subclasses.
	 * 
	 * @param home
	 * @param away
	 * @return
	 */
	public abstract GameStatistic createStatistics(NbaTeam home, NbaTeam away);

}
