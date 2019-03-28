package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.gibson.analytics.core.baseball.algorithm.MatchupAlgorithm;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class ExponentialLineProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away) {		

		
		BigDecimal fieldFactor = getHomeParkFactor(game);
		
		double exponentialLine = 
				MatchupAlgorithm.line(away.runsAgainst(home, fieldFactor), home.runsAgainst(away, fieldFactor));
		
		return new GameStatistic("ExLine", asFormattedString(exponentialLine));
	}
	
	/**
	 * 
	 * @param awayRuns
	 * @param runs
	 * @return
	 */
	protected String calculateLine(double awayRuns, double runs) {
		double lineExp = Math.pow((awayRuns + runs),.284); // .284 will not change
		double line = 0;
		if(runs >= awayRuns) {
			line = Math.pow(runs,lineExp)/(Math.pow(awayRuns, lineExp) + Math.pow(runs, lineExp));
			line = -(line*100)/(1-line);
		}
		if(awayRuns > runs) {
		    line = Math.pow(awayRuns,lineExp)/(Math.pow(awayRuns, lineExp) + Math.pow(runs, lineExp));
			line = (line*100)/(1-line);
		}
		
		BigDecimal outputLine = BigDecimal.valueOf(line);
		
		return outputLine.setScale(0,RoundingMode.HALF_DOWN).toString();
	}

}
