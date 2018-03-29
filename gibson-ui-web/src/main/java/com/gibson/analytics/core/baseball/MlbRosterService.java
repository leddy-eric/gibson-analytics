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
import com.gibson.analytics.enums.MlbTeamLookup;
import com.google.common.base.Optional;

@Service
public class MlbRosterService {

	@Autowired
	private BaseballAPI api;

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

		for (MlbTeamLookup team : MlbTeamLookup.values()) {
			allPlayers.addAll(api.getRoster(team));
		}
		
		cachedRosters = allPlayers.stream().collect(Collectors.groupingBy(Player::getTeam));
	}
}
