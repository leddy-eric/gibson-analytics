package com.gibson.analytics.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.enums.MlbTeamLookup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerRepositoryTest {
	
	@Autowired
	PlayerRepository repository;
	
	
	@Test
	public void testRepositoryLoads(){
		long count = this.repository.count();
		
		assertTrue(count> 0);
	}
	
	@Test 
	public void testTeamNames(){
		List<String> teamNames = this.repository.findTeamNames();
		
		assertTrue(!teamNames.isEmpty());
	}
	
	@Test
	public void testFindByTeam() {
		List<Player> list = this.repository.findByTeam(MlbTeamLookup.ROCKIES.team());
		
		assertTrue(!list.isEmpty());
	}

}
