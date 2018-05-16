package com.gibson.analytics.core.baseball.repository;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.enums.MlbTeamLookup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MlbGameDetailRepositoryTest {
	
	@Autowired
	MlbGameDetailRepository repository;
	
	
	@Test
	public void testRepositoryLoads(){
		long count = this.repository.count();
		
		assertTrue(count> 0);
	}
	
	
	@Test
	public void testFindByTeam() {
		Optional<MlbGameDetail> homeGame = this.repository.findTopByAwayTeamOrderByCreated(MlbTeamLookup.ROCKIES);
		
		assertTrue(homeGame.isPresent());
	}

}
