package com.gibson.analytics.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.Lineup;

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
				api.getLineup("/components/game/mlb/year_2017/month_04/day_19/gid_2017_04_19_balmlb_cinmlb_1");
		
		assertNotNull(lineup);
	}

}