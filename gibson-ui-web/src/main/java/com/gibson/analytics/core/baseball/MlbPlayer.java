package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.init.CsvPitcherConstants;
import com.gibson.analytics.init.CsvPlayerConstants;

/**
 * Wrapper class for Player, adds convenience methods for accessing statistic values.
 * 
 * @author leddy.eric
 *
 */
public class MlbPlayer implements CsvPlayerConstants, CsvPitcherConstants {
	final static Logger log = LoggerFactory.getLogger(MlbPlayer.class);

	private static final String POSITION_PITCHER = "P";

	private int order;
	private Player player;
	private Map<String, BigDecimal> map = new HashMap<>();

	/**
	 * Create an MlbPlayer with the specified order in the lineup.
	 * 
	 * @param player
	 * @param order
	 */
	MlbPlayer(Player player, int order) {
		this.player = player;
		this.order = order;

		if(this.player.getStatistics() != null) {
			this.player.getStatistics().stream().forEach(s -> addStatistic(s));
		}
	}

	protected BigDecimal getStatisticOrDefault(String statisticName, double value) {
		return map.getOrDefault(statisticName, BigDecimal.valueOf(value));
	}

	/**
	 * Calculates the weighted statistic by order
	 * 
	 * @param name - Statistic name
	 * @param defaultValue - The value to uise in the calculation when no statistic is present
	 * @return WeightedStatistic as a BigDecimal
	 */
	protected BigDecimal calculateWeightedStatistic(String name, double defaultValue) {
		double weight = 4.8 - (.1 * order);

		return getStatisticOrDefault(name, defaultValue)
				.multiply(BigDecimal.valueOf(weight))
				.divide(BigDecimal.valueOf(38.7), RoundingMode.HALF_DOWN);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPitcher() {
		return POSITION_PITCHER.equals(player.getPosition());
	}

	/**
	 * Add a statistic.
	 * 
	 * @param s
	 */
	private void addStatistic(PlayerStatistic s) {
		map.put(s.getName(), s.getValue());
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
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

}
