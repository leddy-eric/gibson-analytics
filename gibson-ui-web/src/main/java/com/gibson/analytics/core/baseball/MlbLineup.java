package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gibson.analytics.core.baseball.algorithm.MatchupAlgorithm;
import com.gibson.analytics.core.baseball.stats.PitchingStatistics;
import com.gibson.analytics.init.CsvPlayerConstants;

public class MlbLineup {
	
	final static Logger log = LoggerFactory.getLogger(MlbLineup.class);
	
	private String team;
	private ZonedDateTime gametime;
	
	// Pitching
	private MlbPitcher startingPitcher;
	private PitchingStatistics bullpen;
	
	// Batting Order
	private List<MlbPlayer> lineup;
	
	// Constants
	private static final String POSITION_PITCHER = "P";
	
	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}
	
	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	
	/**
	 * @return the lineup
	 */
	public List<MlbPlayer> getLineup() {
		return lineup;
	}
	
	/**
	 * @param lineup the lineup to set
	 */
	public void setLineup(List<MlbPlayer> lineup) {
		this.lineup = lineup;
	}

	/**
	 * @return the gametime
	 */
	public ZonedDateTime getGametime() {
		return gametime;
	}

	/**
	 * @param gametime the gametime to set
	 */
	public void setGametime(ZonedDateTime gametime) {
		this.gametime = gametime;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal calculateTeamBsR() {
		return mapReduce(CsvPlayerConstants.COLUMN_BSR, 0);
	}
	
	/**
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	private BigDecimal mapReduce(final String name, final double defaultValue) {
		if(lineup.isEmpty()) {
			return BigDecimal.valueOf(0);
		}
		
		return lineup.stream()
				.filter(p -> !POSITION_PITCHER.equals(p.getPlayer().getPosition()))
				.map(p -> p.getStatisticOrDefault(name, defaultValue))
				.reduce(BigDecimal::add).get();
	}



	/**
	 * Uses the lineup to determine the pitcher and passes a MLBPitcher wrapper in return, 
	 * if no pitcher exist in the lineup the default will be returned.
	 *  
	 * @return
	 */
	public MlbPitcher getPitcher() {
		Optional<MlbPlayer> player = lineup.stream()
										.filter(p -> p.isPitcher())
		        						.collect(Collectors.reducing((a, b) -> null));
		
		if(player.isPresent() && startingPitcher == null) {
			return new MlbPitcher(player.get().getPlayer());
		}
		
		return startingPitcher;
	}
	
	public double runsAgainst(MlbLineup opponent, BigDecimal parkFactor) {
		double total = runsAgainst(opponent.getStartingPitcher(), parkFactor) +  runsAgainst(opponent.getBullpen(), parkFactor);
		
		return MatchupAlgorithm.adjustedTotalRuns(total, this.calculateTeamBsR(), opponent.calculateTeamDefPerInn());
	}

	/**
	 * Uses the algorithm defined by Aaron to calculate the expected runs this line up will generate versus opposing pitcher.
	 * 
	 * @param opposingPitcher
	 * @return
	 */
	public double runsAgainst(PitchingStatistics opposingPitcher, BigDecimal parkFactor) {
		log.info("Team: "+ this.team +" vs opposing pitcher");
		
		return MatchupAlgorithm.runsVsOpossingPitching(lineup, opposingPitcher, parkFactor);
	}


	/**
	 * @return the startingPitcher
	 */
	public MlbPitcher getStartingPitcher() {
		return startingPitcher;
	}

	/**
	 * @param startingPitcher the startingPitcher to set
	 */
	public void setStartingPitcher(MlbPitcher startingPitcher) {
		this.startingPitcher = startingPitcher;
	}


	/**
	 * @return the bullpen
	 */
	public PitchingStatistics getBullpen() {
		return bullpen;
	}

	/**
	 * @param bullpen the bullpen to set
	 */
	public void setBullpen(PitchingStatistics bullpen) {
		this.bullpen = bullpen;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal calculateTeamDefPerInn() {
		return mapReduce(CsvPlayerConstants.COLUMN_DEF, 0);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isValid() {
		boolean isValid = true;
		
		if(lineup.isEmpty()) {
			isValid = true;
		}
		
		if(bullpen == null) {
			isValid = true;
		}
		
		if(startingPitcher == null) {
			isValid = true;
		}
		
		return isValid;
	}


}
