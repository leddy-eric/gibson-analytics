package com.gibson.analytics.core;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

public interface GameStatisticsProvider {
	
	/**
	 * 
	 * @param game
	 * @return
	 */
	public GameStatistic createStatistics(Game game);
	
	/**
	 * 
	 * @param league
	 * @return
	 */
	public boolean providesFor(String league);

}
