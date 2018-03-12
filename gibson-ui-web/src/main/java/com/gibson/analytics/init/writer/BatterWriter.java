package com.gibson.analytics.init.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.gibson.analytics.data.Batter;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

public class BatterWriter implements ItemWriter<Batter> {
	private static final Logger log = LoggerFactory.getLogger(BatterWriter.class);
	
	private PlayerRepository playerRepository;
	private TeamRepository teamRepository;
	
	public BatterWriter(PlayerRepository playerRepository, TeamRepository teamRepository) {
		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
	}
	
	@Override
	public void write(List<? extends Batter> batters) throws Exception {
		Map<String, List<Batter>> teams = 
				batters.stream()
				.filter(b -> b.getPlayerId() != null)
				.collect(Collectors.groupingBy(Batter::getTeamId));
		
		teams.forEach((k,v) -> createTeam(k,v));
	}
	
	private void createTeam(String team, List<Batter> batters) {
		batters.sort((b1, b2) -> b1.getOrder().compareTo(b2.getOrder()));
		
		createLineup(team, batters);
	}

	private void createLineup(String name, List<Batter> batters) {
		List<Player> lineup = new ArrayList<>();
		Team team = new Team();
		
		team.setName(name);
		batters.forEach(b -> lineup.add(playerRepository.findById(b.getPlayerId()).get()));
		
		team.setLineup(lineup);
		teamRepository.save(team);
	}

}
