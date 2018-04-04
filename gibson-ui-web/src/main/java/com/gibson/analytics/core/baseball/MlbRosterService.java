package com.gibson.analytics.core.baseball;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.repository.PlayerRepository;

@Service
public class MlbRosterService {
	final static Logger log = LoggerFactory.getLogger(MlbRosterService.class);

	@Autowired
	private BaseballAPI api;
	
	@Autowired
	private PlayerRepository repository;

	/**
	 * 
	 * 
	 * @param position
	 * @return
	 */
	public Lineup findActiveLineup(Game game) {
		Lineup lineup = api.getLineup(game.getGameDataDirectory());
		
		if(gameDataExists(lineup)) {
			return mapApiResponseToFullPlayerData(lineup);
		}
		
		return createActiveLineup(game);
	}
	

	/**
	 * The data from the api does not have the full data needed.
	 * 
	 * @param lineup
	 * @return
	 */
	private Lineup mapApiResponseToFullPlayerData(Lineup lineup) {
		Lineup fullLineup = new Lineup();
		List<Player> awayLineup = mapLineupToFullPlayerData(lineup.getAway());
		List<Player> homeLineup = mapLineupToFullPlayerData(lineup.getHome());
		
		fullLineup.setAway(awayLineup);
		fullLineup.setHome(homeLineup);
		fullLineup.setStatus(lineup.getStatus());
		
		return fullLineup;
	}


	/**
	 * Use the API lineup to lookup the DB information.
	 * 
	 * @param lineup
	 * @return
	 */
	private List<Player> mapLineupToFullPlayerData(List<Player> lineup) {
		List<Player> fullLineup = new ArrayList<>();
		
		for (Player player : lineup) {
			fullLineup.add(resolvePlayer(player));
		}
		
		return fullLineup;
	}


	/**
	 * Return the DB player if one exists, otherwise just use the API player.
	 * 
	 * @param player
	 * @return
	 */
	private Player resolvePlayer(Player player) {
		try {
			return repository.findByName(player.getName()).orElse(player);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return player;
	}


	/**
	 * Validate the return from the api.
	 * 
	 * @param lineup
	 * @return
	 */
	private boolean gameDataExists(Lineup lineup) {
		return lineup.getStatus().is2xxSuccessful();
	}


	/**
	 * 
	 * @param game
	 * @return
	 */
	private Lineup createActiveLineup(Game game) {
		Lineup lineup = new Lineup();
		List<Player> awayLineup = createActiveLineup(game.getAway());
		List<Player> homeLineup = createActiveLineup(game.getHome());
		
		lineup.setAway(awayLineup);
		lineup.setHome(homeLineup);
		
		lineup.setStatus(HttpStatus.NO_CONTENT);
		
		return lineup;
	}


	private List<Player> createActiveLineup(GameTeam team) {
		return repository.findByTeamAndStatus(team.getName(), "A");
	}


	@PostConstruct
	public void init() {
		for (MlbTeamLookup team : MlbTeamLookup.values()) {
			repository.saveAll(api.getRoster(team));
		}
	}
}
