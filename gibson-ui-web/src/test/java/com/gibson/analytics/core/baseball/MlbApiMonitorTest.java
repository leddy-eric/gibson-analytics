package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.repository.MlbGameDetailRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MlbApiMonitorTest {

	private LocalDate date = LocalDate.of(2018, 5, 16);
	
	@Autowired
	private MlbApiMonitor monitor;
	
	@Autowired
	private MlbGameDetailRepository repository;
	
	@Test
	public void testMonitor() {
		monitor.refreshGameData(date);
		
		List<MlbGameDetail> games = repository.findByGameDate(date);
		assertTrue(!games.isEmpty());		
	}
	
}
