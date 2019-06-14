package com.gibson.analytics.core.baseball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gibson.analytics.core.baseball.data.MlbGameActive;
import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.data.MlbGameRoster;
import com.gibson.analytics.core.baseball.data.MlbGameStatus;
import com.gibson.analytics.core.baseball.repository.MlbGameDetailRepository;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;
import com.gibson.analytics.core.baseball.stats.StatisticFactory;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

@Component
public class MlbLineupService {
	final static Logger log = LoggerFactory.getLogger(MlbLineupService.class);
	
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	private MlbGameDetailRepository gameRepository; 
	
	/**
	 * 
	 * @param starterId
	 * @param team
	 * @param batterIds
	 * @return
	 */
	public MlbLineup constructLineup(String starterId, MlbTeamLookup team, String... batterIds) {
		MlbLineup lineup = new MlbLineup();
		lineup.setTeam(team.team());
		MlbPitcher mlbPitcher = constructStarter(starterId);
		lineup.setStartingPitcher(constructStarter(starterId));
		lineup.setBullpen(constructBullpen(mlbPitcher, team));
		lineup.setLineup(constructLineup(batterIds));
		
		return lineup;
	}
	
	public List<MlbGameActive> findLatestGameLineup(MlbTeamLookup team) {
		log.info("Find game lineup for: "+ team);
		Optional<MlbGameDetail> latest = gameRepository.findLatestByTeam(team).stream().findFirst();
		
		if(latest.isPresent()) {
			log.info("Found latest game :"+ latest.get().getApiId());
			
			MlbGameRoster away = latest.get().getAway();
			MlbGameRoster home = latest.get().getHome();
			
			if(team.equals(away.getTeam())) {
				log.info("Found away lineup");
				return away.getLineup();
			} else {
				log.info("Found home lineup");
				return home.getLineup();
			}
		}
		
		return new ArrayList<>();
	}

	private PitchingStatistics constructBullpen(MlbPitcher mlbPitcher, MlbTeamLookup lookup) {
		Team team = teamRepository.findById(lookup.team()).get();
		
		return StatisticFactory.bullpenFrom(team, mlbPitcher.getPlayer());
	}

	/**
	 * 
	 * @param batters
	 * @return
	 */
	private List<MlbPlayer> constructLineup(String[] batters) {
		return constructLineup(Arrays.asList(batters));
	}

	/**
	 * 
	 * @param batters
	 * @return
	 */
	private List<MlbPlayer> constructLineup(List<String> batters) {
		return batters
				.stream()
				.map(b -> Long.parseLong(b))
				.map(id -> constructPlayer(id))
				.map(p -> new MlbPlayer(p))
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private Player constructPlayer(long id) {
		return playerRepository.findById(id).orElse(new Player());
	}

	/**
	 * 
	 * @param playerId
	 * @return
	 */
	private MlbPitcher constructStarter(String playerId) {
		Optional<Player> starter = playerRepository.findById(Long.parseLong(playerId));
		
		if(starter.isPresent()) {
			return new MlbPitcher(starter.get());
		}
		
		// Returns league average starter
		return new MlbPitcher();
	}

}
