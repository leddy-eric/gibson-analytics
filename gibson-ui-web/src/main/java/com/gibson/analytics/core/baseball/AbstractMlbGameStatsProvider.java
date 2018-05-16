package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.repository.MlbGameDetailRepository;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

public abstract class AbstractMlbGameStatsProvider implements GameStatisticsProvider {
	final static Logger log = LoggerFactory.getLogger(AbstractMlbGameStatsProvider.class);

	@Autowired
	MlbRosterService service;
	
	@Autowired
	MlbParkFactorService parkService;
	
	@Autowired
	MlbGameService gameService;
	
	@Autowired
	MlbGameDetailRepository repository;
	
	/**
	 * Lookup method for park factors, should go in team statistics eventually. TODO - Move this into the MLBLineup object.
	 * 
	 * @param game
	 * @return
	 */
	public BigDecimal getHomeParkFactor(Game game) {
		return parkService.findParkFactor(game.getHome().getName());
	}

	/**
	 * 
	 */
	@Override
	public GameStatistic createStatistics(Game game) {
		GameStatistic stat = new GameStatistic("N/A", "N/A");
		
		try {
			stat = createStatistics(game, repository.findByApiId(game.getId()));			
		} catch (Exception e) {
			log.error("Stats provider failed on "+game.getId() + " : "+ e.getMessage());;
		}
		
		return stat;
	}

	/**
	 * To be implemented by extended subclasses.
	 * 
	 * @param game
	 * @param details
	 * @return
	 */
	public GameStatistic createStatistics(Game game, Optional<MlbGameDetail> details) {
		if(details.isPresent()) {
			MlbGame mlbGame = gameService.getGame(details.get());
			return createStatistics(game, mlbGame.getHome(), mlbGame.getAway());			
		}
		
		return new GameStatistic("N/A", "N/A");
	}
	
	/**
	 * 
	 * @param game
	 * @param home
	 * @param away
	 * @return
	 */
	public abstract  GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away); 

	@Override
	public boolean providesFor(String league) {
		return SupportedLeagues.MLB.name().equals(league);
	}


}
