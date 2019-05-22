package com.gibson.analytics.client.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatchupPlayer {

	@XmlAttribute
	private Long id;
	@XmlAttribute
	private String position;
	@XmlAttribute(name="game_position")
	private String gamePosition;
	@XmlAttribute(name="bat_order")
	private Integer batting;
	
	
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the batting
	 */
	public Integer getBatting() {
		return batting;
	}

	/**
	 * @param batting the batting to set
	 */
	public void setBatting(Integer batting) {
		this.batting = batting;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the gamePosition
	 */
	public String getGamePosition() {
		return gamePosition;
	}

	/**
	 * @param gamePosition the gamePosition to set
	 */
	public void setGamePosition(String gamePosition) {
		this.gamePosition = gamePosition;
	}

}
