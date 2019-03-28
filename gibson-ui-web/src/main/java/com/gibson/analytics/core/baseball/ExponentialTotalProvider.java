package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.gibson.analytics.core.baseball.algorithm.MatchupAlgorithm;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class ExponentialTotalProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away) {
		if(!home.isValid() || !away.isValid()) {
			return new GameStatistic("ExTotal", "INVALID "+ game.getStatus());
		}
		
		BigDecimal fieldFactor = getHomeParkFactor(game);

		double awayRuns = away.runsAgainst(home, fieldFactor); 
		double homeRuns = home.runsAgainst(away, fieldFactor);
		
		double winPercentage = MatchupAlgorithm.winPercentage(awayRuns, homeRuns);
		
		double total = (homeRuns + awayRuns) * (0.96 - .028 * winPercentage);
		total = Math.floor(total * 100) / 100;
		
		return new GameStatistic("ExTotal", Double.toString(total));
	}


}
