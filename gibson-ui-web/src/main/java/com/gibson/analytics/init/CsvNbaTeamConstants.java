package com.gibson.analytics.init;

public interface CsvNbaTeamConstants {
	public static final String COLUMN_TEAM = "Team";
	public static final String COLUMN_SCORE = "Score";
	public static final String COLUMN_MOVING_AVERAGE = "MovingAverage";
	public static final String COLUMN_SPREAD_SCORE = "SpreadScore";
	public static final String COLUMN_SPREAD_SCORE_MOVING_AVERAGE = "MovingAverageSpreadScore";
	public static final String COLUMN_VALUE_SCORE = "ValueScore";
	public static final String COLUMN_VALUE_SCORE_MOVING_AVERAGE = "MovingAverageValueScore";
	
	public static final String[] HEADER = new String[] {
			COLUMN_TEAM,
			COLUMN_SCORE,
			COLUMN_MOVING_AVERAGE,
			COLUMN_SPREAD_SCORE,
			COLUMN_SPREAD_SCORE_MOVING_AVERAGE,
			COLUMN_VALUE_SCORE,
			COLUMN_VALUE_SCORE_MOVING_AVERAGE
	};
}
