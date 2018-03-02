package com.gibson.analytics.client.basketball;

import java.net.URI;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gibson.analytics.client.BasketballAPI;
import com.gibson.analytics.data.Scoreboard;

@Component
public class BasketballAPIImpl implements BasketballAPI {
	private static final Logger log = LoggerFactory.getLogger(BasketballAPIImpl.class);
	
    private final String HOSTNAME = "http://data.nba.com";
    private final String RESOURCE_SCOREBOARD = "data/10s/v2015/json/mobile_teams/nba/2017/league/00_full_schedule_week.json";
	
    @Autowired
    private RestTemplate restTemplate;

	/* (non-Javadoc)
	 * @see com.gibson.analytics.client.BasketballAPI#getScoreboard()
	 */
	@Override
	public Scoreboard getScoreboard() {
		return this.getScoreboard(LocalDate.now());
	}
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.client.BasketballAPI#getScoreboard(java.time.LocalDateTime)
	 */
	@Override
	public Scoreboard getScoreboard(LocalDate datetime) {
		log.debug("Getting scoreboard:"+ datetime);
		
        Scoreboard scoreboard = 
        		restTemplate.execute(buildScoreboardUri(datetime), HttpMethod.GET, null, 
        				new BasketballScoreboardResponseExtractor(datetime));
		
		scoreboard.setDate(datetime);
		
		return scoreboard;
	}

	private URI buildScoreboardUri(LocalDate datetime) {
		return UriComponentsBuilder.fromHttpUrl(HOSTNAME)
				   .path(RESOURCE_SCOREBOARD)
				   .build()
				   .toUri();
	}

	@Override
	public String getLeagueIdentifier() {
		return "NBA";
	}
}
