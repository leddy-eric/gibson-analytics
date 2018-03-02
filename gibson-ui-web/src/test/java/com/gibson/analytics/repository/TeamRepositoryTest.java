package com.gibson.analytics.repository;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.Team;
import com.gibson.analytics.repository.TeamRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamRepositoryTest {

	@Autowired
	TeamRepository repository;
	
	@Test
	public void testRepositoryLoads(){
		Iterable<Team> all = this.repository.findAll();
		
		assertTrue(all.iterator().hasNext());
	}
}
