package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.init.CsvPitcherConstants;

@Component
public class LinearLineProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, Map<Player, Map<String, BigDecimal>> homeRoster, Map<Player, Map<String, BigDecimal>> awayRoster) {
		
		BigDecimal homePitcherRank = BigDecimal.valueOf(.5);

		
		Optional<Player> homePitcher = findByPosition(homeRoster, "P");
		if(homePitcher.isPresent()) {
			Player player = homePitcher.get();
			Map<String, BigDecimal> pitchingStats = homeRoster.get(player);
			homePitcherRank = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_RANK, homePitcherRank);         
		}



		// Create pitching defaults home
		BigDecimal awayPitcherRank = BigDecimal.valueOf(.5);

		Optional<Player> awayPitcher = findByPosition(awayRoster, "P");
		if(awayPitcher.isPresent()) {
			Player player = awayPitcher.get();
			Map<String, BigDecimal> pitchingStats = awayRoster.get(player);
			awayPitcherRank = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_RANK, awayPitcherRank);          
		}
		
		
		
		BigDecimal point535 = BigDecimal.valueOf(.535);
		BigDecimal line = homePitcherRank.subtract(awayPitcherRank)
				.add(point535);
		
		BigDecimal oneHundred = BigDecimal.valueOf(100);
		BigDecimal one = BigDecimal.valueOf(1);
		BigDecimal negativeOne = BigDecimal.valueOf(-1);

		if(line.compareTo(point535) != -1) {
			//line = -(line*100)/(1-line);
			line = line.multiply(oneHundred).divide(one.subtract(line),5,RoundingMode.HALF_UP).multiply(negativeOne);
		}
		if(line.compareTo(point535) == -1) {
			//line = (line*100)/(1-line);
			line = line.multiply(oneHundred).divide(one.subtract(line),5,RoundingMode.HALF_UP);

		}
		
		String outputLine = line.setScale(2,RoundingMode.HALF_DOWN).toString();

		return new GameStatistic("Linear Line", outputLine);
	}
	
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