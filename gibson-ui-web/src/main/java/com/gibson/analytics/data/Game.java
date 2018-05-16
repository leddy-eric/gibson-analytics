package com.gibson.analytics.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author leddy.eric
 *
 */
public class Game {
	
	private String id;
	
	private String league;
	
	private GameTeam home;
	private GameTeam away;
	
	private String time;
	private String utc;
	private String status;
	private String gameDataDirectory;

	
	private List<GameStatistic> gameStatistics = new ArrayList<>();
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the gameDataDirectory
	 */
	public String getGameDataDirectory() {
		return gameDataDirectory;
	}

	/**
	 * @param gameDataDirectory the gameDataDirectory to set
	 */
	public void setGameDataDirectory(String gameDataDirectory) {
		this.gameDataDirectory = gameDataDirectory;
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

	/**
	 * @return the home
	 */
	public GameTeam getHome() {
		return home;
	}

	/**
	 * @param home the home to set
	 */
	public void setHome(GameTeam home) {
		this.home = home;
	}

	/**
	 * @return the away
	 */
	public GameTeam getAway() {
		return away;
	}

	/**
	 * @param away the away to set
	 */
	public void setAway(GameTeam away) {
		this.away = away;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the gameStatistics
	 */
	public List<GameStatistic> getGameStatistics() {
		return gameStatistics;
	}

	/**
	 * @param gameStatistics the gameStatistics to set
	 */
	public void setGameStatistics(List<GameStatistic> gameStatistics) {
		this.gameStatistics = gameStatistics;
	}

	/**
	 * @return the utc
	 */
	public String getUtc() {
		return utc;
	}

	/**
	 * @param utc the utc to set
	 */
	public void setUtc(String utc) {
		this.utc = utc;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	

}
