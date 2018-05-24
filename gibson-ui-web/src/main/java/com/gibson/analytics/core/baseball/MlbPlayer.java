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
	
	private MlbPlayer(int order, String name, String position, BigDecimal bsr, BigDecimal def, BigDecimal obp, BigDecimal slg) {		
		this(new Player(), order);
		setPlayerPosition(position);
		setPlayerName(name);
		addStatistic(CsvPlayerConstants.COLUMN_BSR, bsr);
		addStatistic(CsvPlayerConstants.COLUMN_DEF, def);
		addStatistic(CsvPlayerConstants.COLUMN_PARKNORMALIZEDOBP, obp);
		addStatistic(CsvPlayerConstants.COLUMN_PARKNORMALIZEDSLG, slg);
	}

	private void setPlayerName(String name) {
		this.player.setName(name);		
	}

	private void setPlayerPosition(String position) {
		this.player.setPosition(position);
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
	 * Calculates the weighted statistic by order
	 * 
	 * @param name - Statistic name
	 * @param defaultValue - The value to use in the calculation when no statistic is present
	 * @param totalWeight - The value to divide by based on the depth of the lineup
	 * 
	 * @return WeightedStatistic as a BigDecimal
	 */
	protected BigDecimal calculateWeightedStatistic(String name, double defaultValue, double totalWeight) {
		double weight = 4.8 - (.1 * order);
		
		BigDecimal decimal = getStatisticOrDefault(name, defaultValue);

		BigDecimal s = 
				decimal
				.multiply(BigDecimal.valueOf(weight))
				.divide(BigDecimal.valueOf(totalWeight), RoundingMode.HALF_DOWN);
		
		return s;
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
		addStatistic(s.getName(), s.getValue());
	}
	
	/**
	 * Add a statistic.
	 * 
	 * @param s
	 */
	private void addStatistic(String name, BigDecimal value) {
		map.put(name, value);
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
	
	protected static final class Builder {
		private int order;
		private String name;
		private String position;
		private BigDecimal bsr = BigDecimal.valueOf(0);
		private BigDecimal def = BigDecimal.valueOf(0);
		private BigDecimal obp = BigDecimal.valueOf(0);
		private BigDecimal slg = BigDecimal.valueOf(0);
		
		public Builder add(int order, String name, String position) {
			this.order = order;
			this.name = name;
			this.position = position;
			return this;
		}
		
		public Builder bsr(double value) {
			this.bsr = BigDecimal.valueOf(value);
			return this;
		}
		
		public Builder def(double value) {
			this.def = BigDecimal.valueOf(value);
			return this;
		}
		
		public Builder obp(double value) {
			this.obp = BigDecimal.valueOf(value);
			return this;
		}
		
		public Builder slg(double value) {
			this.slg = BigDecimal.valueOf(value);
			return this;
		}

		public MlbPlayer build() {
			return new MlbPlayer(this.order, this.name, this.position, this.bsr, this.def, this.obp, this.slg);
		}
	}

}
