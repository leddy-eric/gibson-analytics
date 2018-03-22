package com.gibson.analytics.core.basketball;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.NbaTeam;

@Component
public class NbaTotalStatsProvider extends AbstractNbaGameStatsProvider {

	@Override
	public GameStatistic createStatistics(NbaTeam home, NbaTeam away) {
		BigDecimal homeScore = new BigDecimal(home.getScore());
		BigDecimal awayScore = new BigDecimal(away.getScore());

		BigDecimal overUnderValue = homeScore.add(awayScore).add(new BigDecimal(210)).setScale(1, RoundingMode.HALF_DOWN);

		return new GameStatistic("Total", overUnderValue.toString());
	}

}
