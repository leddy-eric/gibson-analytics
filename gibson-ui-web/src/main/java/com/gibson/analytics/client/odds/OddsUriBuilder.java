package com.gibson.analytics.client.odds;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.util.concurrent.RateLimiter;

public class OddsUriBuilder {
	
	private static final String HOSTNAME = "https://api.sportradar.us";
	private static final String RESOURCE_TOURNAMENTS = "oddscomparison-{package}{access_level}{version}/{language_code}/{odds_format}/tournaments.{format}";
	private static final String RESOURCE_TOURNAMENT_ODDS = "oddscomparison-{package}{access_level}{version}/{language_code}/{odds_format}/tournaments/{tournament_id}/schedule.{format}";
	
	private OddsAPIConfiguration apiConfiguration;
	private RateLimiter limiter = RateLimiter.create(0.5);
	
	public OddsUriBuilder(OddsAPIConfiguration apiConfiguration) {
		this.apiConfiguration = apiConfiguration;
	}
	
	public URI getTournamentsUri() {
		limiter.acquire();
		return UriComponentsBuilder.fromHttpUrl(HOSTNAME)
				   .path(RESOURCE_TOURNAMENTS)
				   .queryParam("api_key", apiConfiguration.getApiKey())
				   .buildAndExpand(apiConfiguration.getRegion(),
						   			apiConfiguration.getAccessLevel(),
						   			apiConfiguration.getVersion(),
						   			apiConfiguration.getLanguageCode(),
						   			apiConfiguration.getOddsFormat(),
						   			apiConfiguration.getResponseFormat())
				   .toUri();
	}
	
	public URI getTournamentsOddsUri(String tournamentId) {
		limiter.acquire();
		return UriComponentsBuilder.fromHttpUrl(HOSTNAME)
				   .path(RESOURCE_TOURNAMENT_ODDS)
				   .queryParam("api_key", apiConfiguration.getApiKey())
				   .buildAndExpand(apiConfiguration.getRegion(),
						   			apiConfiguration.getAccessLevel(),
						   			apiConfiguration.getVersion(),
						   			apiConfiguration.getLanguageCode(),
						   			apiConfiguration.getOddsFormat(),
						   			tournamentId,
						   			apiConfiguration.getResponseFormat())
				   .toUri();
	}

}
