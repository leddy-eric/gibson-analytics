package com.gibson.analytics.client.hockey;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Scoreboard;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class HockeyScoreboardResponseExtractor implements ResponseExtractor<Scoreboard> {
	private final Logger log = LoggerFactory.getLogger(HockeyScoreboardResponseExtractor.class);
	private final LocalDate date;

	public HockeyScoreboardResponseExtractor(LocalDate date) {
		this.date = date;
	}

	@Override
	public Scoreboard extractData(ClientHttpResponse response) throws IOException {
		Scoreboard scoreboard = new Scoreboard();
		scoreboard.setStatus(response.getStatusCode());
		
		if(response.getStatusCode().is2xxSuccessful()){
			String pathExpression = "$.dates.[0].games[*]";
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

	private Game extractGameData(Map<String, ?> map) {
		Game game = new Game();
		
		HashMap<String, String> gameData = flattenMap("game", map, new HashMap<String,String>());
		// gameData.keySet().forEach(s -> log.debug("Key:" +s));
		log.debug("NHL Scoreboard: "+map.keySet().toString());
		
		game.setId(gameData.get("game.link"));
		game.setLeague("NHL");
		
		GameTeam away = new GameTeam();
		away.setCity(extractValue(gameData, "game.teams.away.team.name" ).get());
		away.setCode(extractValue(gameData, "game.teams.away.team.id" ).get());
		away.setName(extractValue(gameData, "game.teams.away.team.name" ).get());
		
		StringBuffer record = new StringBuffer();
		record.append(extractValue(gameData, "game.teams.away.leagueRecord.wins" ).get());
		record.append('-');
		record.append(extractValue(gameData, "game.teams.away.leagueRecord.losses" ).get());
		record.append('-');
		record.append(extractValue(gameData, "game.teams.away.leagueRecord.ot" ).get());
		away.setRecord(record.toString());
		
		GameTeam home = new GameTeam();
		home.setCity(extractValue(gameData, "game.teams.home.team.name" ).get());
		home.setCode(extractValue(gameData, "game.teams.home.team.id" ).get());
		home.setName(extractValue(gameData, "game.teams.home.team.name" ).get());
		record = new StringBuffer();
		record.append(extractValue(gameData, "game.teams.home.leagueRecord.wins" ).get());
		record.append('-');
		record.append(extractValue(gameData, "game.teams.home.leagueRecord.losses" ).get());
		record.append('-');
		record.append(extractValue(gameData, "game.teams.home.leagueRecord.ot" ).get());
		home.setRecord(record.toString());
		
		game.setHome(home);
		game.setAway(away);
		
		String gameTime = extractValue(gameData, "game.gameDate" ).get();
		
		Instant instant = OffsetDateTime.parse(gameTime).toInstant();
		LocalDateTime utcTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
		ZonedDateTime zonedTime = utcTime.atZone(ZoneId.of("America/New_York"));
		//dateTime.format(DateTimeFormatter.ofLocalizedTime(TimeStyle.));
		
		game.setUtc(utcTime.toString());
		game.setTime(zonedTime.toLocalTime().toString());
		
		return game;
	}

	@SuppressWarnings("rawtypes")
	private HashMap<String, String> flattenMap(String root, Map<String, ?> source, HashMap<String, String> target) {
		Set<String> keySet = source.keySet();
		
		for (String key : keySet) {
			Object value = source.get(key);
			if(HashMap.class.isAssignableFrom(value.getClass())) {
				flattenMap(root + "." + key, (HashMap) value, target);
			} else {
				String targetKey = root + "." + key;
				target.put(targetKey, value.toString());
			}
			
		}
		
		return target;
	}

	private Optional<String> extractValue(HashMap<?,?> map, String key) {
		Object value = map.get(key);
		if(value == null) {
			return Optional.empty();
		} 
		
		return Optional.of(value.toString());
	}

}
