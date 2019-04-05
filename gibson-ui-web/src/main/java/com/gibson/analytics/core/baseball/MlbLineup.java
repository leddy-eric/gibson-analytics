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
	
	public double runsAgainst(MlbLineup opponent, ParkFactor parkFactor) {
		double runsAgainstStarter = runsAgainst(opponent.getStartingPitcher(), parkFactor);
		double runsAgainstBullpen = runsAgainst(opponent.getBullpen(), parkFactor);
		
		log.debug(String.format("Runs:  vsStarter : %1$.8f | vsBullpen :  %2$.8f ", runsAgainstStarter, runsAgainstBullpen));
		
		double total = runsAgainstStarter +  runsAgainstBullpen;
		
		return MatchupAlgorithm.adjustedTotalRuns(total, this.calculateTeamBsR(), opponent.calculateTeamDefPerInn());
	}

	private double runsAgainst(PitchingStatistics stats, ParkFactor parkFactor) {
		log.debug("Team: "+ this.team + " vs  Bullpen");
		log.debug(String.format("Stats:  [SO, Walk, OBA, INN] : [%1$.8f,  %2$.8f , %3$.8f ,  %4$.8f] ", 
					stats.getStrikeoutRate(), 
					stats.getWalkRate(), 
					stats.getOnBaseAverage(), 
					stats.getProjectedInnings()));
		
		if(lineup.isEmpty()) {
			return MatchupAlgorithm.runsVsOpossingPitching(stats, parkFactor);
		}
		
		return MatchupAlgorithm.runsVsOpossingPitching(lineup, stats, parkFactor);
	}

	/**
	 * Uses the algorithm defined by Aaron to calculate the expected runs this line up will generate versus opposing pitcher.
	 * 
	 * @param opposingPitcher
	 * @return
	 */
	public double runsAgainst(MlbPitcher stats, ParkFactor parkFactor) {
		log.debug("Team: "+ this.team + " vs " + stats.getName());
		log.debug(String.format("Stats:  [SO, Walk, OBA, INN] : [%1$.8f,  %2$.8f , %3$.8f ,  %4$.8f] ", 
				stats.getStrikeoutRate(), 
				stats.getWalkRate(), 
				stats.getOnBaseAverage(), 
				stats.getProjectedInnings()));
		
		if(lineup.isEmpty()) {
			return MatchupAlgorithm.runsVsOpossingPitching(stats, parkFactor);
		}
		
		return MatchupAlgorithm.runsVsOpossingPitching(lineup, stats, parkFactor);
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

}
