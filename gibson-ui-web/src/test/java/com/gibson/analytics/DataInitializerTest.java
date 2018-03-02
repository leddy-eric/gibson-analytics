package com.gibson.analytics;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.data.NbaTeam;
import com.gibson.analytics.init.CsvSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataInitializerTest {

	@Autowired
	public CsvSource playerData;
	
	@Autowired
	public CsvSource nbaTeamData;
	
	@Test
	public void nbaTeamDataIsNotNull() {
		assertTrue(nbaTeamData != null);
	}
	
	@Test
	public void nbaTeamDataCsvLoads() {
		assertTrue(nbaTeamData.isInitialized());
		assertTrue(nbaTeamData.getHeader().isPresent());
		assertTrue(nbaTeamData.getRecords().isPresent());
	}
	
	@Test
	public void playerDataIsNotNull() {
		assertTrue(playerData != null);
	}
	
	@Test
	public void playerDataCsvLoads() {
		assertTrue(playerData.isInitialized());
		assertTrue(playerData.getHeader().isPresent());
		assertTrue(playerData.getRecords().isPresent());
	}

}
