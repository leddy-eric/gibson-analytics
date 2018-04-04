package com.gibson.analytics.init.processor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.gibson.analytics.data.Batter;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.init.CsvLineupConstants;
import com.gibson.analytics.repository.PlayerRepository;

public class BatterProcessor implements ItemProcessor<Map<String, String>, Batter>, CsvLineupConstants {
	
	private static final Logger log = LoggerFactory.getLogger(BatterProcessor.class);
	private PlayerRepository playerRepository;
	
	public BatterProcessor(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	@Override
	public Batter process(Map<String, String> item) throws Exception {
		Batter b = new Batter();
		String playerName = item.get(COLUMN_PLAYER_NAME);
		Optional<Player> player = playerRepository.findByName(playerName);
		
		if(!player.isPresent()) {
			return null;
		}
		
		Player p = player.get();
		b.setPlayerId(p.getId());
		b.setTeamId(p.getTeam());
		b.setOrder(Integer.parseInt(item.get(COLUMN_BATTING_ORDER)));	
		
		return b;
	}

}
