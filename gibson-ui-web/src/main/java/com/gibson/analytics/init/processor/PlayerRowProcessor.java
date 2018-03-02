package com.gibson.analytics.init.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.init.CsvPlayerConstants;

/**
 * This processor is called with a single row and will build the Player entity to be written to the database.
 * @author leddy.eric
 *
 */
public class PlayerRowProcessor implements ItemProcessor<Map<String, String>, Player>, CsvPlayerConstants {
	final static Logger log = LoggerFactory.getLogger(PlayerRowProcessor.class);

	@Override
	public Player process(Map<String, String> row) throws Exception {
		Player p = new Player();
		
		p.setId(Long.parseLong(row.get(COLUMN_PLAYERID)));
		p.setName(row.get(COLUMN_NAME));
		p.setTeam(row.get(COLUMN_TEAM));
	
		
		return p;
	}

}
