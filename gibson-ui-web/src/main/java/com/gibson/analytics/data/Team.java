package com.gibson.analytics.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * The Class Team.
 */
@Entity
public class Team implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	
	@OneToMany(mappedBy="teamId")
	private List<TeamStatistic> teamStatistics =  new ArrayList<>();
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the teamStatistics
	 */
	public List<TeamStatistic> getTeamStatistics() {
		return teamStatistics;
	}

	/**
	 * @param teamStatistics the teamStatistics to set
	 */
	public void setTeamStatistics(List<TeamStatistic> teamStatistics) {
		this.teamStatistics = teamStatistics;
	}

}
