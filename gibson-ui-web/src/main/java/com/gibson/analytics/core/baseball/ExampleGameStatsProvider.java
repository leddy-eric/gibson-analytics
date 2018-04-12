package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Player;

@Component
public class ExampleGameStatsProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, Map<Player, Map<String, BigDecimal>> homeRoster, Map<Player, Map<String, BigDecimal>> awayRoster) {
		BigDecimal homeOBP = calculateTeamStatistic(homeRoster, "ParkNormalizedOBP",  new BigDecimal(.3));
		System.out.println("homeOBP: "+homeOBP);
		BigDecimal homeSLG = calculateTeamStatistic(homeRoster, "ParkNormalizedSLG",  new BigDecimal(.4));	
		System.out.println("homeSLG: "+homeSLG);
		BigDecimal awayOBP = calculateTeamStatistic(homeRoster, "ParkNormalizedOBP",  new BigDecimal(.3));
		System.out.println("awayOBP: "+awayOBP);
		BigDecimal awaySLG = calculateTeamStatistic(awayRoster, "ParkNormalizedSLG",  new BigDecimal(.4));

		return new GameStatistic("testOBP", awaySLG.toString());
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


}
