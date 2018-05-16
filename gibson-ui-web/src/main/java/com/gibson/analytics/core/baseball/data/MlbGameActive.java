package com.gibson.analytics.core.baseball.data;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.gibson.analytics.data.Player;

@Entity
public class MlbGameActive implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -7532184326694016615L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private int battingOrder;
	
	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = false)
	@JoinColumn(name = "playerId")	
	private Player player;

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
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the battingOrder
	 */
	public int getBattingOrder() {
		return battingOrder;
	}

	/**
	 * @param battingOrder the battingOrder to set
	 */
	public void setBattingOrder(int battingOrder) {
		this.battingOrder = battingOrder;
	}
	
	
}
