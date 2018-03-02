package com.gibson.analytics.client.odds;

import java.util.List;
import java.util.Map;

public interface OddsAPI {

	/**
	 * Gets the name ID pairs from the odds API, subsequent calls to get the tournament odds should use the tournament ID
	 * @return
	 */
	Map<String, String> getTournamentMapping();

	/**
	 * Returns all events where the API provides odds.
	 * 
	 * @param tournamentId - the ID return from the original map
	 * @return
	 */
	List<Odds> getOddsEvents(String tournamentId);

}