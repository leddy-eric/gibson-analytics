package com.gibson.analytics.client.odds;

public class Odds {
	
	private String id;
	private String homeTeam;
	private String homeTeamCode;
	private String awayTeam;
	private String awayTeamCode;
	private String date;
	private String total;
	private String spread;

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
	 * @return the homeTeam
	 */
	public String getHomeTeam() {
		return homeTeam;
	}

	/**
	 * @param homeTeam the homeTeam to set
	 */
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	/**
	 * @return the awayTeam
	 */
	public String getAwayTeam() {
		return awayTeam;
	}

	/**
	 * @param awayTeam the awayTeam to set
	 */
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the spread
	 */
	public String getSpread() {
		return spread;
	}

	/**
	 * @param spread the spread to set
	 */
	public void setSpread(String spread) {
		this.spread = spread;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the homeTeamCode
	 */
	public String getHomeTeamCode() {
		return homeTeamCode;
	}

	/**
	 * @param homeTeamCode the homeTeamCode to set
	 */
	public void setHomeTeamCode(String homeTeamCode) {
		this.homeTeamCode = homeTeamCode;
	}

	/**
	 * @return the awayTeamCode
	 */
	public String getAwayTeamCode() {
		return awayTeamCode;
	}

	/**
	 * @param awayTeamCode the awayTeamCode to set
	 */
	public void setAwayTeamCode(String awayTeamCode) {
		this.awayTeamCode = awayTeamCode;
	}

}
