package com.gibson.analytics.core.baseball;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;

@Service
public class MlbGameService {
	final static Logger log = LoggerFactory.getLogger(MlbGameService.class);
	
	@Autowired
	private BaseballAPI api;
	
	@Autowired
	private MlbLineupService lineupService;

	public MlbGame getGameDetails(Game game) {
		MlbGame gameDetails = new MlbGame();
		
		Lineup lineup = api.getLineup(game.getGameDataDirectory());
		
		if(lineupDataExists(lineup)) {
			MlbLineup away = getActiveLineup(game.getUtc(), game.getAway(), lineup.getAway());
			MlbLineup home = getActiveLineup(game.getUtc(), game.getHome(), lineup.getHome());
			
			gameDetails.setAway(away);
			gameDetails.setHome(home);
		} else {
			MlbLineup away = lineupService.findActive(game.getAway());
			MlbLineup home = lineupService.findActive(game.getHome());
			
			gameDetails.setAway(away);
			gameDetails.setHome(home);
		}
		
		
		return gameDetails;
	}

	private boolean lineupDataExists(Lineup lineup) {
		return lineup.getStatus().is2xxSuccessful();
	}

	private MlbLineup getActiveLineup(String gametime, GameTeam team, List<Player> lineup) {
		Assert.notNull(gametime, "MLBGameService requires a UTC Gametime");
		return lineupService.findActiveLineup(ZonedDateTime.parse(gametime), team, lineup);
	}
}
