package com.gibson.analytics.core.baseball;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

/**
 * Roster service builds out the active rosters published by the API.
 * 
 * @author leddy.eric
 *
 */
@Service
public class MlbRosterService {
	final static Logger log = LoggerFactory.getLogger(MlbRosterService.class);

	@Autowired
	private BaseballAPI api;
	
	@Autowired
	private PlayerRepository repository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	public Lineup findActiveLineup(Game game) {
		return api.getLineup(game.getGameDataDirectory());
	}


	@PostConstruct
	public void init() {
		for (MlbTeamLookup mlbTeam : MlbTeamLookup.values()) {
			Team team = new Team();
			team.setName(mlbTeam.team());
			teamRepository.save(team);
			
			repository.saveAll(api.getRoster(mlbTeam));
		}
	}
}
