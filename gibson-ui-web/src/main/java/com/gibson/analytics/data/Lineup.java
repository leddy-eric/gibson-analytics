package com.gibson.analytics.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class Lineup {
	
	private HttpStatus status;
	private List<Player> home = new ArrayList<>();
	private List<Player> away = new ArrayList<>();
	
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
	public List<Player> getHome() {
		return home;
	}

	/**
	 * @param home the home to set
	 */
	public void setHome(List<Player> home) {
		this.home = home;
	}

	/**
	 * @return the away
	 */
	public List<Player> getAway() {
		return away;
	}

	/**
	 * @param away the away to set
	 */
	public void setAway(List<Player> away) {
		this.away = away;
	}
}
