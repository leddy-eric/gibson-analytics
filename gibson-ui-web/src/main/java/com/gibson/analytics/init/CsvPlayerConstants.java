package com.gibson.analytics.init;

public interface CsvPlayerConstants {

	public static final String COLUMN_PLAYERID="batter";
	public static final String COLUMN_STRIKEOUT_RATE="totalBatterStrikeoutRate";
	public static final String COLUMN_WALK_RATE="totalBatterWalkRate";
	public static final String COLUMN_WEIGHTED_OBA="totalBatterwOBA";
	public static final String COLUMN_BATS="bats";
	public static final String COLUMN_BSR="BsRperPA";
	public static final String COLUMN_DEF="DefperInn";
	
	
	public static final String[] SUMMARY_HEADER = new String[] {
			COLUMN_PLAYERID,
			COLUMN_STRIKEOUT_RATE,
			COLUMN_WALK_RATE,
			COLUMN_WEIGHTED_OBA,
			COLUMN_BATS,
			COLUMN_BSR,
			COLUMN_DEF};
}
