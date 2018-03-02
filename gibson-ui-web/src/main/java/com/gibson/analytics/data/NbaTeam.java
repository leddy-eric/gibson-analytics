package com.gibson.analytics.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @author leddy.eric
 *
 */
@Entity
public class NbaTeam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String name;
	
	@Column(nullable = false)
	private String score;

	@Column(nullable = false)
	private String scoreMovingAverage;
	
	@Column(nullable = false)
	private String spreadScore;
	
	@Column(nullable = false)
	private String spreadScoreMovingAverage;
	
	@Column(nullable = false)
	private String valueScore;
	
	@Column(nullable = false)
	private String valueScoreMovingAverage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScoreMovingAverage() {
		return scoreMovingAverage;
	}

	public void setScoreMovingAverage(String scoreMovingAverage) {
		this.scoreMovingAverage = scoreMovingAverage;
	}

	public String getSpreadScore() {
		return spreadScore;
	}

	public void setSpreadScore(String spreadScore) {
		this.spreadScore = spreadScore;
	}

	public String getSpreadScoreMovingAverage() {
		return spreadScoreMovingAverage;
	}

	public void setSpreadScoreMovingAverage(String spreadScoreMovingAverage) {
		this.spreadScoreMovingAverage = spreadScoreMovingAverage;
	}

	public String getValueScore() {
		return valueScore;
	}

	public void setValueScore(String valueScore) {
		this.valueScore = valueScore;
	}

	public String getValueScoreMovingAverage() {
		return valueScoreMovingAverage;
	}

	public void setValueScoreMovingAverage(String valueScoreMovingAverage) {
		this.valueScoreMovingAverage = valueScoreMovingAverage;
	}


} 