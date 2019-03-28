package com.gibson.analytics.core.baseball.data;

public enum MlbGameStatus {
	REFRESH,
	// Game with no probables or lineups
	OPEN,
	// Game has probables
	ESTIMATED,
	// Game has lineups and probables
	RECOMMEND,
	// Game is over
	FINAL	
}
