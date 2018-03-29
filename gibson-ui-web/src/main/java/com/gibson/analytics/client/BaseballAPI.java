package com.gibson.analytics.client;

import java.util.List;

import com.gibson.analytics.core.ScoreboardProvider;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;

public interface BaseballAPI extends ScoreboardProvider {
	
	/**
	 * 
	 * @param gameDataDirectory
	 * @return
	 */
	public Lineup getLineup(String gameDataDirectory);
	
	/**
	 * Uses the MLB api to get the active roster.
	 * 
	 * @param teamId
	 * @return
	 */
	public List<Player> getRoster(String teamId);

}
