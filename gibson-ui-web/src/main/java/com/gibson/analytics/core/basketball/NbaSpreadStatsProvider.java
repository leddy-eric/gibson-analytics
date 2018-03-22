package com.gibson.analytics.core.basketball;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.NbaTeam;

@Component
public class NbaSpreadStatsProvider extends AbstractNbaGameStatsProvider {

	@Override
	public GameStatistic createStatistics(NbaTeam home, NbaTeam away) {
		BigDecimal spread = new BigDecimal(away.getSpreadScore())
				.subtract(new BigDecimal(home.getSpreadScore()))
				.subtract(new BigDecimal(3.5)).setScale(1, RoundingMode.HALF_DOWN);


		return new GameStatistic("Spread", spread.toString());
	}

}
