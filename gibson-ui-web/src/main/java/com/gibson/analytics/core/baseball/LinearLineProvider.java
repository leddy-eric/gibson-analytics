package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class LinearLineProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away) {
		return new GameStatistic("Linear Line", calculateLine(home.getStartingPitcher().getRank(), away.getStartingPitcher().getRank()));
	}

	/**
	 * Home Rank and Away Rank are just the pitcher rank from the game. (pitcher.csv)
	 * 
	 * @param homeRank
	 * @param awayRank
	 * 
	 * @return
	 */
	private String calculateLine(BigDecimal homeRank, BigDecimal awayRank) {
		BigDecimal point535 = BigDecimal.valueOf(.535);
		BigDecimal line = homeRank.subtract(awayRank).add(point535);

		BigDecimal oneHundred = BigDecimal.valueOf(100);
		BigDecimal one = BigDecimal.valueOf(1);
		BigDecimal negativeOne = BigDecimal.valueOf(-1);

		if(line.compareTo(BigDecimal.valueOf(.5)) < 0) {
			//line = (line*100)/(1-line);
			line = one.subtract(line);
			line = line.multiply(oneHundred).divide(one.subtract(line), 5, RoundingMode.HALF_UP);
		} else {
			//line = -(line*100)/(1-line);
			line = line.multiply(oneHundred).divide(one.subtract(line), 5, RoundingMode.HALF_UP).multiply(negativeOne);			
		}
		
		return line.setScale(2, RoundingMode.HALF_DOWN).toString();
	}

}