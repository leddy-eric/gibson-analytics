/*
 * 
 */
package com.gibson.analytics.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * The Class Player.
 */
@Entity
public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column
	private String team;
	
	@OneToMany(mappedBy="playerId")
	private List<PlayerStatistic> playerStatistics;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Sets the team.
	 *
	 * @param team the new team
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * Gets the team.
	 *
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * @return the statistics
	 */
	public List<PlayerStatistic> getStatistics() {
		return playerStatistics;
	}

	/**
	 * @param playerStatistics the playerStatistics to set
	 */
	public void setStatistics(List<PlayerStatistic> playerStatistics) {
		this.playerStatistics = playerStatistics;
	}
}
