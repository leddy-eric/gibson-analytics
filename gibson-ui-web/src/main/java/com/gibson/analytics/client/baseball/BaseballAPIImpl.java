package com.gibson.analytics.client.baseball;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.client.model.Matchup;
import com.gibson.analytics.client.model.MatchupPlayer;
import com.gibson.analytics.client.model.MatchupTeam;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.Scoreboard;
import com.gibson.analytics.enums.MlbTeamLookup;

@Component
public class BaseballAPIImpl implements BaseballAPI {
	private static final Logger log = LoggerFactory.getLogger(BaseballAPIImpl.class);
	
	// Host information 
    private final String HOSTNAME = "http://gd2.mlb.com";
    private final String RESOURCE_SCOREBOARD = "components/game/mlb/year_{year}/month_{month}/day_{day}/miniscoreboard.json";
	private final String RESOURCE_PLAYERS = "/players.xml";
    private final String HOSTNAME_LOOKUP = "http://mlb.mlb.com";
	private final String RESOURCE_ROSTER = "lookup/json/named.roster_40.bam";

    @Autowired
    private RestTemplate restTemplate;

	/**
	 * 
	 * @param gameDataDirectory
	 * @return
	 */
	private URI buildLineupUri(String gameDataDirectory) {
		return UriComponentsBuilder.fromHttpUrl(HOSTNAME)
				   .path(gameDataDirectory)
				   .path(RESOURCE_PLAYERS)
				   .build().toUri();
	}
	
	private URI buildRosterUri(String teamId) {
		URI uri = UriComponentsBuilder.fromHttpUrl(HOSTNAME_LOOKUP)
				   .path(RESOURCE_ROSTER)
				   .queryParam("team_id", teamId)
				   .build().toUri();
		log.debug("Roster URI: " +uri.toString());
		return uri;
	}
	
	/**
	 * Create the resource URI for the scoreboard resource.
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private URI buildScoreboardUri(int year, int month, int day) {
		return UriComponentsBuilder.fromHttpUrl(HOSTNAME)
								   .path(RESOURCE_SCOREBOARD)
								   .buildAndExpand(year, String.format("%02d", month) , String.format("%02d", day))
								   .toUri();
	}

	/**
	 * Create the resource URI for the scoreboard resource.
	 * 
	 * @param dateTime
	 * @return
	 */
	private URI buildScoreboardUri(LocalDate dateTime) {
		return buildScoreboardUri(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
	}

	@Override
	public String getLeagueIdentifier() {
		return "MLB";
	}

	@Override
	public Lineup getLineup(String gameDataDirectory) {
		log.debug("Getting lineup:"+ gameDataDirectory);
		Lineup lineup = new Lineup();
		
		try {
			ResponseEntity<Matchup> entity = restTemplate.getForEntity(buildLineupUri(gameDataDirectory), 
					Matchup.class);
			lineup = mapToLineup(entity);
		} catch (HttpClientErrorException e)   {
			// Return empty if not found
			if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
				lineup.setStatus(e.getStatusCode());
		    } else {
		        throw e;
		    }
		}

	    return lineup;
	}

	@Override
	public List<Player> getRoster(MlbTeamLookup team) {
		return restTemplate.execute(buildRosterUri(team.apiIdentifier()), HttpMethod.GET, null, 
				new BaseballRosterResponseExtractor());
	}

	@Override
	public Scoreboard getScoreboard() {		
		return this.getScoreboard(LocalDate.now());
	}

	@Override
	public Scoreboard getScoreboard(LocalDate date) {
		log.debug("Getting scoreboard:"+ date.format(DateTimeFormatter.ISO_LOCAL_DATE));
		
        Scoreboard scoreboard = 
        		restTemplate.execute(buildScoreboardUri(date), HttpMethod.GET, null, 
        				new BaseballScoreboardResponseExtractor());
		
		scoreboard.setDate(date);
		
		return scoreboard;
	}

	private void mapToLineup(Lineup lineup, MatchupTeam team) {
		List<Player> playersInLineup =	
				team.getPlayers().stream()
					.filter(p -> p.getBatting() != null)
					.sorted((p1, p2) -> p1.getBatting().compareTo(p2.getBatting()))
					.map(p -> mapToPlayer(p))
					.collect(Collectors.toList());
		
		if(team.getType().equals("away")){
			lineup.setAway(playersInLineup);
		} else {
			lineup.setHome(playersInLineup);
		}
	}
	
	private Player mapToPlayer(MatchupPlayer p) {
		Player player = new Player();
		player.setName(p.getFirst() + " " +p.getLast());
		player.setPosition(p.getPosition());
		return player;
	}

	private Lineup mapToLineup(ResponseEntity<Matchup> entity) {
		Lineup lineup = new Lineup();
		lineup.setStatus(entity.getStatusCode());
		
		if(entity.getStatusCode().is2xxSuccessful()) {
			Matchup matchup = entity.getBody();
			
			List<MatchupTeam> teams = matchup.getTeams();
			teams.forEach(team -> mapToLineup(lineup, team));	
		}
		
		return lineup;
	}

}
