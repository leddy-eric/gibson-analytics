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
public class LinearTotalProvider extends AbstractMlbGameStatsProvider {

	@Override
	public GameStatistic createStatistics(Game game, MlbLineup home, MlbLineup away) {
		// TODO Auto-generated method stub
		BigDecimal homeFactor = home.getPitcher().getFactor();
		BigDecimal awayFactor = away.getPitcher().getFactor();

		return new GameStatistic("Linear Total", createLinearTotal(game, homeFactor, awayFactor));
	}

	private String createLinearTotal(Game game, BigDecimal homePitcherFactor, BigDecimal awayPitcherFactor) {
		BigDecimal homefieldFactor = getHomeParkFactor(game);
		System.out.println(homefieldFactor);

		BigDecimal nine = BigDecimal.valueOf(9);
		BigDecimal gameTotal = homePitcherFactor.multiply(awayPitcherFactor)
				.multiply(homefieldFactor).multiply(nine);

		return gameTotal.setScale(2,RoundingMode.HALF_DOWN).toString();
	}

//	@Override
//	public GameStatistic createStatistics(Game game, Map<Player, Map<String, BigDecimal>> homeRoster, Map<Player, Map<String, BigDecimal>> awayRoster) {
//
//		BigDecimal homePitcherFactor = BigDecimal.valueOf(1);
//
//
//		Optional<Player> homePitcher = findByPosition(homeRoster, "P");
//		if(homePitcher.isPresent()) {
//			Player player = homePitcher.get();
//			Map<String, BigDecimal> pitchingStats = homeRoster.get(player);
//			homePitcherFactor = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_FACTOR, homePitcherFactor);         
//		}
//
//
//
//		// Create pitching defaults home
//		BigDecimal awayPitcherFactor = BigDecimal.valueOf(1);
//
//		Optional<Player> awayPitcher = findByPosition(awayRoster, "P");
//		if(awayPitcher.isPresent()) {
//			Player player = awayPitcher.get();
//			Map<String, BigDecimal> pitchingStats = awayRoster.get(player);
//			awayPitcherFactor = pitchingStats.getOrDefault(CsvPitcherConstants.COLUMN_FACTOR, awayPitcherFactor);          
//		}
//
//		BigDecimal homefieldFactor = getHomeParkFactor(game);
//		System.out.println(homefieldFactor);
//
//		BigDecimal nine = BigDecimal.valueOf(9);
//		BigDecimal gameTotal = homePitcherFactor.multiply(awayPitcherFactor)
//				.multiply(homefieldFactor).multiply(nine);
//
//		String outputTotal = gameTotal.setScale(2,RoundingMode.HALF_DOWN).toString();
//
//		return new GameStatistic("Linear Total", outputTotal);
//	}
//
//	private Optional<Player> findByPosition(Map<Player, Map<String, BigDecimal>> roster, String position) {
//		Set<Player> players = roster.keySet();
//		for (Player player : players) {
//			if(player.getPosition().equals(position)) {
//				return Optional.of(player);
//			}
//
//		}
//		return Optional.empty();
//
//	}



}