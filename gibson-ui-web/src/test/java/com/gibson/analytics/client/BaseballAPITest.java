package com.gibson.analytics.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.enums.MlbTeamLookup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseballAPITest {
	
	@Autowired
	BaseballAPI api;
	
	@Test
	public void isConfigured(){
		assertNotNull(api);
	}
	
	@Test
	public void returnsScoreboard() {
		assertNotNull(api.getScoreboard());
	}
	
	@Test
	public void getLineup() {
		Lineup lineup = 
				api.getLineup("components/game/mlb/year_2019/month_03/day_30/gid_2019_03_30_chnmlb_texmlb_1/players.xml");
		
		assertNotNull(lineup);
	}
	
	@Test
	public void testRoster() {
		List<Player> roster = api.getRoster(MlbTeamLookup.CUBS);
		
		assertNotNull(roster);
		assertTrue(!roster.isEmpty());
		assertTrue(roster.get(0).getTeam().equals(MlbTeamLookup.CUBS.team()));
	}
	

}
