package com.gibson.analytics.init.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.gibson.analytics.data.Batter;
import com.gibson.analytics.init.CsvLineupConstants;

public class BatterProcessor implements ItemProcessor<Map<String, String>, Batter>, CsvLineupConstants {
	
	private static final Logger log = LoggerFactory.getLogger(BatterProcessor.class);
	
	@Override
	public Batter process(Map<String, String> item) throws Exception {
		Batter b = new Batter();
		
		String playerId = item.get(COLUMN_PLAYER_ID);
		
		if(!playerId.equals("NA")){
			b.setPlayerId(Long.parseLong(playerId));
		}
		
		b.setOrder(Integer.parseInt(item.get(COLUMN_BATTING_ORDER)));
		b.setTeamId(item.get(COLUMN_TEAM));
		
		return b;
	}

}
