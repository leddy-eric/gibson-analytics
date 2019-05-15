package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.data.Team;
import com.gibson.analytics.data.TeamStatistic;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.gibson.analytics.init.CsvTeamConstants;
import com.gibson.analytics.repository.TeamRepository;
import com.gibson.analytics.repository.TeamStatisticRepository;

/**
 * Look up factor from map by team name.
 * 
 * @author leddy.eric
 *
 */
@Service
public class MlbParkFactorService {
	final static Logger log = LoggerFactory.getLogger(MlbParkFactorService.class);
	
	@Autowired
	TeamRepository temaRepository;
	
	@Autowired
	TeamStatisticRepository teamStatsRepository;
	
	public ParkFactor findParkFactor(String team) {
		return findParkFactor(MlbTeamLookup.lookupFromTeamName(team), CsvTeamConstants.COLUMN_WEIGHTED_TEAM_OBA); 
	}
	
	public ParkFactor findLinearParkFactor(String team) {
		return findParkFactor(MlbTeamLookup.lookupFromTeamName(team), CsvTeamConstants.COLUMN_LINE_LEARNED_PF); 
	}
	
	public ParkFactor findParkFactor(MlbTeamLookup team, String column) {
		Optional<TeamStatistic> factor = teamStatsRepository.findDistinctByTeamIdAndName(team.team(), column);
		
		if(factor.isPresent()) {
			TeamStatistic statistic = factor.get();
			log.info("TeamStats: "+ team);
			log.info("TeamStats: " +  statistic.getName() + " : " + statistic.getValue());
			return new ParkFactor(statistic.getValue(), team);
		}
		
		return new ParkFactor(BigDecimal.ONE, team); 
	}
}
