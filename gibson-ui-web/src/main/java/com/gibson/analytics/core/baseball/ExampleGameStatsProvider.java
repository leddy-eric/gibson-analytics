package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

@Component
public class ExampleGameStatsProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, Map<Integer, Map<String, BigDecimal>> homeRoster, Map<Integer, Map<String, BigDecimal>> awayRoster) {		
		Map<String, BigDecimal> firstHomeBatter = homeRoster.get(new Integer(1));
		Map<String, BigDecimal> secondHomeBatter = homeRoster.get(new Integer(2));
		Map<String, BigDecimal> thirdHomeBatter = homeRoster.get(new Integer(3));
		Map<String, BigDecimal> fourthHomeBatter = homeRoster.get(new Integer(4));
		Map<String, BigDecimal> fifthHomeBatter = homeRoster.get(new Integer(5));
		Map<String, BigDecimal> sixthHomeBatter = homeRoster.get(new Integer(6));
		Map<String, BigDecimal> seventhHomeBatter = homeRoster.get(new Integer(7));
		Map<String, BigDecimal> eighthHomeBatter = homeRoster.get(new Integer(8));
		Map<String, BigDecimal> ninthHomeBatter = homeRoster.get(new Integer(9));
		
		//Getting OBP for all 9 HomeBatters
		BigDecimal HOBP1 = firstHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP2 = secondHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP3 = thirdHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP4 = fourthHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP5 = fifthHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP6 = sixthHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP7 = seventhHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP8 = eighthHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal HOBP9 = ninthHomeBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		
		//Calculating the weighted average contribution each player has
		BigDecimal HOBPcontrib1 = HOBP1.multiply(BigDecimal.valueOf(4.7)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib2 = HOBP2.multiply(BigDecimal.valueOf(4.6)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib3 = HOBP3.multiply(BigDecimal.valueOf(4.5)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib4 = HOBP4.multiply(BigDecimal.valueOf(4.4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib5 = HOBP5.multiply(BigDecimal.valueOf(4.3)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib6 = HOBP6.multiply(BigDecimal.valueOf(4.2)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib7 = HOBP7.multiply(BigDecimal.valueOf(4.1)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib8 = HOBP8.multiply(BigDecimal.valueOf(4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HOBPcontrib9 = HOBP9.multiply(BigDecimal.valueOf(3.9)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);

		//add it all up for team OBP
		BigDecimal homeOBP = HOBPcontrib1.add(HOBPcontrib2).add(HOBPcontrib3).add(HOBPcontrib4).add(HOBPcontrib5).add(HOBPcontrib6).add(HOBPcontrib7).add(HOBPcontrib8).add(HOBPcontrib9);
		System.out.println("First HomeBatter FinalOBP " + HOBPcontrib1);

		//System.out.println("First HomeBatter FinalOBP " + firstHomeBatter.get("FinalOBP"));
		homeOBP = homeOBP.setScale(5, RoundingMode.HALF_UP);
		
		
		//Getting SLG for all 9 HomeBatters
		BigDecimal HSLG1 = firstHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG2 = secondHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG3 = thirdHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG4 = fourthHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG5 = fifthHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG6 = sixthHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG7 = seventhHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG8 = eighthHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal HSLG9 = ninthHomeBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		
		//Calculating the weighted average contribution each player has for SLG
		BigDecimal HSLGcontrib1 = HSLG1.multiply(BigDecimal.valueOf(4.7)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib2 = HSLG2.multiply(BigDecimal.valueOf(4.6)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib3 = HSLG3.multiply(BigDecimal.valueOf(4.5)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib4 = HSLG4.multiply(BigDecimal.valueOf(4.4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib5 = HSLG5.multiply(BigDecimal.valueOf(4.3)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib6 = HSLG6.multiply(BigDecimal.valueOf(4.2)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib7 = HSLG7.multiply(BigDecimal.valueOf(4.1)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib8 = HSLG8.multiply(BigDecimal.valueOf(4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal HSLGcontrib9 = HSLG9.multiply(BigDecimal.valueOf(3.9)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);

		//add it all up for team OBP
		BigDecimal homeSLG = HSLGcontrib1.add(HSLGcontrib2).add(HSLGcontrib3).add(HSLGcontrib4).add(HSLGcontrib5).add(HSLGcontrib6).add(HSLGcontrib7).add(HSLGcontrib8).add(HSLGcontrib9);
		System.out.println("First HomeBatter FinalSLG " + HSLGcontrib1);

		//System.out.println("First HomeBatter FinalOBP " + firstHomeBatter.get("FinalOBP"));
		homeSLG = homeSLG.setScale(5, RoundingMode.HALF_UP);
		
		
		
		
		
		Map<String, BigDecimal> firstAwayBatter = awayRoster.get(new Integer(1));
		Map<String, BigDecimal> secondAwayBatter = awayRoster.get(new Integer(2));
		Map<String, BigDecimal> thirdAwayBatter = awayRoster.get(new Integer(3));
		Map<String, BigDecimal> fourthAwayBatter = awayRoster.get(new Integer(4));
		Map<String, BigDecimal> fifthAwayBatter = awayRoster.get(new Integer(5));
		Map<String, BigDecimal> sixthAwayBatter = awayRoster.get(new Integer(6));
		Map<String, BigDecimal> seventhAwayBatter = awayRoster.get(new Integer(7));
		Map<String, BigDecimal> eighthAwayBatter = awayRoster.get(new Integer(8));
		Map<String, BigDecimal> ninthAwayBatter = awayRoster.get(new Integer(9));
		
		//Getting OBP for all 9 AwayBatters
		BigDecimal AOBP1 = firstAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP2 = secondAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP3 = thirdAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP4 = fourthAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP5 = fifthAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP6 = sixthAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP7 = seventhAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP8 = eighthAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		BigDecimal AOBP9 = ninthAwayBatter.getOrDefault("ParkNormalizedOBP", new BigDecimal(.3));
		
		//Calculating the weighted average contribution each player has
		BigDecimal AOBPcontrib1 = AOBP1.multiply(BigDecimal.valueOf(4.7)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib2 = AOBP2.multiply(BigDecimal.valueOf(4.6)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib3 = AOBP3.multiply(BigDecimal.valueOf(4.5)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib4 = AOBP4.multiply(BigDecimal.valueOf(4.4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib5 = AOBP5.multiply(BigDecimal.valueOf(4.3)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib6 = AOBP6.multiply(BigDecimal.valueOf(4.2)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib7 = AOBP7.multiply(BigDecimal.valueOf(4.1)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib8 = AOBP8.multiply(BigDecimal.valueOf(4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal AOBPcontrib9 = AOBP9.multiply(BigDecimal.valueOf(3.9)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);

		//add it all up for team OBP
		BigDecimal awayOBP = AOBPcontrib1.add(AOBPcontrib2).add(AOBPcontrib3).add(AOBPcontrib4).add(AOBPcontrib5).add(AOBPcontrib6).add(AOBPcontrib7).add(AOBPcontrib8).add(AOBPcontrib9);
		System.out.println("First AwayBatter FinalOBP " + AOBPcontrib1);

		//System.out.println("First AwayBatter FinalOBP " + firstAwayBatter.get("FinalOBP"));
		awayOBP = awayOBP.setScale(5, RoundingMode.HALF_UP);
		
		
		//Getting SLG for all 9 AwayBatters
		BigDecimal ASLG1 = firstAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG2 = secondAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG3 = thirdAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG4 = fourthAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG5 = fifthAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG6 = sixthAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG7 = seventhAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG8 = eighthAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		BigDecimal ASLG9 = ninthAwayBatter.getOrDefault("ParkNormalizedSLG", new BigDecimal(.4));
		
		//Calculating the weighted average contribution each player has for SLG
		BigDecimal ASLGcontrib1 = ASLG1.multiply(BigDecimal.valueOf(4.7)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib2 = ASLG2.multiply(BigDecimal.valueOf(4.6)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib3 = ASLG3.multiply(BigDecimal.valueOf(4.5)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib4 = ASLG4.multiply(BigDecimal.valueOf(4.4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib5 = ASLG5.multiply(BigDecimal.valueOf(4.3)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib6 = ASLG6.multiply(BigDecimal.valueOf(4.2)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib7 = ASLG7.multiply(BigDecimal.valueOf(4.1)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib8 = ASLG8.multiply(BigDecimal.valueOf(4)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);
		BigDecimal ASLGcontrib9 = ASLG9.multiply(BigDecimal.valueOf(3.9)).divide(BigDecimal.valueOf(38.7),RoundingMode.HALF_DOWN);

		//add it all up for team OBP
		BigDecimal awaySLG = ASLGcontrib1.add(ASLGcontrib2).add(ASLGcontrib3).add(ASLGcontrib4).add(ASLGcontrib5).add(ASLGcontrib6).add(ASLGcontrib7).add(ASLGcontrib8).add(ASLGcontrib9);
		System.out.println("First AwayBatter FinalSLG " + ASLGcontrib1);

		//System.out.println("First HomeBatter FinalOBP " + firstHomeBatter.get("FinalOBP"));
		awaySLG = awaySLG.setScale(5, RoundingMode.HALF_UP);
		
		
		return new GameStatistic("testOBP", awaySLG.toString());
		//return new GameStatistic("rosterSize", Integer.toString(homeRoster.size()));
	}


}
