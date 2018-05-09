package com.gibson.analytics.core.baseball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.repository.PlayerRepository;

@Component
public class MlbRandomLineupGenerator {
	final static Logger log = LoggerFactory.getLogger(MlbRandomLineupGenerator.class);
	
	// Cache
	ConcurrentHashMap<String, List<Player>> randomLineups = new ConcurrentHashMap<>();
	
	@Autowired
	private PlayerRepository repository;
	
	// TODO - Fix this
	private List<String> FIELD_TYPE_INFIELD = Arrays.asList("1B","2B","3B","SS","C");
	private List<String> FIELD_TYPE_OUTFIELD = Arrays.asList("LF","CF","RF", "OF");
	
	/**
	 * 
	 * @param team
	 * @return
	 */
	public List<Player> getRandomLineup(GameTeam team) {
		return getRandomLineup(team.getName());
	}
	
	/**
	 * 
	 * @param teamName
	 * @return
	 */
	public List<Player> getRandomLineup(String teamName) {
		if(!randomLineups.containsKey(teamName)) {
			List<Player> lineup = generateRandomLineup(teamName);
			randomLineups.put(teamName, lineup);
		}
		
		return randomLineups.get(teamName);
	}

	private List<Player> generateRandomLineup(String teamName) {
		log.info("Generate Lineup for "+ teamName);
		Map<String, List<Player>> positionMap = 
				repository
					.findByTeamAndStatus(teamName, "A")
					.stream()
					.collect(Collectors.groupingBy(Player::getPosition));
		
		return randomLineup(positionMap);
	}


	private Optional<Player> randomPlayer(String key, Map<String, List<Player>> positionMap) {
		List<Player> players = positionMap.get(key);
		if(players != null) {
			return players.stream().filter(p -> !p.getStatistics().isEmpty()).findAny();
		}
		
		return Optional.empty();
	}


	private List<Player> randomLineup(Map<String, List<Player>> positionMap) {
		List<Player> lineup = new ArrayList<>();

		lineup.addAll(randomPlayers(FIELD_TYPE_INFIELD, positionMap, 5));
		lineup.addAll(randomPlayers(FIELD_TYPE_OUTFIELD, positionMap, 3));
		
		Collections.shuffle(lineup);
		
		lineup.add(randomPlayer("P", positionMap).orElseThrow(IllegalStateException::new));
		
		return lineup;
	}

	private  List<Player> randomPlayers(List<String> positions, Map<String, List<Player>> map, int minimum) {
		List<Player> lineup = new ArrayList<>();
		List<Player> eligible = new ArrayList<>();
		
		for (String p : positions) {
			Optional<Player> player = randomPlayer(p, map);
			eligible.addAll(map.getOrDefault(p, Collections.emptyList()));
			
			if(player.isPresent()) {
				lineup.add(player.get());
			}
		}
		
		// Check for complete lineup
		int missing = minimum - lineup.size();
		if(missing > 0) {
			addRandomEligible(missing, lineup, eligible);
		}
		
		return lineup;
	}

	/**
	 * 
	 * @param missing
	 * @param lineup
	 * @param eligible
	 */
	private void addRandomEligible(int missing, List<Player> lineup, List<Player> eligible) {
		eligible.removeAll(lineup);
		
		for (int i = 0; i < missing; i++) {
			Optional<Player> any = eligible.stream().findAny();
			lineup.add(any.orElseThrow(IllegalStateException::new));
		}
		
	}

}
