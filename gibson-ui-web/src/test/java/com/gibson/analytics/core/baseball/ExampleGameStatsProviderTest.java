package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleGameStatsProviderTest {
	
	@Autowired
	private ExampleGameStatsProvider provider;
	
	@MockBean
	private MlbRosterService service;
	
	private Game game;
	
	@Before
	public void setUp() {
		// Setup
		game =  new Game();
		GameTeam home = new GameTeam();
		home.setName("Nationals");
		GameTeam away = new GameTeam();
		away.setName("Rockies");
		
		game.setGameDataDirectory("/nowhere/data");
		game.setAway(away);
		game.setHome(home);
		
		given(service.findActiveLineup(game))
			.willReturn(createTestLineup());
	}

	@Test
	public void providerIsWired() {
		assertNotNull(provider);
	}
	
	@Test
	@Transactional
	public void providerCalculatesGame() {
		GameStatistic createStatistics = provider.createStatistics(game);
		assertNotNull(createStatistics);
		assertEquals("The defaults should calculate to 0.44400","0.44400", createStatistics.getValue());
	}
	
	/**
	 * Create test lineup
	 * 
	 * @return
	 */
	private Lineup createTestLineup() {
		Lineup lineup = new Lineup();
		lineup.setStatus(HttpStatus.OK);
		
		List<Player> away = new ArrayList<>();
		away.add(createTestPlayer("Test Player 1", "1B"));
		away.add(createTestPlayer("Test Player 2", "2B"));
		away.add(createTestPlayer("Test Player 3", "SS"));
		away.add(createTestPlayer("Test Player 4", "3B"));
		away.add(createTestPlayer("Test Player 5", "C"));
		away.add(createTestPlayer("Test Player 6", "LF"));
		away.add(createTestPlayer("Test Player 7", "CF"));
		away.add(createTestPlayer("Test Player 8", "RF"));
		away.add(createTestPlayer("Test Player 9", "P"));
		
		List<Player> home = new ArrayList<>();
		home.add(createTestPlayer("Home Player 1", "1B"));
		home.add(createTestPlayer("Home Player 2", "2B"));
		home.add(createTestPlayer("Home Player 3", "SS"));
		home.add(createTestPlayer("Home Player 4", "3B"));
		home.add(createTestPlayer("Home Player 5", "C"));
		home.add(createTestPlayer("Home Player 6", "LF"));
		home.add(createTestPlayer("Home Player 7", "CF"));
		home.add(createTestPlayer("Home Player 8", "RF"));
		home.add(createTestPlayer("Home Player 9", "P"));
		
		lineup.setHome(home);
		lineup.setAway(away);
		return lineup;
	}
	
	private Player createTestPlayer(String name, String position) {
		List<PlayerStatistic> playerStatistics = new ArrayList<>();
	
		playerStatistics.add(createTestStatistic("ParkNormalizedOBP", ".33"));
		playerStatistics.add(createTestStatistic("ParkNormalizedSLG", ".44"));
		
		return createTestPlayer(name, position, playerStatistics);
	}


	/**
	 * Create test data
	 * @param name
	 * @param position
	 * @param playerStatistics
	 * @return
	 */
	private Player createTestPlayer(String name, String position, List<PlayerStatistic> playerStatistics) {
		Player p = new Player();
		p.setName(name);
		p.setPosition(position);
		p.setStatistics(playerStatistics);
		return p;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	private PlayerStatistic createTestStatistic(String name, String value) {
		PlayerStatistic playerStatistic = new PlayerStatistic();
		
		playerStatistic.setName(name);
		playerStatistic.setValue(value);
		
		return playerStatistic;
	}

}
