package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

/**
 * Enum for batters plate position.
 * 
 * @author leddy.eric
 *
 */
public enum Bats {
	RIGHT,
	LEFT,
	SWITCH;
	
	
	public static Bats valueOf(BigDecimal value) {
		if(BigDecimal.ZERO.compareTo(value) > 0) {
			return LEFT;
		} else if(BigDecimal.ZERO.compareTo(value) < 0) {
			return RIGHT;	
		}
		
		return Bats.SWITCH;
	}
}
