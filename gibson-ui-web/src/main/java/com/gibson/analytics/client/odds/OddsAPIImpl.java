package com.gibson.analytics.client.odds;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

public class OddsAPIImpl implements OddsAPI {
	private final Logger log = LoggerFactory.getLogger(OddsAPIImpl.class);
	
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private OddsUriBuilder oddsUriBuilder;
    
    /* (non-Javadoc)
	 * @see com.gibson.analytics.client.odds.OddsAPI#getTournamentMapping()
	 */
    @Override
	public Map<String, String> getTournamentMapping(){
    	URI uri = oddsUriBuilder.getTournamentsUri();
    	log.debug("Tournament URI: "+uri);
    	
    	return restTemplate.execute(uri, HttpMethod.GET, null, new TournamentMapResponseExtractor());
    }
    
    /* (non-Javadoc)
	 * @see com.gibson.analytics.client.odds.OddsAPI#getOddsEvents(java.lang.String)
	 */
    @Override
	public List<Odds> getOddsEvents(String tournamentId){
    	URI uri = oddsUriBuilder.getTournamentsOddsUri(tournamentId);
    	log.debug("Tournament events URI: "+uri);
    	
    	return restTemplate.execute(uri, HttpMethod.GET, null,	new TournamentOddsResponseExtractor());
    }

}
