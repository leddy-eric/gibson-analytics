package com.gibson.analytics.init.processor;

import java.util.Map;
import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.init.CsvPlayerConstants;
import com.gibson.analytics.repository.PlayerRepository;

/**
 * This processor is called with a single row and will build the Player entity to be written to the database.
 * @author leddy.eric
 *
 */
public class PlayerRowProcessor implements ItemProcessor<Map<String, String>, Player>, CsvPlayerConstants {
	private PlayerRepository repository;
	
	
	public PlayerRowProcessor(PlayerRepository repository){
		this.repository = repository;
	}

	@Override
	public Player process(Map<String, String> row) throws Exception {
		Player player = null;

		Optional<Player> playerOptional = repository.findByName(row.get(COLUMN_NAME));
		
		if(playerOptional.isPresent()) {
			player = playerOptional.get();
		} else {
			player = new Player();
			player.setName(row.get(COLUMN_NAME));
			player.setTeam(row.get(COLUMN_TEAM));
			player.setStatus("I");		
		}
		
		return player;
	}

}
