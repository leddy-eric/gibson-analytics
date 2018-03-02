package com.gibson.analytics.client.odds;

import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OddsAPITest {
	private final Logger log = LoggerFactory.getLogger(OddsAPITest.class);
			
	@Autowired
	private OddsAPI api;
	
	@Test
	@Ignore
	public void testTournaments() {
		Map<String, String> tournamentMapping = api.getTournamentMapping();
		assertFalse(tournamentMapping.isEmpty());
	}
	
	@Test
	@Ignore
	public void testOddsEvents() {
		List<Odds> events = api.getOddsEvents("sr:tournament:132");
		assertFalse(events.isEmpty());
		
		for (Odds e : events) {
			log.debug(e.getAwayTeamCode() + " @ "+ e.getHomeTeamCode() + " - (Total:"+ e.getTotal() + ") (Spread:" +e.getSpread() +") "+ e.getDate());			
		}

	}

}
