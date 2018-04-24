package com.gibson.analytics.init;

public interface CsvPitcherConstants {

	public static final String COLUMN_NAME="Name";
	public static final String COLUMN_PLAYERID="playerid";
	public static final String COLUMN_PA="PA";
	public static final String COLUMN_SB="SB";
	public static final String COLUMN_CS="CS";
	public static final String COLUMN_PARKNORM_OBP_AGAINST = "ParkNormalizedOBPAgainst";
	public static final String COLUMN_PARKNORM_SLG_AGAINST = "ParkNormalizedSLGAgainst";
	public static final String COLUMN_IP_PER_GS = "IPperGS";
	public static final String COLUMN_GB_PERC = "GBPerc";
	public static final String COLUMN_RANK = "Rank";
	public static final String COLUMN_FACTOR = "Factor";



	public static final String[] HEADER = new String[] {			
			COLUMN_NAME,
			COLUMN_PLAYERID,
			COLUMN_PA,
			COLUMN_SB,
			COLUMN_CS,
			COLUMN_PARKNORM_OBP_AGAINST,
			COLUMN_PARKNORM_SLG_AGAINST,
			COLUMN_IP_PER_GS,
			COLUMN_GB_PERC};
	
	public static final String[] SUMMARY_HEADER = new String[] {			
			COLUMN_NAME,
			COLUMN_PARKNORM_OBP_AGAINST,
			COLUMN_PARKNORM_SLG_AGAINST,
			COLUMN_GB_PERC,
			COLUMN_RANK,
			COLUMN_FACTOR};
}
