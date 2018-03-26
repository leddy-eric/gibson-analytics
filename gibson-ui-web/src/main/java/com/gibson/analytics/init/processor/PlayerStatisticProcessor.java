package com.gibson.analytics.init.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;

import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.init.CsvPlayerConstants;

public class PlayerStatisticProcessor implements ItemProcessor<Map<String, String>, List<PlayerStatistic>>, CsvPlayerConstants {

	@Override
	public List<PlayerStatistic> process(Map<String, String> row) throws Exception {
		List<PlayerStatistic> stats = new ArrayList<>();
		Long playerId = Long.parseLong(row.get(COLUMN_PLAYERID));
		
		Set<Entry<String,String>> entrySet = row.entrySet();
		
		for (Entry<String, String> entry : entrySet) {
			PlayerStatistic s = new PlayerStatistic();
			s.setName(entry.getKey());
			s.setValue(entry.getValue());
			s.setPlayerId(playerId);
			
			stats.add(s);
		}
		
		return stats;
	}

}
