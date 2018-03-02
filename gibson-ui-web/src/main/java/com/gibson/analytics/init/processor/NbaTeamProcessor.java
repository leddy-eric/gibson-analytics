package com.gibson.analytics.init.processor;

import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.gibson.analytics.data.NbaTeam;
import com.gibson.analytics.init.CsvNbaTeamConstants;

public class NbaTeamProcessor implements ItemProcessor<Map<String, String>, NbaTeam>, CsvNbaTeamConstants {

	@Override
	public NbaTeam process(Map<String, String> row) throws Exception {
		NbaTeam team = new NbaTeam();
		
		team.setName(row.get(COLUMN_TEAM));
		team.setScore(row.get(COLUMN_SCORE));
		team.setScoreMovingAverage(row.get(COLUMN_MOVING_AVERAGE));
		team.setSpreadScore(row.get(COLUMN_SPREAD_SCORE));
		team.setSpreadScoreMovingAverage(row.get(COLUMN_SPREAD_SCORE_MOVING_AVERAGE));
		team.setValueScore(row.get(COLUMN_VALUE_SCORE));
		team.setValueScoreMovingAverage(row.get(COLUMN_VALUE_SCORE_MOVING_AVERAGE));
		
		return team;
	}

}
