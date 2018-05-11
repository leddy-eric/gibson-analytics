package com.gibson.analytics.core.baseball;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.repository.PlayerRepository;

/**
 * This is turning into a bit of a mess, the whole baseball package needs to be refactored.
 * 
 * @author leddy.eric
 *
 */
@Service
public class MlbLineupService {
	final static Logger log = LoggerFactory.getLogger(MlbLineupService.class);

	@Autowired
	private PlayerRepository repository;

	@Autowired
	private MlbRandomLineupGenerator lineupGenerator;

	private ConcurrentHashMap<String, MlbLineup> active = new ConcurrentHashMap<>();

	/**
	 * Constructs MLBLineup and caches the results into the active line up cache.
	 * 
	 * @param gametime
	 * @param team
	 * @param apiLineup
	 */
	public MlbLineup cacheLatestApiLineup(ZonedDateTime gametime, GameTeam team, List<Player> apiLineup) {
		MlbLineup lineup = resolveLineup(gametime, team, apiLineup);

		return active.merge(team.getName(), lineup, (oldLineup, newLineup) -> {
			if(oldLineup.getGametime().isBefore(newLineup.getGametime())) {
				log.debug("Update lineup: " + newLineup.getTeam());
				return newLineup;
			} 

			return oldLineup;
		});

	}

	public MlbLineup findActive(GameTeam team) {
		if(active.containsKey(team.getName())) {
			MlbLineup latest = active.get(team.getName());
			
			return constructActiveLineup(latest, team);
		}

		return constructRandomLineup(team);
	}

	/**
	 * 
	 * @param latest
	 * @param team
	 * @return
	 */
	private MlbLineup constructActiveLineup(MlbLineup latest, GameTeam team) {
		// TODO Auto-generated method stub
		MlbLineup active = new MlbLineup();
		
		active.setTeam(latest.getTeam());
		active.setLineup(latest.getLineup());
		
		MlbPitcher startingPitcher = findProbableStarter(team);
		active.setStartingPitcher(startingPitcher);
		
		return active;
	}

	private MlbPitcher findProbableStarter(GameTeam team) {
		if(team.getMetadata().containsKey("starter")) {
			String starter = team.getMetadata().get("starter");
			Optional<Player> probable = repository.findByName(starter);
			
			if(probable.isPresent()) {
				return new MlbPitcher(probable.get());
			}
		}
		
		return new MlbPitcher();
	}

	private MlbLineup constructRandomLineup(GameTeam team) {
		log.debug("Generate Random lineup: " + team.getName());
		List<Player> randomLineup = lineupGenerator.getRandomLineup(team);
		return resolveLineup(ZonedDateTime.now(), team, randomLineup);
	}

	/**
	 * Construct the MlbLineup object for the given team and api lineup.
	 * @param gametime 
	 * 
	 * @param team
	 * @param apiLineup
	 * @return
	 */
	private MlbLineup resolveLineup(ZonedDateTime gametime, GameTeam team, List<Player> apiLineup) {
		MlbLineup lineup = new MlbLineup();

		lineup.setLineup(resolveAll(apiLineup));
		lineup.setTeam(team.getName());
		lineup.setGametime(gametime);

		return lineup;
	}

	/**
	 * 
	 * @param apiLineup
	 * @return
	 */
	private List<MlbPlayer> resolveAll(List<Player> apiLineup) {
		List<MlbPlayer> lineup = new ArrayList<>();
		int order = 1;

		for (Player player : apiLineup) {
			lineup.add(new MlbPlayer(resolvePlayer(player), order++));
		}

		return lineup;
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

}
