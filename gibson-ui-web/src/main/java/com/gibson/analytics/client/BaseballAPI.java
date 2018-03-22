package com.gibson.analytics.client;

import com.gibson.analytics.core.ScoreboardProvider;
import com.gibson.analytics.data.Lineup;

public interface BaseballAPI extends ScoreboardProvider {
	
	/**
	 * 
	 * @param gameDataDirectory
	 * @return
	 */
	public Lineup getLineup(String gameDataDirectory);

}
