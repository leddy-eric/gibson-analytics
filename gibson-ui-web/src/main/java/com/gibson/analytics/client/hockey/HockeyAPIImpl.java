package com.gibson.analytics.client.hockey;

import java.net.URI;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gibson.analytics.client.HockeyAPI;
import com.gibson.analytics.data.Scoreboard;

@Component
public class HockeyAPIImpl implements HockeyAPI {
	private static final Logger log = LoggerFactory.getLogger(HockeyAPIImpl.class);
	
	private final String HOSTNAME = "https://statsapi.web.nhl.com";
    private final String RESOURCE_SCOREBOARD = "api/v1/schedule";
    
    @Autowired
    private RestTemplate restTemplate;
	

	@Override
	public Scoreboard getScoreboard() {
		return this.getScoreboard(LocalDate.now());
	}

	@Override
	public Scoreboard getScoreboard(LocalDate datetime) {
		log.debug("Getting scoreboard:"+ datetime);
		
        Scoreboard scoreboard = 
        		restTemplate.execute(buildScoreboardUri(datetime), HttpMethod.GET, null, 
        				new HockeyScoreboardResponseExtractor(datetime));
		
		scoreboard.setDate(datetime);
		
		return scoreboard;
	}

	private URI buildScoreboardUri(LocalDate datetime) {
		return UriComponentsBuilder.fromHttpUrl(HOSTNAME)
				   .path(RESOURCE_SCOREBOARD)
				   .queryParam("startDate", datetime)
				   .queryParam("endDate", datetime)
				   .build()
				   .toUri();
	}

	@Override
	public String getLeagueIdentifier() {
		return "NHL";
	}

}
