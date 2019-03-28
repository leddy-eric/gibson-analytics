package com.gibson.analytics.init;

public interface CsvTeamConstants {
	public static final String COLUMN_NAME = "Team";
	public static final String COLUMN_STRIKEOUT_RATE="bullpenStrikeoutRate";
	public static final String COLUMN_WALK_RATE="bullpenWalkRate";
	public static final String COLUMN_WEIGHTED_OBA="bullpenwOBA";
	public static final String COLUMN_WEIGHTED_TEAM_OBA="wOBA";
	
	
	public static final String[] SUMMARY_HEADER = {
			COLUMN_NAME,
			COLUMN_STRIKEOUT_RATE,
			COLUMN_WALK_RATE,
			COLUMN_WEIGHTED_OBA,
			COLUMN_WEIGHTED_TEAM_OBA,
			"lineLearnedPF"
	};
}
