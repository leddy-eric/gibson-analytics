package com.gibson.analytics.core.baseball;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gibson.analytics.core.baseball.algorithm.MatchupAlgorithm;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class ExponentialLineProvider extends AbstractMlbGameStatsProvider {
	final static Logger log = LoggerFactory.getLogger(ExponentialLineProvider.class);

	@Override
	public GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away) {		
		ParkFactor parkFactor = getHomeParkFactor(game);
		
		double runsAgainst = away.runsAgainst(home, parkFactor);
		double homeRunsAgainst = home.runsAgainst(away, parkFactor);
		
		log.debug("ExLine: "+ runsAgainst +" - "+homeRunsAgainst);
		
		double exponentialLine = MatchupAlgorithm.line(runsAgainst, homeRunsAgainst);
		
		log.debug("ExLine: "+ exponentialLine);
		return new GameStatistic("ExLine", asFormattedString(exponentialLine));
	}

}
