package com.gibson.analytics.client.baseball;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
		Game result = new Game();

		MutablePropertyValues values = new MutablePropertyValues();
		map.forEach((k,v ) -> values.add(toCamel(k), v));
		
		// Bind them
		DataBinder data = new DataBinder(result);
		data.bind(values);
		
		result.setHome(extractGameTeam(HOME, values));
		result.setAway(extractGameTeam(AWAY, values));
		result.setLeague(SupportedLeagues.MLB.name());	
		result.setUtc(extractGameTime(map));	
		
		if(log.isDebugEnabled()) {
			try {
				data.close();
			} catch (BindException e) {
				e.getAllErrors().forEach(error -> System.out.println("Could not bind: "+error.getObjectName()));
			}
		}
		
		return result;
	}

	private String extractGameTime(Map<String, ?> map) {
		Object media = JsonPath.read(map, "$.game_media.media");
		
		if(HashMap.class.isAssignableFrom(media.getClass())) {
			return ((HashMap) media).get("start").toString();
		} 

		return JsonPath.read(media, "$[0].start").toString();
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

	private GameTeam extractGameTeam(String type, MutablePropertyValues values) {
		GameTeam team = new GameTeam();
		if(type == HOME) {
			MlbTeamLookup apiTeam = MlbTeamLookup.lookupFrom(extractStringValue(values, "homeTeamId"));
			team.setCity(extractStringValue(values, "homeTeamCity"));
			team.setCode(extractStringValue(values, "homeNameAbbrev"));
			team.setName(apiTeam.team());
			team.setRecord(extractStringValue(values, "homeWin")+"-" +
					extractStringValue(values, "homeLoss"));
		} else {
			MlbTeamLookup apiTeam = MlbTeamLookup.lookupFrom(extractStringValue(values, "awayTeamId"));
			team.setCity(extractStringValue(values, "awayTeamCity"));
			team.setCode(extractStringValue(values, "awayNameAbbrev"));
			team.setName(apiTeam.team());
			team.setRecord(extractStringValue(values, "awayWin")+"-" +
					extractStringValue(values, "awayLoss"));
		}
		return team;
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
		
		if(array.length > 1){
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
