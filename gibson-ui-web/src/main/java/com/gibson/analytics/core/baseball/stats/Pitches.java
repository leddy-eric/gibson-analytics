package com.gibson.analytics.core.baseball.stats;

import java.math.BigDecimal;

public enum Pitches {
	RIGHT,
	LEFT,
	BULPEN;
	
	public static Pitches valueOf(BigDecimal value) {
		if(BigDecimal.ZERO.compareTo(value) > 0) {
			return RIGHT;
		}
		
		return LEFT;
	}
}
