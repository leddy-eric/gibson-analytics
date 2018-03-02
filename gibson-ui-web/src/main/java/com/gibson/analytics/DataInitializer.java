package com.gibson.analytics;

import com.gibson.analytics.init.CsvSource;

public interface DataInitializer {

	/**
	 * Load pitchers.
	 *
	 * @param source the source
	 */
	void loadPitchers(CsvSource source);

	/**
	 * Load players.
	 *
	 * @param source the source
	 */
	void loadPlayers(CsvSource source);
	
	/**
	 * Load NBA Team statistics
	 * 
	 * @param source
	 */
	void loadNbaTeams(CsvSource source);

}