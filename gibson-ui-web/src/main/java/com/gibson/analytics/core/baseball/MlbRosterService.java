package com.gibson.analytics.core.baseball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.data.Player;
import com.google.common.base.Optional;

@Service
public class MlbRosterService {

	@Autowired
	private BaseballAPI api;
	
	// TODO - we really should get this by the MLB api, but I don't know how yet
	List<String> teamIds = Arrays.asList( "112", "134", "138", "142", "117", "147", "111", "108", "158", "143", 
										  "120",  "145", "137", "114", "115", "146", "116", "121", "110", "140", 
										  "141", "139",  "133", "135", "144", "113", "118", "119", "136","109");

	private List<Player> allPlayers = new ArrayList<>();
	private Map<String, List<Player>> cachedRosters = new HashMap<>();

	
	/**
	 * This is dumb.
	 * 
	 * @param position
	 * @return
	 */
	public List<Player> findPlayersByPosition(String position) {
		Optional<String> query = Optional.of(position);
		
		// Full rosters
		List<Player> allPlayers = 
				cachedRosters
					.values()
					.stream()
					.flatMap(List::stream)
					.collect(Collectors.toList());
		
		return allPlayers.stream()
				.filter(p -> query.or("?").equals(p.getPosition()))
				.collect(Collectors.toList());
		
	}
	
	/**
	 * 
	 * @param teamName
	 * @return
	 */
	public List<Player> findRosterByTeam(String teamName) {
		return cachedRosters.get(teamName);
	}
	
	@PostConstruct
	public void init() {
		// Clean up
		allPlayers.clear();
		cachedRosters.clear();
		
		for (String teamId : teamIds) {
			allPlayers.addAll(api.getRoster(teamId));
		}
		
		cachedRosters = allPlayers.stream().collect(Collectors.groupingBy(Player::getTeam));
		cachedRosters.keySet().forEach(System.out::println);
	}
}
