package com.gibson.analytics.init;

public interface CsvPlayerConstants {

	public static final String COLUMN_TEAM="Team";
	public static final String COLUMN_PLAYERID="playerid";
	public static final String COLUMN_NAME="Name";
	public static final String COLUMN_PA="PA";
	public static final String COLUMN_HR="HR";
	public static final String COLUMN_R="R";
	public static final String COLUMN_RBI="RBI";
	public static final String COLUMN_BBPERC="BBPerc";
	public static final String COLUMN_KPERC="KPerc";
	public static final String COLUMN_BABIP="Babip";
	public static final String COLUMN_OBP="OBP";
	public static final String COLUMN_SLG="SLG";
	public static final String COLUMN_SB="SB";
	public static final String COLUMN_CS="CS";
	public static final String COLUMN_SPD="Spd";
	public static final String COLUMN_BSR="BsR";
	public static final String COLUMN_DEF="Def";
	public static final String COLUMN_LD="LD";
	public static final String COLUMN_GB="GB";
	public static final String COLUMN_FB="FB";
	public static final String COLUMN_SOFT="Soft";
	public static final String COLUMN_MED="Med";
	public static final String COLUMN_HARD="Hard";
	public static final String COLUMN_HRPERFB="HRperFB";
	public static final String COLUMN_PULL="Pull";
	public static final String COLUMN_OSWING="OSwing";
	public static final String COLUMN_ZSWING="ZSwing";
	public static final String COLUMN_PAVSL="PAVsL";
	public static final String COLUMN_HRVSL="HRVsL";
	public static final String COLUMN_RVSL="RVsL";
	public static final String COLUMN_RBIVSL="RBIVsL";
	public static final String COLUMN_BBPERCVSL="BBPercVsL";
	public static final String COLUMN_KPERCVSL="KPercVsL";
	public static final String COLUMN_BABIPVSL="BabipVsL";
	public static final String COLUMN_OBPVSL="OBPVsL";
	public static final String COLUMN_SLGVSL="SLGVsL";
	public static final String COLUMN_LDVSL="LDVsL";
	public static final String COLUMN_GBVSL="GBVsL";
	public static final String COLUMN_FBVSL="FBVsL";
	public static final String COLUMN_HRPERFBVSL="HRperFBVsL";
	public static final String COLUMN_SOFTVSL="SoftVsL";
	public static final String COLUMN_MEDVSL="MedVsL";
	public static final String COLUMN_HARDVSL="HardVsL";
	public static final String COLUMN_PAVSR="PAVsR";
	public static final String COLUMN_HRVSR="HRVsR";
	public static final String COLUMN_RVSR="RVsR";
	public static final String COLUMN_RBIVSR="RBIVsR";
	public static final String COLUMN_BBPERCVSR="BBPercVsR";
	public static final String COLUMN_KPERCVSR="KPercVsR";
	public static final String COLUMN_BABIPVSR="BabipVsR";
	public static final String COLUMN_OBPVSR="OBPVsR";
	public static final String COLUMN_SLGVSR="SLGVsR";
	public static final String COLUMN_LDVSR="LDVsR";
	public static final String COLUMN_GBVSR="GBVsR";
	public static final String COLUMN_FBVSR="FBVsR";
	public static final String COLUMN_HRPERFBVSR="HRperFBVsR";
	public static final String COLUMN_SOFTVSR="SoftVsR";
	public static final String COLUMN_MEDVSR="MedVsR";
	public static final String COLUMN_HARDVSR="HardVsR";
	public static final String COLUMN_STEPFUNCTIONPULLPERC="StepFunctionPullPerc";
	public static final String COLUMN_EXPECTEDBABIP="ExpectedBabip";
	public static final String COLUMN_EXPECTEDBABIPVSR="ExpectedBabipVsR";
	public static final String COLUMN_EXPECTEDBABIPVSL="ExpectedBabipVsL";
	public static final String COLUMN_EXPECTEDHRPERFB="ExpectedHRperFB";
	public static final String COLUMN_EXPECTEDHRPERFBVSR="ExpectedHRperFBVsR";
	public static final String COLUMN_EXPECTEDHRPERFBVSL="ExpectedHRperFBVsL";
	public static final String COLUMN_FINALOBP="FinalOBP";
	public static final String COLUMN_FINALSLG="FinalSLG";
	public static final String COLUMN_FINALOBPVSR="FinalOBPVsR";
	public static final String COLUMN_FINALSLGVSR="FinalSLGVsR";
	public static final String COLUMN_FINALOBPVSL="FinalOBPVsL";
	public static final String COLUMN_FINALSLGVSL="FinalSLGVsL";
	public static final String COLUMN_PARKBABIP="ParkBabip";
	public static final String COLUMN_PARKOBP="ParkOBP";
	public static final String COLUMN_PARKSLG="ParkSLG";
	public static final String COLUMN_MEANHRFACTOR="meanHRFactor";
	public static final String COLUMN_OBPFACTOR="OBPFactor";
	public static final String COLUMN_SLGFACTOR="SLGFactor";
	public static final String COLUMN_EFFECTIVEPLAYEROBPFACTOR="EffectivePlayerOBPFactor";
	public static final String COLUMN_EFFECTIVEPLAYERSLGFACTOR="EffectivePlayerSLGFactor";
	public static final String COLUMN_EFFECTIVEPLAYERHRFACTOR="EffectivePlayerHRFactor";
	public static final String COLUMN_PARKNORMALIZEDOBP="ParkNormalizedOBP";
	public static final String COLUMN_PARKNORMALIZEDSLG="ParkNormalizedSLG";
	public static final String COLUMN_PARKNORMALIZEDOBPVSR="ParkNormalizedOBPVsR";
	public static final String COLUMN_PARKNORMALIZEDSLGVSR="ParkNormalizedSLGVsR";
	public static final String COLUMN_PARKNORMALIZEDOBPVSL="ParkNormalizedOBPVsL";
	public static final String COLUMN_PARKNORMALIZEDSLGVSL="ParkNormalizedSLGVsL";


	public static final String[] HEADER = new String[] {COLUMN_TEAM,
			COLUMN_PLAYERID,
			COLUMN_NAME,
			COLUMN_PA,
			COLUMN_HR,
			COLUMN_R,
			COLUMN_RBI,
			COLUMN_BBPERC,
			COLUMN_KPERC,
			COLUMN_BABIP,
			COLUMN_OBP,
			COLUMN_SLG,
			COLUMN_SB,
			COLUMN_CS,
			COLUMN_SPD,
			COLUMN_BSR,
			COLUMN_DEF,
			COLUMN_LD,
			COLUMN_GB,
			COLUMN_FB,
			COLUMN_SOFT,
			COLUMN_MED,
			COLUMN_HARD,
			COLUMN_HRPERFB,
			COLUMN_PULL,
			COLUMN_OSWING,
			COLUMN_ZSWING,
			COLUMN_PAVSL,
			COLUMN_HRVSL,
			COLUMN_RVSL,
			COLUMN_RBIVSL,
			COLUMN_BBPERCVSL,
			COLUMN_KPERCVSL,
			COLUMN_BABIPVSL,
			COLUMN_OBPVSL,
			COLUMN_SLGVSL,
			COLUMN_LDVSL,
			COLUMN_GBVSL,
			COLUMN_FBVSL,
			COLUMN_HRPERFBVSL,
			COLUMN_SOFTVSL,
			COLUMN_MEDVSL,
			COLUMN_HARDVSL,
			COLUMN_PAVSR,
			COLUMN_HRVSR,
			COLUMN_RVSR,
			COLUMN_RBIVSR,
			COLUMN_BBPERCVSR,
			COLUMN_KPERCVSR,
			COLUMN_BABIPVSR,
			COLUMN_OBPVSR,
			COLUMN_SLGVSR,
			COLUMN_LDVSR,
			COLUMN_GBVSR,
			COLUMN_FBVSR,
			COLUMN_HRPERFBVSR,
			COLUMN_SOFTVSR,
			COLUMN_MEDVSR,
			COLUMN_HARDVSR,
			COLUMN_STEPFUNCTIONPULLPERC,
			COLUMN_EXPECTEDBABIP,
			COLUMN_EXPECTEDBABIPVSR,
			COLUMN_EXPECTEDBABIPVSL,
			COLUMN_EXPECTEDHRPERFB,
			COLUMN_EXPECTEDHRPERFBVSR,
			COLUMN_EXPECTEDHRPERFBVSL,
			COLUMN_FINALOBP,
			COLUMN_FINALSLG,
			COLUMN_FINALOBPVSR,
			COLUMN_FINALSLGVSR,
			COLUMN_FINALOBPVSL,
			COLUMN_FINALSLGVSL,
			COLUMN_PARKBABIP,
			COLUMN_PARKOBP,
			COLUMN_PARKSLG,
			COLUMN_MEANHRFACTOR,
			COLUMN_OBPFACTOR,
			COLUMN_SLGFACTOR,
			COLUMN_EFFECTIVEPLAYEROBPFACTOR,
			COLUMN_EFFECTIVEPLAYERSLGFACTOR,
			COLUMN_EFFECTIVEPLAYERHRFACTOR,
			COLUMN_PARKNORMALIZEDOBP,
			COLUMN_PARKNORMALIZEDSLG,
			COLUMN_PARKNORMALIZEDOBPVSR,
			COLUMN_PARKNORMALIZEDSLGVSR,
			COLUMN_PARKNORMALIZEDOBPVSL,
			COLUMN_PARKNORMALIZEDSLGVSL};
	
	public static final String[] SUMMARY_HEADER = new String[] {
			COLUMN_NAME,
			COLUMN_PARKNORMALIZEDOBP,
			COLUMN_PARKNORMALIZEDSLG,
			COLUMN_BSR,
			COLUMN_DEF};
}
