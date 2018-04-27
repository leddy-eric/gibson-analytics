package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.init.CsvPitcherConstants;

@Component
public class ExampleGameStatsProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, Map<Player, Map<String, BigDecimal>> homeRoster, Map<Player, Map<String, BigDecimal>> awayRoster) {
		BigDecimal LeagueAverageOBP = BigDecimal.valueOf(.32);
		BigDecimal LeagueAverageSLG = BigDecimal.valueOf(.415);
		// These could change and be league dependent
		
		//BigDecimal OBP_Coef = BigDecimal.valueOf(.32); 
		double OBP_Coef = .32;
		//BigDecimal OBP_ex_Coef = BigDecimal.valueOf(3.07);
		double OBP_ex_Coef = 3.07;
		
		//BigDecimal SLG_Coef = BigDecimal.valueOf(10.56); // These could change and be league dependent
		//BigDecimal SLG_ex_Coef = BigDecimal.valueOf(1.46);
		double SLG_Coef = 10.56;
		double SLG_ex_Coef = 1.46;
		
		
		BigDecimal GDP_Coef = BigDecimal.valueOf(-.49); // 
		
		BigDecimal homeOBP = calculateTeamStatistic(homeRoster, "ParkNormalizedOBP",  new BigDecimal(.3));
		homeOBP = homeOBP.add(BigDecimal.valueOf(.006));
		System.out.println("homeOBP: "+homeOBP);
		
		BigDecimal homeSLG = calculateTeamStatistic(homeRoster, "ParkNormalizedSLG",  new BigDecimal(.4));	
		System.out.println("homeSLG: "+homeSLG);
		homeSLG = homeSLG.add(BigDecimal.valueOf(.008));

		BigDecimal awayOBP = calculateTeamStatistic(homeRoster, "ParkNormalizedOBP",  new BigDecimal(.3));
		System.out.println("awayOBP: "+awayOBP);
		awayOBP = awayOBP.subtract(BigDecimal.valueOf(.006));

		BigDecimal awaySLG = calculateTeamStatistic(awayRoster, "ParkNormalizedSLG",  new BigDecimal(.4));
		awaySLG = awaySLG.subtract(BigDecimal.valueOf(.008));

		BigDecimal homeDef = calculateNonWeightedTeamStatistic(homeRoster, "Def", new BigDecimal(0));
		BigDecimal homeBsR = calculateNonWeightedTeamStatistic(homeRoster, "BsR", new BigDecimal(0));

		BigDecimal awayDef = calculateNonWeightedTeamStatistic(awayRoster, "Def", new BigDecimal(0));
		BigDecimal awayBsR = calculateNonWeightedTeamStatistic(awayRoster, "BsR", new BigDecimal(0));

		// Create pitching defaults home

		BigDecimal homePitcherOBP = BigDecimal.valueOf(.33);
		BigDecimal homePitcherSLG = BigDecimal.valueOf(.45);
		BigDecimal homePitcherGb = BigDecimal.valueOf(.4);

		Optional<Player> homePitcher = findByPosition(homeRoster, "P");
		if(homePitcher.isPresent()) {
			Player player = homePitcher.get();
			Map<String, BigDecimal> pitchingStats = homeRoster.get(player);
			homePitcherOBP = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_OBP_AGAINST, homePitcherOBP);
			homePitcherSLG = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_SLG_AGAINST, homePitcherSLG);
			homePitcherGb = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_GB_PERC, homePitcherGb);          
		}



		// Create pitching defaults home
		BigDecimal awayPitcherOBP = BigDecimal.valueOf(.33);
		BigDecimal awayPitcherSLG = BigDecimal.valueOf(.45);
		BigDecimal awayPitcherGb = BigDecimal.valueOf(.4);
		Optional<Player> awayPitcher = findByPosition(awayRoster, "P");
		if(homePitcher.isPresent()) {
			Player player = awayPitcher.get();
			Map<String, BigDecimal> pitchingStats = awayRoster.get(player);
			awayPitcherOBP = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_OBP_AGAINST, awayPitcherOBP);
			awayPitcherSLG = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_PARKNORM_SLG_AGAINST, awayPitcherSLG);
			awayPitcherGb = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_GB_PERC, awayPitcherGb);           
		}
		
		BigDecimal homeEffectiveOBPvsStarter = homeOBP.multiply(awayPitcherOBP)
														.divide(LeagueAverageOBP,5,RoundingMode.HALF_UP);
		
		BigDecimal homeEffectiveSLGvsStarter = homeSLG.multiply(awayPitcherSLG)
				.divide(LeagueAverageSLG,5,RoundingMode.HALF_UP);
		
		
		double homeRunsFromOBPvsStarter = Math.pow(OBP_Coef*homeEffectiveOBPvsStarter.doubleValue(), OBP_ex_Coef)*9/9; // When I bring in innings I can fix the first 9
		double homeRunsFromSLGvsStarter = Math.pow(SLG_Coef*homeEffectiveSLGvsStarter.doubleValue(), SLG_ex_Coef)*9/9;
		double homeRunsFromBsR = homeBsR.doubleValue()/162;
		double homeRunsRemovedFromDoublePlays = awayPitcherGb.doubleValue()*GDP_Coef.doubleValue();
		
		double homeRuns = homeRunsFromOBPvsStarter +
				homeRunsFromSLGvsStarter +
				homeRunsFromBsR +
				homeRunsRemovedFromDoublePlays;
		
		
		
		BigDecimal awayEffectiveOBPvsStarter = awayOBP.multiply(homePitcherOBP)
				.divide(LeagueAverageOBP,5,RoundingMode.HALF_UP);

		BigDecimal awayEffectiveSLGvsStarter = awaySLG.multiply(homePitcherSLG)
				.divide(LeagueAverageSLG,5,RoundingMode.HALF_UP);

		
		double awayRunsFromOBPvsStarter = Math.pow(OBP_Coef*awayEffectiveOBPvsStarter.doubleValue(), OBP_ex_Coef)*9/9; // When I bring in innings I can fix the first 9
		double awayRunsFromSLGvsStarter = Math.pow(SLG_Coef*awayEffectiveSLGvsStarter.doubleValue(), SLG_ex_Coef)*9/9;
		double awayRunsFromBsR = awayBsR.doubleValue()/162;
		double awayRunsRemovedFromDoublePlays = homePitcherGb.doubleValue()*GDP_Coef.doubleValue();
		
		double awayRuns = awayRunsFromOBPvsStarter +
				awayRunsFromSLGvsStarter +
				awayRunsFromBsR +
				awayRunsRemovedFromDoublePlays;
		
		double lineExp = Math.pow((awayRuns + homeRuns),.284); // .284 will not change
		double line = 0;
		if(homeRuns >= awayRuns) {
			line = Math.pow(homeRuns,lineExp)/(Math.pow(awayRuns, lineExp) + Math.pow(homeRuns,lineExp));
			line = -(line*100)/(1-line);
		}
		if(awayRuns > homeRuns) {
		    line = Math.pow(awayRuns,lineExp)/(Math.pow(awayRuns, lineExp) + Math.pow(homeRuns,lineExp));
			line = (line*100)/(1-line);
		}
		BigDecimal outputLine = BigDecimal.valueOf(line);
		String outputString = outputLine.setScale(0,RoundingMode.HALF_DOWN).toString();
		return new GameStatistic("testLine", outputString);
	}

	/**
	 * Roles up a player statistic using the position in the lineup to adjust the weighted contribution,
	 * 
	 * @param roster - the team lineup
	 * @param statKey - the name of the stat to rollup
	 * @param statDefault - the default value to use if the stat does not exist
	 * 
	 * @return teamStat - sum of all the weighted statistics.
	 */
	private BigDecimal calculateTeamStatistic(Map<Player, Map<String, BigDecimal>> roster, String statKey, BigDecimal statDefault) {
		Set<Player> players = roster.keySet();
		
		// Variables
		BigDecimal teamStat = new BigDecimal(0);
		BigDecimal contribution = BigDecimal.valueOf(4.8);
		
		for (Player player : players) {
			// Adjust for order
			contribution = contribution.subtract(BigDecimal.valueOf(.1));
			
			BigDecimal weighted = roster.get(player)
								   .getOrDefault(statKey, statDefault)
								   .multiply(contribution)
								   .divide(BigDecimal.valueOf(38.7), RoundingMode.HALF_DOWN);
			teamStat = teamStat.add(weighted);
		}
		
		return teamStat.setScale(5, RoundingMode.HALF_UP);
	}
	
	
	private BigDecimal calculateNonWeightedTeamStatistic(Map<Player, Map<String, BigDecimal>> roster, String statKey, BigDecimal statDefault) {
		Set<Player> players = roster.keySet();
		
		// Variables
		BigDecimal teamStat = new BigDecimal(0);
		//BigDecimal playerStat = BigDecimal.valueOf(4.8);
		
		for (Player player : players) {
			// Adjust for order
			//contribution = contribution.subtract(BigDecimal.valueOf(.1));
			
			BigDecimal playerStat = roster.get(player)
								   .getOrDefault(statKey, statDefault);
			teamStat = teamStat.add(playerStat);
		}
		
		return teamStat.setScale(5, RoundingMode.HALF_UP);
	}
	
	  /**

     * Find player by position.

     *

      * @param roster

     * @param position

     * @return

     */

	private Optional<Player> findByPosition(Map<Player, Map<String, BigDecimal>> roster, String position) {
		Set<Player> players = roster.keySet();
		for (Player player : players) {
			if(player.getPosition().equals(position)) {
				return Optional.of(player);
			}

		}
		return Optional.empty();

	}
	


}
