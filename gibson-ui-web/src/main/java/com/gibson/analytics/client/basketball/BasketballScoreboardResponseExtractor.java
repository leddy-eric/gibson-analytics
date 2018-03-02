package com.gibson.analytics.client.basketball;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Scoreboard;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class BasketballScoreboardResponseExtractor implements ResponseExtractor<Scoreboard> {
	private final Logger log = LoggerFactory.getLogger(BasketballScoreboardResponseExtractor.class);
	private final LocalDate date;

	public BasketballScoreboardResponseExtractor(LocalDate date) {
		this.date = date;
	}

	@Override
	public Scoreboard extractData(ClientHttpResponse response) throws IOException {
		Scoreboard scoreboard = new Scoreboard();
		scoreboard.setStatus(response.getStatusCode());
		
		if(response.getStatusCode().is2xxSuccessful()){
			String pathExpression = "$..g[?(@.gdte == '"+ date + "')]";
			log.debug("Read response for " + pathExpression);
		
			List<Map<String, Object>> games = 
					JsonPath.parse(response.getBody(), Configuration.defaultConfiguration()).read(pathExpression);
			
			for (Map<String, Object> map : games) {
				if(map != null) {
					Game game = extractGameData(map);
					scoreboard.getGames().add(game);	
				} else {
					log.info("Scoreboard has null elements");
				}
			}
		}
		
		return scoreboard;
	}

	private Game extractGameData(Map<String, Object> map) {
		Game game = new Game();
		
		log.debug("NBA Scoreboard: "+map.keySet().toString());
		
		game.setLeague("NBA");
		game.setId(map.get("gid").toString());
		
		game.setHome(extractTeamData(map.get("h")));
		game.setAway(extractTeamData(map.get("v")));
		game.setTime(map.get("stt").toString());
		
		String utc = map.get("gdtutc").toString()+ "T" + map.get("utctm").toString();
		game.setUtc(utc );
		
		
		return game;
	}

	@SuppressWarnings("rawtypes")
	private GameTeam extractTeamData(Object map) {
		GameTeam team = new GameTeam();
		
		if(HashMap.class.isAssignableFrom(map.getClass())) {
			
			Optional<String> city = extractValue((HashMap) map, "tc");
			Optional<String> record = extractValue((HashMap) map, "re");
			Optional<String> code = extractValue((HashMap) map, "ta");
			Optional<String> name = extractValue((HashMap) map, "tn");
	
			log.debug("City "+ city);
			log.debug("Record "+ record);
			log.debug("Code "+ code);
			log.debug("Name "+ name);
			
			team.setCity(city.get());
			team.setCode(code.get());
			team.setName(name.get());
			team.setRecord(record.get());
		}
		
		return team;
	}

	private Optional<String> extractValue(HashMap<?,?> map, String key) {
		// TODO Auto-generated method stub
		Object value = map.get(key);
		if(value == null) {
			return Optional.empty();
		} 
		
		return Optional.of(value.toString());
	}

}
