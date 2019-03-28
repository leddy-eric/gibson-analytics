package com.gibson.analytics.init.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.init.CsvPitcherConstants;
import com.gibson.analytics.repository.PlayerRepository;

public class PitcherStatisticProcessor implements ItemProcessor<Map<String, String>, List<PlayerStatistic>>, CsvPitcherConstants {
	
	private PlayerRepository repository;
	
	public PitcherStatisticProcessor(PlayerRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<PlayerStatistic> process(Map<String, String> row) throws Exception {
		List<PlayerStatistic> stats = new ArrayList<>();
		Optional<String> playerId = Optional.ofNullable(row.get(COLUMN_PLAYERID));
		
		long id = 0;
		
		if(playerId.isPresent()) {
			id = Long.parseLong(playerId.get());
		}
		
		Optional<Player> player = repository.findById(id);
		
		// Update stats if they exist
		if(player.isPresent()) {
			Set<Entry<String,String>> entrySet = row.entrySet();
			
			for (Entry<String, String> entry : entrySet) {
				PlayerStatistic s = new PlayerStatistic();
				s.setName(entry.getKey());
				String value = entry.getValue();
				s.setPlayerId(player.get().getId());

				if(entry.getKey().equals(CsvPitcherConstants.COLUMN_THROWS)) {
					if(value.equals("L")) {
						s.setValue(BigDecimal.ONE.negate());
					} else  if (value.equals("S")){
						s.setValue(BigDecimal.ZERO);
					} else {
						s.setValue(BigDecimal.ONE);
					}
					
					stats.add(s);				
				} else {
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
				System.out.println("Could not parse big decimal (" +value +") "+e.getMessage());
			}			
		}

		return BigDecimal.valueOf(0);
	}

}
