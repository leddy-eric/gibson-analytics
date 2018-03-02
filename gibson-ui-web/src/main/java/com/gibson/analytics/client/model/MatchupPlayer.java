package com.gibson.analytics.client.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatchupPlayer {

	@XmlAttribute
	private String first;
	@XmlAttribute
	private String last;
	@XmlAttribute
	private String position;
	@XmlAttribute(name="bat_order")
	private Integer batting;
	
	/**
	 * @return the first
	 */
	public String getFirst() {
		return first;
	}
	
	/**
	 * @param first the first to set
	 */
	public void setFirst(String first) {
		this.first = first;
	}
	
	/**
	 * @return the last
	 */
	public String getLast() {
		return last;
	}
	
	/**
	 * @param last the last to set
	 */
	public void setLast(String last) {
		this.last = last;
	}
	
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

}
