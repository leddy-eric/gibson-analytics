package com.gibson.analytics.init.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.init.CsvPlayerConstants;
import com.gibson.analytics.repository.PlayerRepository;

public class PlayerStatisticProcessor implements ItemProcessor<Map<String, String>, List<PlayerStatistic>>, CsvPlayerConstants {
	
	private PlayerRepository repository;
	private List<String> skipColumns = Arrays.asList("Name","playerId","Team");
	
	public PlayerStatisticProcessor(PlayerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<PlayerStatistic> process(Map<String, String> row) throws Exception {
		List<PlayerStatistic> stats = new ArrayList<>();
		String playerName = row.get(COLUMN_NAME);
		
		Optional<Player> player = repository.findByName(playerName);
		
		// Update stats if they exist
		if(player.isPresent()) {
			Set<Entry<String,String>> entrySet = row.entrySet();
			
			for (Entry<String, String> entry : entrySet) {
				if(!skipColumns.contains(entry.getKey())) {
					PlayerStatistic s = new PlayerStatistic();
					s.setName(entry.getKey());
					String value = entry.getValue();
					s.setPlayerId(player.get().getId());
					
					if(!StringUtils.isEmpty(value) && !value.equals("NA")) {
						BigDecimal valueAsDecimal = extractValueAsDecimal(value);
						s.setValue(valueAsDecimal);
						stats.add(s);	
					}
				}
			}
		}

		return stats;
	}
	
	/**
	 * Translate String to big decimal.
	 * 
	 * @param value
	 * @return
	 */
	private BigDecimal extractValueAsDecimal(String value) {
		if(StringUtils.hasText(value)) {
			try {
				return new BigDecimal(value);
			} catch (Exception e) {
				//System.out.println("Could not parse big decimal (" +value +") "+e.getMessage());
			}			
		}

		return BigDecimal.valueOf(0);
	}

}
