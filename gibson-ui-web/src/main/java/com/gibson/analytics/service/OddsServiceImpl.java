package com.gibson.analytics.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gibson.analytics.client.odds.Odds;
import com.gibson.analytics.client.odds.OddsAPI;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

public class OddsServiceImpl implements OddsService {
	private final Logger log = LoggerFactory.getLogger(OddsServiceImpl.class);
	
	@Autowired
	private OddsAPI oddsApi;
	
	private List<String> supported = Arrays.asList(new String[] {"NBA","NHL"});
	private Map<String,Odds> availableOdds = new ConcurrentHashMap<>();
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.service.OddsService#createOdds(com.gibson.analytics.data.Game)
	 */
	@Override
	public void createOdds(Game game) {
		String key = generateKey(game);
		log.info("Query odds "+key);
		
		Odds odds = availableOdds.get(key);
		if(odds != null) {
			for (GameStatistic gameStatistic : game.getGameStatistics()) {
				if(gameStatistic.getName().equals("Total")) {
					gameStatistic.setVegas(odds.getTotal());
				} else if (gameStatistic.getName().equals("Spread")) {
					gameStatistic.setVegas(odds.getSpread());
				}
			}
		}

	}
	
	private String generateKey(Game game) {
		return game.getLeague() + game.getHome().getCode() + game.getUtc();
	}
	
	private String generateKey(String league, Odds odds) {
		return league + odds.getHomeTeamCode() + odds.getDate();
	}

	
	@Scheduled(cron="0 0/30 * * * *")
	@PostConstruct
	public void initialize() throws Exception {
		try {
			availableOdds.putAll(currentOdds());
		} finally {
			log.info("Odds updated: "+availableOdds.size() + " entries");
		}
	}
	
	
	private Map<String,Odds> currentOdds() {
		Map<String,Odds> odds = new HashMap<>();
		Set<Entry<String, String>> entrySet = oddsApi.getTournamentMapping().entrySet();
		
		for (Entry<String, String> entry : entrySet) {
			String league = entry.getKey();
			if(supported.contains(entry.getKey())) {
				List<Odds> events = oddsApi.getOddsEvents(entry.getValue());
				for (Odds o : events) {
					String generatedKey = generateKey(league, o);
					odds.put(generatedKey, o);
				}
			}
		}
		
		return odds;
	}


}
