package com.gibson.analytics.client.baseball;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.client.ResponseExtractor;

import com.gibson.analytics.core.SupportedLeagues;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Scoreboard;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@Component
public class BaseballScoreboardResponseExtractor implements ResponseExtractor<Scoreboard> {
	private static final String HOME = "home";
	private static final String AWAY = "away";
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Scoreboard extractData(ClientHttpResponse response) throws IOException {
		Scoreboard scoreboard = new Scoreboard();
		scoreboard.setStatus(response.getStatusCode());
		
		if(response.getStatusCode().is2xxSuccessful()){
			Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS);
			List<Map<String, Object>> games = JsonPath.parse(response.getBody(), configuration).read("$.data.games.game[*]");
			
			for (Map<String, Object> map : games) {
				if(map != null) {
					extractGameData(map).ifPresent(g -> scoreboard.getGames().add(g));
				} else {
					log.info("Scoreboard has null elements");
				}
			}
		}
	        
		return scoreboard;
	}

	private Optional<Game> extractGameData(Map<String, ?> map) {
		try {
			Game result = new Game();

			MutablePropertyValues values = new MutablePropertyValues();
			map.forEach((k,v ) -> values.add(toCamel(k), v));
			
			// Bind them
			DataBinder data = new DataBinder(result);
			data.bind(values);
			
			result.setHome(extractGameTeam(HOME, values));
			result.setAway(extractGameTeam(AWAY, values));
			result.setLeague(SupportedLeagues.MLB.name());	
			
			extractGameTime(map).ifPresent(t -> result.setUtc(t));
			
			if(log.isDebugEnabled()) {
				debugBinding(data);
			}			
			
			return Optional.of(result);
		} catch (Exception e) {
			log.error("extractGameData failed", e);
		}

		
		return Optional.empty();
	}

	private void debugBinding(DataBinder data) {
		try {
			data.close();
		} catch (BindException e) {
			e.getAllErrors().forEach(error -> System.out.println("Could not bind: "+error.getObjectName()));
		}
	}

	private Optional<String> extractGameTime(Map<String, ?> map) {
		Object media = JsonPath.read(map, "$.game_media.media");
		if(media !=  null) {
			// From map
			if(Map.class.isAssignableFrom(media.getClass())) {
				Object start = ((Map) media).get("start");
				
				if(start != null) {
					return Optional.of(start.toString());
				}
			} else {
				// From array
				Object start = JsonPath.read(media, "$[0].start");
				
				if(start != null) {
					return Optional.of(start.toString());
				}
			}	
		}
		
		return Optional.empty();
	}

	private GameTeam extractGameTeam(String type, MutablePropertyValues values) {
		GameTeam team = new GameTeam();
		if(type == HOME) {
			MlbTeamLookup apiTeam = MlbTeamLookup.lookupFrom(extractStringValue(values, "homeTeamId"));
			team.setCity(extractStringValue(values, "homeTeamCity"));
			team.setCode(extractStringValue(values, "homeNameAbbrev"));
			team.setName(apiTeam.team());
			team.setRecord(extractStringValue(values, "homeWin")+"-" +
					extractStringValue(values, "homeLoss"));
			
			String starter = extractStarter(values, "homeProbablePitcher");
			
			if(StringUtils.hasText(starter)) {
				team.getMetadata().put(MlbMetadata.KEY_STARTER, starter);
			}
		} else {
			MlbTeamLookup apiTeam = MlbTeamLookup.lookupFrom(extractStringValue(values, "awayTeamId"));
			team.setCity(extractStringValue(values, "awayTeamCity"));
			team.setCode(extractStringValue(values, "awayNameAbbrev"));
			team.setName(apiTeam.team());
			team.setRecord(extractStringValue(values, "awayWin")+"-" +
					extractStringValue(values, "awayLoss"));
			
			String starter = extractStarter(values, "awayProbablePitcher");
			
			if(StringUtils.hasText(starter)) {
				team.getMetadata().put(MlbMetadata.KEY_STARTER, starter);
			}
		}
		return team;
	}

	private String extractStarter(MutablePropertyValues values, String key) {
		StringBuilder name = new StringBuilder();
		Object v = values.get(key);
		
		if(v != null) {
			if(Map.class.isAssignableFrom(v.getClass())) {
				Optional<Object> first = Optional.ofNullable(((Map) v).get("first"));
				Optional<Object> last = Optional.ofNullable(((Map) v).get("last"));

				first.ifPresent(f -> name.append(f.toString()));
				name.append(' ');
				last.ifPresent(l -> name.append(l.toString()));			
			}			
		}

		return StringUtils.trimWhitespace(name.toString());
	}

	private String extractStringValue(MutablePropertyValues values, String key) {
		Optional<Object> value = Optional.ofNullable(values.get(key));
		if(value.isPresent()) {
			return value.get().toString();
		}
		return "";
	}

	private String toCamel(String key) {
		String[] array = StringUtils.tokenizeToStringArray(key, "_");
		
		if(array.length > 1) {
			StringBuffer camelCase = new StringBuffer(array[0]);
			
			for (int i = 1; i < array.length; i++) {
				camelCase.append(StringUtils.capitalize(array[i]));				
			}
		
			return camelCase.toString();
		} else {
			return key;
		}
		
	}
	
}
