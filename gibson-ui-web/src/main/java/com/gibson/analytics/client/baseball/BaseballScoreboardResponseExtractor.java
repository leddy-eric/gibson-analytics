package com.gibson.analytics.client.baseball;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.web.client.ResponseExtractor;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.Scoreboard;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@Component
public class BaseballScoreboardResponseExtractor implements ResponseExtractor<Scoreboard> {
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

	private Game extractGameData(Map<String, Object> map) {
		Game result = new Game();
		
		MutablePropertyValues values = new MutablePropertyValues();
		map.forEach((k,v ) -> values.add(toCamel(k), v));
		
		// Bind them
		DataBinder data = new DataBinder(result);
		data.bind(values);
		
		if(log.isDebugEnabled()) {
			try {
				data.close();
			} catch (BindException e) {
				e.getAllErrors().forEach(error -> System.out.println("Could not bind: "+error.getObjectName()));
			}
		}
		
		return result;
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
