package com.gibson.analytics.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * Represents the scoreboard data for the given date.
 *   
 * @author leddy.eric
 *
 */
public class Scoreboard {
	
	private LocalDate date;
	private HttpStatus status;
	private String league;
	private List<Game> games = new ArrayList<>();
	
	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	
	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
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
	 * @return the games
	 */
	public List<Game> getGames() {
		return games;
	}
	
	/**
	 * @param games the games to set
	 */
	public void setGames(List<Game> games) {
		this.games = games;
	}

	/**
	 * @return the league
	 */
	public String getLeague() {
		return league;
	}

	/**
	 * @param league the league to set
	 */
	public void setLeague(String league) {
		this.league = league;
	}

}
