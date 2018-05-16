package com.gibson.analytics.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.gibson.analytics.client.model.MatchupPlayer;

public class Lineup {
	
	private HttpStatus status;
	private List<MatchupPlayer> home = new ArrayList<>();
	private List<MatchupPlayer> away = new ArrayList<>();
	
	/**
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	/**
	 * @return the home
	 */
	public List<MatchupPlayer> getHome() {
		return home;
	}

	/**
	 * @param home the home to set
	 */
	public void setHome(List<MatchupPlayer> home) {
		this.home = home;
	}

	/**
	 * @return the away
	 */
	public List<MatchupPlayer> getAway() {
		return away;
	}

	/**
	 * @param away the away to set
	 */
	public void setAway(List<MatchupPlayer> away) {
		this.away = away;
	}
}
