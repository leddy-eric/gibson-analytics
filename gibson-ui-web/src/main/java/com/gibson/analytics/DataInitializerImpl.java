package com.gibson.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gibson.analytics.init.CsvSource;

@Component
public class DataInitializerImpl implements DataInitializer {
	final static Logger log = LoggerFactory.getLogger(DataInitializerImpl.class);

	private static final int NBA_TEAM_COLUMN_COUNT = 7;
	private static final int PITCHER_COLUMN_COUNT = 9;
	private static final int PLAYER_COLUMN_COUNT = 61;

	
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.DataInitializer#loadPitchers(com.gibson.analytics.init.CsvSource)
	 */
	@Override
	public void loadPitchers(CsvSource source) {
		if(isValid(source, PITCHER_COLUMN_COUNT)){

		} else {
			log.error("The pitcher csv file is not of the expected format");						
		}		
	}

	/* (non-Javadoc)
	 * @see com.gibson.analytics.DataInitializer#loadPlayers(com.gibson.analytics.init.CsvSource)
	 */
	@Override
	public void loadPlayers(CsvSource source) {
		if(isValid(source, PLAYER_COLUMN_COUNT)){
			
		} else {
			log.error("The pitcher csv file is not of the expected format");						
		}	
	}
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.DataInitializer#loadNbaTeams(com.gibson.analytics.init.CsvSource)
	 */
	@Override
	public void loadNbaTeams(CsvSource source) {
		if(isValid(source, NBA_TEAM_COLUMN_COUNT)){
			
		} else {
			log.error("The NBA Team Ranking csv file is not of the expected format");						
		}	
	}
	
	/**
	 * Checks if is valid.
	 *
	 * @param source the source
	 * @param expectedColumnCount the expected column count
	 * @return true, if source header is present and has the expected size
	 */
	private boolean isValid(CsvSource source, int expectedColumnCount) {
		if(source.isInitialized()){
			return (source.getHeader().get().size() == expectedColumnCount);
		}
		
		return false;
	}

	

}
