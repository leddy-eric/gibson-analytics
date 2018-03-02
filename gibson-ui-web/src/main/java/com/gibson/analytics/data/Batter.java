package com.gibson.analytics.data;

import java.io.Serializable;

/**
 * The Class Batter.
 */
public class Batter implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	

	/** The team id. */
	private String teamId;
	
	/** The order. */
	private Integer order;
	
	/** The player id. */
	private Long playerId;

	/**
	 * Gets the team id.
	 *
	 * @return the teamId
	 */
	public String getTeamId() {
		return teamId;
	}

	/**
	 * Sets the team id.
	 *
	 * @param teamId the teamId to set
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 *
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @return the playerId
	 */
	public Long getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

}
