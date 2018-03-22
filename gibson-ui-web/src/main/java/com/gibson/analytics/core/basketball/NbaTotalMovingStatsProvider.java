package com.gibson.analytics.core.basketball;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.NbaTeam;

@Component
public class NbaTotalMovingStatsProvider extends AbstractNbaGameStatsProvider {

	@Override
	public GameStatistic createStatistics(NbaTeam home, NbaTeam away) {
		BigDecimal homeScoreMoving = new BigDecimal(home.getScoreMovingAverage());
		BigDecimal awayScoreMoving = new BigDecimal(away.getScoreMovingAverage());
		BigDecimal overUnderValueMoving = homeScoreMoving.add(awayScoreMoving).add(new BigDecimal(210)).setScale(1, RoundingMode.HALF_DOWN);

		return new GameStatistic("Total Moving", overUnderValueMoving.toString());
	}

}
