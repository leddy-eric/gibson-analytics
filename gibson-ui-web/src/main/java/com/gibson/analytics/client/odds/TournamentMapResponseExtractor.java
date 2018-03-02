package com.gibson.analytics.client.odds;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class TournamentMapResponseExtractor implements ResponseExtractor<Map<String, String>> {
	private final Logger log = LoggerFactory.getLogger(TournamentMapResponseExtractor.class);

	@Override
	public Map<String, String> extractData(ClientHttpResponse response) throws IOException {
		HashMap<String, String> map = new HashMap<>();
		
		
		if(response.getStatusCode().is2xxSuccessful()){
			String pathExpression = "$.tournaments[*]";
			log.debug("Read response for " + pathExpression);
			
			List<Map<String, Object>> tournaments = 
					JsonPath.parse(response.getBody(), Configuration.defaultConfiguration()).read(pathExpression);

			
			for (Map<String, Object> tournament : tournaments) {
				if(tournament != null) {
					// log.debug("Tournament: " + tournament.keySet());
					map.put(tournament.get("name").toString(), tournament.get("id").toString());
				} else {
					log.info("Tournaments has null elements");
				}
			}
		}
		
		return map;
	}

}
