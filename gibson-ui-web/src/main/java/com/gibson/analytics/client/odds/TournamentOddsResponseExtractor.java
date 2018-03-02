package com.gibson.analytics.client.odds;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class TournamentOddsResponseExtractor implements ResponseExtractor<List<Odds>> {
	private final Logger log = LoggerFactory.getLogger(TournamentOddsResponseExtractor.class);

	@Override
	public List<Odds> extractData(ClientHttpResponse response) throws IOException {
		List<Odds> odds = new ArrayList<>();
		
		if(response.getStatusCode().is2xxSuccessful()){
			String pathExpression = "$.sport_events[*]";
			log.debug("Read response for " + pathExpression);
			
			DocumentContext context = JsonPath.parse(response.getBody(), Configuration.defaultConfiguration());
			
			List<Map<String, Object>> events = context.read(pathExpression);
			
			for (int i = 0; i < events.size(); i++) {
				Map<String, Object> event = events.get(i);
				if(event != null) {
					Odds o = new Odds();

					o.setId(event.get("id").toString());
					Instant instant = OffsetDateTime.parse(event.get("scheduled").toString()).toInstant();
					LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
					o.setDate(dateTime.toString());
					Map<String, Object> homeTeam = context.read("$.sport_events["+ i +"].competitors[0]");
					o.setHomeTeam(homeTeam.get("name").toString());
					o.setHomeTeamCode(homeTeam.get("abbreviation").toString());
					Map<String, Object> awayTeam = context.read("$.sport_events["+ i +"].competitors[1]");
					o.setAwayTeam(awayTeam.get("name").toString());
					o.setAwayTeamCode(awayTeam.get("abbreviation").toString());
					List<String> total =
							context.read("$.sport_events["+ i +"].markets[?(@.name == 'total')].books[0].outcomes[?(@.type == 'over')].total");
					if(!total.isEmpty()) {
						o.setTotal(total.get(0));
					}
					List<String> spread = 
							context.read("$.sport_events["+ i +"].markets[?(@.name == 'spread')].books[0].outcomes[?(@.type == 'home')].spread");
					
					if(!spread.isEmpty()) {
						o.setSpread(spread.get(0));						
					}

					odds.add(o);
				} else {
					log.error("Event has zero lements");
				}
			}
			
		}
		
		return odds;
	}


}
