package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.enums.MlbTeamLookup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MlbLineupServiceTest {
	@Autowired
	MlbLineupService service;
	
	@Test
	public void testLineupCalculations() {
		GameTeam team = new GameTeam();
		team.setName(MlbTeamLookup.INDIANS.team());
		MlbLineup indians = service.findActive(team);
		
		assertNotNull(indians);
		
		BigDecimal teamOnBaseRunning = indians.calculateTeamBsR();
		System.out.println(teamOnBaseRunning);
		
	}
}
