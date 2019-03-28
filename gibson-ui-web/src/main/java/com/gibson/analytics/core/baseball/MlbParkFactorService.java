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
	
	private final Map<String, BigDecimal> factors = new HashMap<>();  
	
	@Autowired
	TeamRepository temaRepository;
	
	@Autowired
	TeamStatisticRepository teamStatsRepository;
	
	public BigDecimal findParkFactor(String team) {
		Optional<TeamStatistic> wOBA = teamStatsRepository.findDistinctByTeamIdAndName(team, CsvTeamConstants.COLUMN_WEIGHTED_TEAM_OBA);
		
		if(wOBA.isPresent()) {
			log.info("TeamStats: "+ team);
			log.info("TeamStats: " +  wOBA.get().getName() + " : " + wOBA.get().getValue());
			return wOBA.get().getValue();
		}
		
		return BigDecimal.ONE;
	}
}
