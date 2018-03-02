package com.gibson.analytics.init;

public interface CsvLineupConstants {
	
	public static final String COLUMN_PLAYER_NAME = "Name";
	public static final String COLUMN_BATTING_ORDER = "BattingOrder";
	public static final String COLUMN_TEAM = "Team";
	public static final String COLUMN_PLAYER_ID = "playerId";

	public static final String[] HEADER = new String[] {
			COLUMN_PLAYER_NAME,
			COLUMN_BATTING_ORDER,
			COLUMN_TEAM,
			COLUMN_PLAYER_ID
	};
}
