package com.gibson.analytics.client;

import java.time.LocalDateTime;

import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Scoreboard;

public interface BaseballAPI {
	
	/**
	 * Return a scoreboard object for todays games.
	 * 
	 * @return
	 */
	public Scoreboard getScoreboard();
	
	/**
	 * Returns a list of 
	 * 
	 * @param date
	 * @return
	 */
	public Scoreboard getScoreboard(LocalDateTime datetime);
	
	/**
	 * 
	 * @param gameDataDirectory
	 * @return
	 */
	public Lineup getLineup(String gameDataDirectory);

}
