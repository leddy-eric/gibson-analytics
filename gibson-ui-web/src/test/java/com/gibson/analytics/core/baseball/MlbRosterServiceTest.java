package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Lineup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MlbRosterServiceTest {
	
	@Autowired
	private MlbRosterService service;
	
	@Test
	public void testDefaultRoster() {
		// Setup
		Game game =  new Game();
		
		GameTeam home = new GameTeam();
		home.setName("Cubs");
		GameTeam away = new GameTeam();
		away.setName("Rockies");
		
		game.setGameDataDirectory("/nowhere/data");
		game.setAway(away);
		game.setHome(home);
		
		Lineup lineup = service.findActiveLineup(game);
		assertNotNull(lineup);
	}
	
	@Test
	public void testRosterWithMlbData() {
		// Setup
		Game game =  new Game();
		
		GameTeam home = new GameTeam();
		home.setName("Cubs");
		GameTeam away = new GameTeam();
		away.setName("Rockies");
		
		game.setGameDataDirectory("/components/game/mlb/year_2017/month_04/day_19/gid_2017_04_19_balmlb_cinmlb_1");
		game.setAway(away);
		game.setHome(home);
		
		Lineup lineup = service.findActiveLineup(game);
		assertNotNull(lineup);
	}
	
	
}
