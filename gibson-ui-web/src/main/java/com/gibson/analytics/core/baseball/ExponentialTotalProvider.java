package com.gibson.analytics.core.baseball;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gibson.analytics.core.baseball.algorithm.MatchupAlgorithm;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class ExponentialTotalProvider extends AbstractMlbGameStatsProvider {
	final static Logger log = LoggerFactory.getLogger(ExponentialTotalProvider.class);

	@Override
	public GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away) {
		ParkFactor parkFactor = getHomeParkFactor(game);

		double awayRuns = away.runsAgainst(home, parkFactor); 
		double homeRuns = home.runsAgainst(away, parkFactor);
		
		double winPercentage = MatchupAlgorithm.homeWinPercentage(awayRuns, homeRuns);
		
		log.info("Total: away :"+ awayRuns + " home: "+ homeRuns + " win % "+ winPercentage);
		double total = (homeRuns + awayRuns) * (0.966 - (.028 * winPercentage));
		log.info("Total:" + total);
		total = Math.floor(total * 100) / 100;
		log.info("Total:" + total);
		
		return new GameStatistic("ExTotal", Double.toString(total));
	}


}
