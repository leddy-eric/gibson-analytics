package com.gibson.analytics.core.basketball;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.NbaTeam;

@Component
public class NbaSpreadMovingStatsProvider extends AbstractNbaGameStatsProvider {

	@Override
	public GameStatistic createStatistics(NbaTeam home, NbaTeam away) {
		BigDecimal spreadMoving = new BigDecimal(away.getSpreadScoreMovingAverage())
				.subtract(new BigDecimal(home.getSpreadScoreMovingAverage()))
				.subtract(new BigDecimal(3.5)).setScale(1, RoundingMode.HALF_DOWN);

		return new GameStatistic("Spread Moving Average", spreadMoving.toString());
	}

}
