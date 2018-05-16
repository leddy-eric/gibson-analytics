package com.gibson.analytics.core;

import java.time.LocalDate;

import com.gibson.analytics.data.Scoreboard;

public interface ScoreboardProvider {

	/**
	 * Return a scoreboard object for todays games.
	 * 
	 * @return
	 */
	Scoreboard getScoreboard();

	/**
	 * Returns a list of 
	 * 
	 * @param date
	 * @return
	 */
	Scoreboard getScoreboard(LocalDate date);
	
	/**
	 * Returns the league that this scoreboard supports.
	 * 
	 * @return
	 */
	String getLeagueIdentifier();

}