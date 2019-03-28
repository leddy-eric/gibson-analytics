package com.gibson.analytics.init;

public interface CsvPitcherConstants {

	public static final String COLUMN_PLAYERID="pitcher";
	public static final String COLUMN_STRIKEOUT_RATE ="totalPitcherStrikeoutRate";
	public static final String COLUMN_WALK_RATE ="totalPitcherWalkRate";
	public static final String COLUMN_WEIGHTED_OBP="totalPitcherwOBA";
	public static final String COLUMN_THROWS ="throws";
	public static final String COLUMN_INN_PER_START="inningsPerStart";
	public static final String COLUMN_RANK="Rank";
	public static final String COLUMN_FACTOR="Factor";
	
	public static final String[] SUMMARY_HEADER = new String[] {			
			COLUMN_PLAYERID,
			COLUMN_STRIKEOUT_RATE,
			COLUMN_WALK_RATE,
			COLUMN_WEIGHTED_OBP,
			COLUMN_THROWS,
			COLUMN_INN_PER_START,
			COLUMN_RANK,
			COLUMN_FACTOR};
}
