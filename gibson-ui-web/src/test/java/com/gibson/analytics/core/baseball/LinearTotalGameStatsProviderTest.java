package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.enums.MlbTeamLookup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinearTotalGameStatsProviderTest {

	@Autowired
	public LinearTotalProvider provider;
	
	@Test
	@Ignore
	@Transactional
	public void testGameLine() {
		Game g = new Game();
		GameTeam home = new GameTeam();
		home.setName(MlbTeamLookup.RED_SOX.team());
		g.setHome(home);
		GameTeam away = new GameTeam();
		away.setName(MlbTeamLookup.ATHLETICS.team());
		g.setAway(away);
		
		g.setId("2018/05/14/oakmlb-bosmlb-1");
		
		GameStatistic stat = provider.createStatistics(g);
		
		assertTrue(stat.getValue().equals("0.10"));
	}
}
