package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gibson.analytics.enums.MlbTeamLookup;

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
	
	public BigDecimal findParkFactor(String team) {
		return factors.getOrDefault(team, BigDecimal.valueOf(1));
	}
	
	@PostConstruct
	public void init() {
		factors.put(MlbTeamLookup.ANGELS.team(), new BigDecimal("0.96"));
		factors.put(MlbTeamLookup.ASTROS.team(), new BigDecimal("0.94"));
		factors.put(MlbTeamLookup.ATHLETICS.team(), new BigDecimal("0.98"));
		factors.put(MlbTeamLookup.BLUE_JAYS.team(), new BigDecimal("1.05"));
		factors.put(MlbTeamLookup.BRAVES.team(), new BigDecimal("1.02"));
		factors.put(MlbTeamLookup.BREWERS.team(), new BigDecimal("1.01"));
		factors.put(MlbTeamLookup.CARDINALS.team(), new BigDecimal("0.95"));
		factors.put(MlbTeamLookup.CUBS.team(), new BigDecimal("1"));
		factors.put(MlbTeamLookup.DIAMONDBACKS.team(), new BigDecimal("1.12"));
		factors.put(MlbTeamLookup.DODGERS.team(), new BigDecimal("0.94"));
		factors.put(MlbTeamLookup.GIANTS.team(), new BigDecimal("0.92"));
		factors.put(MlbTeamLookup.INDIANS.team(), new BigDecimal("1.05"));
		factors.put(MlbTeamLookup.MARINERS.team(), new BigDecimal("0.97"));
		factors.put(MlbTeamLookup.MARLINS.team(), new BigDecimal("0.92"));
		factors.put(MlbTeamLookup.METS.team(), new BigDecimal("0.95"));
		factors.put(MlbTeamLookup.NATIONALS.team(), new BigDecimal("0.98"));
		factors.put(MlbTeamLookup.ORIOLES.team(), new BigDecimal("1.04"));
		factors.put(MlbTeamLookup.PADRES.team(), new BigDecimal("0.92"));
		factors.put(MlbTeamLookup.PHILLIES.team(), new BigDecimal("0.99"));
		factors.put(MlbTeamLookup.PIRATES.team(), new BigDecimal("0.98"));
		factors.put(MlbTeamLookup.RANGERS.team(), new BigDecimal("1.15"));
		factors.put(MlbTeamLookup.RAYS.team(), new BigDecimal("0.93"));
		factors.put(MlbTeamLookup.RED_SOX.team(), new BigDecimal("1.08"));
		factors.put(MlbTeamLookup.REDS.team(), new BigDecimal("1.04"));
		factors.put(MlbTeamLookup.ROCKIES.team(), new BigDecimal("1.32"));
		factors.put(MlbTeamLookup.ROYALS.team(), new BigDecimal("1.03"));
		factors.put(MlbTeamLookup.TIGERS.team(), new BigDecimal("1.07"));
		factors.put(MlbTeamLookup.TWINS.team(), new BigDecimal("1.05"));
		factors.put(MlbTeamLookup.WHITE_SOX.team(), new BigDecimal("1.01"));
		factors.put(MlbTeamLookup.YANKEES.team(), new BigDecimal("1.05"));	
	}

}
