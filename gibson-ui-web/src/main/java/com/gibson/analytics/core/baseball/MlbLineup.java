package com.gibson.analytics.core.baseball;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gibson.analytics.init.CsvPlayerConstants;

public class MlbLineup {
	
	final static Logger log = LoggerFactory.getLogger(MlbLineup.class);
	
	private String team;
	private ZonedDateTime gametime;
	private MlbPitcher startingPitcher;
	private List<MlbPlayer> lineup;
	
	private double onBasePercentageCoef = 57;
	private double onBasePercentageExCoef =  3.07;
	
	private double sluggingCoef = 10.56;
	private double sluggingExCoef = 1.46;	
	
	// Constants
	private static final BigDecimal LeagueAverageOBP = BigDecimal.valueOf(.32); 
	private static final BigDecimal LeagueAverageSLG = BigDecimal.valueOf(.415);
	private static final BigDecimal GDP_Coef = BigDecimal.valueOf(-.49);
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
	public BigDecimal calculateTeamOnBasePercentage() {
		return mapReduceWeighted(CsvPlayerConstants.COLUMN_PARKNORMALIZEDOBP, .31);
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal calculateTeamSlugging() {
		return mapReduceWeighted(CsvPlayerConstants.COLUMN_PARKNORMALIZEDSLG, .40);
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
	private BigDecimal mapReduceWeighted(final String name, final double defaultValue) {
		double leagueTotalWeight =  38.7;
		
		if(lineup.isEmpty()) {
			return BigDecimal.valueOf(0);
		}
		
		if(lineup.size() < 10) {
			leagueTotalWeight = 34.8;
		}
		
		final double totalWeight = leagueTotalWeight;
		
		return lineup.stream()
				.filter(p -> !POSITION_PITCHER.equals(p.getPlayer().getPosition()))
				.map(p -> p.calculateWeightedStatistic(name, defaultValue, totalWeight))
				.reduce(BigDecimal::add).get();
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
			return new MlbPitcher(player.get());
		}
		
		return startingPitcher;
	}

	/**
	 * @return the onBasePercentageCoef
	 */
	public double getOnBasePercentageCoef() {
		return onBasePercentageCoef;
	}

	/**
	 * @param onBasePercentageCoef the onBasePercentageCoef to set
	 */
	public void setOnBasePercentageCoef(double onBasePercentageCoef) {
		this.onBasePercentageCoef = onBasePercentageCoef;
	}

	/**
	 * @return the onBasePercentageExCoef
	 */
	public double getOnBasePercentageExCoef() {
		return onBasePercentageExCoef;
	}

	/**
	 * @param onBasePercentageExCoef the onBasePercentageExCoef to set
	 */
	public void setOnBasePercentageExCoef(double onBasePercentageExCoef) {
		this.onBasePercentageExCoef = onBasePercentageExCoef;
	}

	/**
	 * @return the sluggingCoef
	 */
	public double getSluggingCoef() {
		return sluggingCoef;
	}

	/**
	 * @param sluggingCoef the sluggingCoef to set
	 */
	public void setSluggingCoef(double sluggingCoef) {
		this.sluggingCoef = sluggingCoef;
	}

	/**
	 * @return the sluggingExCoef
	 */
	public double getSluggingExCoef() {
		return sluggingExCoef;
	}

	/**
	 * @param sluggingExCoef the sluggingExCoef to set
	 */
	public void setSluggingExCoef(double sluggingExCoef) {
		this.sluggingExCoef = sluggingExCoef;
	}
	
	/**
	 * Uses the algorithm defined by Aaron to calculate the expected runs this line up will generate versus opposing pitcher.
	 * 
	 * @param opposingPitcher
	 * @return
	 */
	public double calculateRunsVsOpposingPicther(MlbPitcher opposingPitcher) {
		log.info("Team: "+ this.team +" vs "+ opposingPitcher.getPitcher().getPlayer().getName());
		BigDecimal obp = calculateTeamOnBasePercentage();
		log.info("OBP: "+obp);
		BigDecimal slg = calculateTeamSlugging();
		log.info("slg: "+slg);
		
		BigDecimal effectiveOBP = obp.multiply(opposingPitcher.getOnBasePercentage()).divide(LeagueAverageOBP, 5, RoundingMode.HALF_UP);
		log.info("effectiveOBP: "+effectiveOBP);
		BigDecimal effectiveSlugging = slg.multiply(opposingPitcher.getSlugging()).divide(LeagueAverageSLG,5,RoundingMode.HALF_UP);
		log.info("effectiveSlugging: "+effectiveSlugging);


		double runsFromOBPvsStarter = 
				getOnBasePercentageCoef() * Math.pow(effectiveOBP.doubleValue(), getOnBasePercentageExCoef()) * 9/9;
				//Math.pow(getOnBasePercentageCoef() * effectiveOBP.doubleValue(), getOnBasePercentageExCoef()) * 9/9; // When I bring in innings I can fix the first 9
		double runsFromSLGvsStarter = 
				getSluggingCoef() * Math.pow(effectiveSlugging.doubleValue(), getSluggingExCoef()) * 9/9;
				//Math.pow(getSluggingCoef() * effectiveSlugging.doubleValue(), getSluggingExCoef()) * 9/9;
		
		double runsFromBsR = calculateTeamBsR().doubleValue()/162;
		double runsRemovedFromDoublePlays = opposingPitcher.getGroundBalls().doubleValue() * GDP_Coef.doubleValue();

		return (runsFromOBPvsStarter + runsFromSLGvsStarter + runsFromBsR + runsRemovedFromDoublePlays);
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

}
