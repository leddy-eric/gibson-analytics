package com.gibson.analytics.repository;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.NbaTeam;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NbaTeamRepositoryTest {

	@Autowired
	NbaTeamRepository repository;
	
	@Test
	public void testRepositoryLoads(){
		Iterable<NbaTeam> all = this.repository.findAll();
		
		assertTrue(all.iterator().hasNext());
	}
}
