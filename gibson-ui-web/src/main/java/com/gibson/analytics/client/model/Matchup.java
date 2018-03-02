package com.gibson.analytics.client.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="game")
@XmlAccessorType(XmlAccessType.FIELD)
public class Matchup {
	
	@XmlAttribute
	private String venue;
	@XmlAttribute
	private String date;
	
	@XmlElement(name="team")
	private List<MatchupTeam> teams;
	

	/**
	 * @return the venue
	 */
	public String getVenue() {
		return venue;
	}

	/**
	 * @param venue the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
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
	 * @return the teams
	 */
	public List<MatchupTeam> getTeams() {
		return teams;
	}

	/**
	 * @param teams the teams to set
	 */
	public void setTeams(List<MatchupTeam> teams) {
		this.teams = teams;
	}

}
