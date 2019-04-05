package com.gibson.analytics.core.baseball;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.client.baseball.MlbMetadata;
import com.gibson.analytics.client.model.MatchupPlayer;
import com.gibson.analytics.core.baseball.data.MlbGameActive;
import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.data.MlbGameRoster;
import com.gibson.analytics.core.baseball.data.MlbGameStatus;
import com.gibson.analytics.core.baseball.stats.StatisticFactory;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

@Service
public class MlbGameService {
	final static Logger log = LoggerFactory.getLogger(MlbGameService.class);

	@Autowired
	private MlbGameStatusService statusService;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;

	/**
	 * Returns the game from the existing game detail.
	 * 
	 * @param game
	 * @return
	 */
	public MlbGame getGame(MlbGameDetail detail) {
		MlbGame game = new MlbGame();
		
		Optional<MlbGameRoster> away = Optional.ofNullable(detail.getAway());
		
		if(away.isPresent()) {
			game.setAway(createLineup(away.get()));			
		} 
		
		Optional<MlbGameRoster> home = Optional.ofNullable(detail.getHome());
		
		if(home.isPresent()) {
			game.setHome(createLineup(home.get()));			
		}

		return game;		
	}
	
	private MlbLineup createLineup(MlbGameRoster roster) {
		MlbLineup lineup = new MlbLineup();
		Team team = teamRepository.findById(roster.getTeam().team()).get();

		List<MlbPlayer> batters = createLineup(roster.getLineup());
		lineup.setLineup(batters);
		
		Optional<MlbGameActive> starter = Optional.ofNullable(roster.getProbable());
		
		if(starter.isPresent()) {
			Player p = starter.get().getPlayer();
			MlbPitcher startingPitcher = new MlbPitcher(p);
			
			lineup.setStartingPitcher(startingPitcher);			
			lineup.setBullpen(StatisticFactory.bullpenFrom(team, p));

		} else {
			lineup.setStartingPitcher(new MlbPitcher());
			lineup.setBullpen(StatisticFactory.leagueAverageBullpen(MlbTeamLookup.lookupFromTeamName(team.getName())));
		}

		lineup.setTeam(roster.getTeam().name());

		return lineup;
	}

	/**
	 * Wrap the data.
	 * 
	 * @param lineup
	 * @return
	 */
	private List<MlbPlayer> createLineup(List<MlbGameActive> lineup) {
		return lineup.stream()
				.map(a -> new MlbPlayer(a.getPlayer()))
				.collect(Collectors.toList());
	}


	/**
	 * Handles the construction of a MlbGameDetails, the object returned is not a persistent entity.
	 * 
	 * @param game
	 * @param lineup
	 * @return
	 */
	public MlbGameDetail createGameDetails(Game game, Lineup lineup) {
		MlbGameStatus status = statusService.statusFromApiResults(game, lineup);

		MlbGameDetail detail = new MlbGameDetail();

		detail.setApiId(game.getId());
		detail.setStatus(status);

		Optional<String> utc = Optional.ofNullable(game.getUtc());
		if(utc.isPresent()) {
			ZonedDateTime zonedDateTime = 
					ZonedDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(utc.get()));
			
			detail.setGameDate(zonedDateTime.toLocalDate());			
		}

		detail.setAway(createTeamDetails(game.getAway(), lineup.getAway()));
		detail.setHome(createTeamDetails(game.getHome(), lineup.getHome()));

		return detail;
	}

	private MlbGameRoster createTeamDetails(GameTeam team, List<MatchupPlayer> lineup) {
		MlbGameRoster roster =  new MlbGameRoster();
		roster.setSource("DEFAULT");
		roster.setTeam(MlbTeamLookup.lookupFromTeamName(team.getName()));
		
		Optional<String> probable = Optional.ofNullable(team.getMetadata().get(MlbMetadata.KEY_STARTER));
		
		if(lineup != null && ! lineup.isEmpty()) {
			List<MlbGameActive> activeLineup = new ArrayList<>(); 
			roster.setSource("API");
			
			for (MatchupPlayer player : lineup) {
				Optional<Player> p = resolvePlayer(player, team.getName());
				
				if(p.isPresent()) {
					MlbGameActive active = new MlbGameActive();
					active.setBattingOrder(player.getBatting());
					active.setPlayer(p.get());
					activeLineup.add(active);
					
					if(player.getPosition().equals("P")) {
						MlbGameActive starter = new MlbGameActive();
						starter.setPlayer(p.get());
						roster.setProbable(starter);		
					}
				} 
			}
			
			roster.setLineup(activeLineup);
		} 
		
		if(probable.isPresent()) {
			Optional<Player> pitcher = playerRepository.findByName(probable.get());
			if(pitcher.isPresent()) {
				MlbGameActive active = new MlbGameActive();
				active.setPlayer(pitcher.get());
				
				// Set it only if a probable wasn't found in the lineup. lineup > probable.
				if(roster.getProbable() == null) {
					roster.setProbable(active);		
				}			
			}
		}	
		
		return roster;
	}
	
	/**
	 * Return the DB player if one exists, otherwise just use the API player.
	 * 
	 * @param player
	 * @return
	 */
	private Optional<Player> resolvePlayer(MatchupPlayer player, String team) {
		try {
			return playerRepository.findById(player.getId());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return Optional.empty();
	}

}
