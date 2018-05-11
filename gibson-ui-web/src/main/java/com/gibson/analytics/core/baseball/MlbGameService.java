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

	/**
	 * 
	 * @param game
	 * @return
	 */
	public MlbGame getGameDetails(Game game) {
		
		Lineup lineup = api.getLineup(game.getGameDataDirectory());
		
		if(lineupDataExists(lineup)) {
			return mergeApiLineup(game, lineup);
		} 		
		
		return createGameDetails(game);
	}

	private MlbGame createGameDetails(Game game) {
		MlbGame gameDetails = new MlbGame();
		
		gameDetails.setAway(lineupService.findActive(game.getAway()));
		gameDetails.setHome(lineupService.findActive(game.getHome()));
		
		return gameDetails;
	}

	/**
	 * Merge the latest API response into the cache in the line up service and return newly constructed MlbGame.
	 * 
	 * @param game
	 * @param lineup
	 * @return
	 */
	protected MlbGame mergeApiLineup(Game game, Lineup lineup) {
		MlbGame gameDetails = new MlbGame();
		
		MlbLineup away = cacheActiveLineup(game.getUtc(), game.getAway(), lineup.getAway());
		MlbLineup home = cacheActiveLineup(game.getUtc(), game.getHome(), lineup.getHome());
		
		gameDetails.setAway(away);
		gameDetails.setHome(home);
		
		return gameDetails;
	}

	private boolean lineupDataExists(Lineup lineup) {
		return lineup.getStatus().is2xxSuccessful();
	}

	private MlbLineup cacheActiveLineup(String gametime, GameTeam team, List<Player> lineup) {
		Assert.notNull(gametime, "MLBGameService requires a UTC Gametime");
		return lineupService.cacheLatestApiLineup(ZonedDateTime.parse(gametime), team, lineup);
	}
}
