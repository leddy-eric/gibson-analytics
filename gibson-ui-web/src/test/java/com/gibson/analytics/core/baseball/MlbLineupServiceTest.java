package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertEquals;

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
public class MlbLineupServiceTest {

	@Autowired
	MlbLineupService lienupService;
	
	@Autowired
	public ExponentialLineProvider lineProvider;
	
	@Autowired
	public ExponentialTotalProvider totalProvider;
	
	@Test
	@Transactional
	public void testLineup() {
		MlbLineup home = lienupService.constructLineup("625643", 
									  MlbTeamLookup.WHITE_SOX, 
									  "641313", 
									  "467793", 
									  "547989",
									  "456078",
									  "660162",
									  "650391",
									  "475174",
									  "602922",
									  "641553");
		
		MlbLineup away = lienupService.constructLineup("579328", 
				  MlbTeamLookup.MARINERS, 
				  "605480", 
				  "571745", 
				  "570267",
				  "457803",
				  "542921",
				  "553882",
				  "592387",
				  "596129",
				  "543829");
		
		Game g = new Game();
		GameTeam homeTeam = new GameTeam();
		homeTeam.setName(MlbTeamLookup.WHITE_SOX.team());
		g.setHome(homeTeam);
		GameTeam awayTeam = new GameTeam();
		awayTeam.setName(MlbTeamLookup.MARINERS.team());
		g.setAway(awayTeam);
		
		g.setId("2019/04/05/seamlb-chalb-1");
		
		GameStatistic line = lineProvider.createStatistics(g, home, away);
		GameStatistic total = totalProvider.createStatistics(g, home, away);
		
		assertEquals("Expected 8.18 for exponential total....", 8.18, total.getValue());
		assertEquals("Expected -220 for exponential line total....", -220, line.getValue());
	}
	
	@Test
	@Transactional
	public void testLineupNl() {
		MlbLineup away = lienupService.constructLineup("628317", 
									  MlbTeamLookup.DODGERS, 
									  "571771", 
									  "608369", 
									  "457759",
									  "641355",
									  "572041",
									  "571970",
									  "621035",
									  "431145",
									  "628317");
		
		MlbLineup home = lienupService.constructLineup("542881", 
				  MlbTeamLookup.ROCKIES, 
				  "453568", 
				  "621311", 
				  "571448",
				  "596115",
				  "641857",
				  "435622",
				  "547172",
				  "641658",
				  "542881");
		
		Game g = new Game();
		GameTeam homeTeam = new GameTeam();
		homeTeam.setName(MlbTeamLookup.ROCKIES.team());
		g.setHome(homeTeam);
		GameTeam awayTeam = new GameTeam();
		awayTeam.setName(MlbTeamLookup.DODGERS.team());
		g.setAway(awayTeam);
		
		g.setId("2019/04/05/ladmlb-colmlb-1");
		
		GameStatistic line = lineProvider.createStatistics(g, home, away);
		GameStatistic total = totalProvider.createStatistics(g, home, away);
		
		assertEquals("Expected 8.18 for exponential total....", 8.18, total.getValue());
		assertEquals("Expected -220 for exponential line total....", -220, line.getValue());
	}
	
	
}
