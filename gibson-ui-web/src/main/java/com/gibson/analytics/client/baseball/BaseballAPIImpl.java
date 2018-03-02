package com.gibson.analytics.client.baseball;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.client.model.Matchup;
import com.gibson.analytics.client.model.MatchupTeam;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Scoreboard;

@Component
public class BaseballAPIImpl implements BaseballAPI {
	private static final Logger log = LoggerFactory.getLogger(BaseballAPIImpl.class);
	
	// Host information 
    private final String HOSTNAME = "http://gd2.mlb.com";
    private final String RESOURCE_SCOREBOARD = "components/game/mlb/year_{year}/month_{month}/day_{day}/miniscoreboard.json";
	private final String RESOURCE_PLAYERS = "/players.xml";

    @Autowired
    private RestTemplate restTemplate;

	@Override
	public Scoreboard getScoreboard() {		
		return this.getScoreboard(LocalDateTime.now());
	}
	
	@Override
	public Scoreboard getScoreboard(LocalDateTime datetime) {
		log.debug("Getting scoreboard:"+ datetime.format(DateTimeFormatter.ISO_LOCAL_DATE));
		
        Scoreboard scoreboard = 
        		restTemplate.execute(buildScoreboardUri(datetime), HttpMethod.GET, null, 
        				new BaseballScoreboardResponseExtractor());
		
		scoreboard.setDate(datetime.toLocalDate());
		
		return scoreboard;
	}
	
	@Override
	public Lineup getLineup(String gameDataDirectory) {
		log.debug("Getting lineup:"+ gameDataDirectory);
		
		ResponseEntity<Matchup> entity = restTemplate.getForEntity(buildLineupUri(gameDataDirectory), 
				Matchup.class);
		
		return mapToLineup(entity);
	}


	private Lineup mapToLineup(ResponseEntity<Matchup> entity) {
		Lineup lineup = new Lineup();
		lineup.setStatus(entity.getStatusCode());
		
		if(entity.getStatusCode().is2xxSuccessful()){
			log.error("Could not find an active lineup...." + entity.getStatusCode());
			Matchup matchup = entity.getBody();
			
			List<MatchupTeam> teams = matchup.getTeams();
			teams.forEach(team -> mapToLineup(lineup, team));	
		}
		
		return lineup;
	}

	private void mapToLineup(Lineup lineup, MatchupTeam team) {
		List<String> playersInLineup =	
				team.getPlayers().stream()
					.filter(p -> p.getBatting() != null)
					.sorted((p1, p2) -> p1.getBatting().compareTo(p2.getBatting()))
					.map(p -> p.getPosition()+" - " +p.getFirst()+" "+p.getLast())
					.collect(Collectors.toList());
		
		if(team.getType().equals("away")){
			lineup.setAway(playersInLineup);
		} else {
			lineup.setHome(playersInLineup);
		}
	}

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

	/**
	 * Create the resource URI for the scoreboard resource.
	 * 
	 * @param dateTime
	 * @return
	 */
	private URI buildScoreboardUri(LocalDateTime dateTime) {
		return buildScoreboardUri(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
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

}
