package com.gibson.analytics.core.baseball;

import com.gibson.analytics.core.GameStatisticsProvider;
import com.gibson.analytics.core.SupportedLeagues;

public abstract class AbstractMlbGameStatsProvider implements GameStatisticsProvider {


	@Override
	public boolean providesFor(String league) {
		return SupportedLeagues.MLB.name().equals(league);
	}

}
