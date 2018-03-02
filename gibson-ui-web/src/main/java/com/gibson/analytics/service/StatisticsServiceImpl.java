package com.gibson.analytics.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;
import com.gibson.analytics.data.NbaTeam;
import com.gibson.analytics.data.PointValue;
import com.gibson.analytics.repository.NbaTeamRepository;

/**
 * 
 * @author leddy.eric
 *
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private TreeMap<BigDecimal, PointValue> valueSpreadMap = new TreeMap<>();

	@Autowired
	NbaTeamRepository repository;

	/* (non-Javadoc)
	 * @see com.gibson.analytics.service.StatisticsService#createStatistics(com.gibson.analytics.data.Game)
	 */
	@Override
	public List<GameStatistic> createStatistics(Game game) {
		List<GameStatistic> statistics = new ArrayList<>();
		String league = game.getLeague();

		log.info("Adding statistics for "+game.getId());

		NbaTeam awayTeam = repository.findOne(game.getAway().getName());
		NbaTeam homeTeam = repository.findOne(game.getHome().getName());

		if(awayTeam != null && homeTeam != null) {
			GameStatistic score = createScore(awayTeam, homeTeam);
			GameStatistic scoreMoving = createScoreMoving(awayTeam, homeTeam);
			GameStatistic spread = createSpread(awayTeam, homeTeam);
			GameStatistic spreadMoving = createSpreadMoving(awayTeam, homeTeam);
			GameStatistic valueSpread = createValueSpread(awayTeam, homeTeam);
			GameStatistic valueSpreadMoving = createValueSpreadMoving(awayTeam, homeTeam);	


			// Add Statistics
			statistics.add(score);
			statistics.add(scoreMoving);
			//statistics.add(spread);
			//statistics.add(spreadMoving);
			statistics.add(valueSpread);
			statistics.add(valueSpreadMoving);
		} else {
			log.error("Could not resolve team " +game.getAway().getName() +" or "+ game.getHome().getName() + " available names are: " );
			//repository.findAll().forEach(t -> log.debug(t.getName()));
			statistics.add(new GameStatistic("N/A", "N/A"));
			statistics.add(new GameStatistic("N/A", "N/A"));
			statistics.add(new GameStatistic("N/A", "N/A"));
			statistics.add(new GameStatistic("N/A", "N/A"));
			statistics.add(new GameStatistic("N/A", "N/A"));
			statistics.add(new GameStatistic("N/A", "N/A"));
		}

		return statistics;
	}

	/**
	 * 
	 * @param awayTeam
	 * @param homeTeam
	 * @return
	 */
	private GameStatistic createValueSpreadMoving(NbaTeam awayTeam, NbaTeam homeTeam) {
		BigDecimal adjustedValueScore = 
				new BigDecimal(awayTeam.getValueScoreMovingAverage()).subtract(new BigDecimal(homeTeam.getValueScoreMovingAverage()));
		
		log.info("Adjusted Value Score: "+ adjustedValueScore);

		Entry<BigDecimal, PointValue> floor = valueSpreadMap.floorEntry(adjustedValueScore);
		Entry<BigDecimal, PointValue> ceiling = valueSpreadMap.ceilingEntry(adjustedValueScore);
		
		if(floor == null) {
			return new GameStatistic("Spread Moving", ceiling.getValue().getSpread().toString());
		} else if(ceiling == null) {
			return new GameStatistic("Spread Moving", floor.getValue().getSpread().toString());
		} else {
			PointValue floorValue = floor.getValue();
			PointValue ceilingValue = ceiling.getValue();
			
			log.info("Floor: "+ floorValue.getValue());
			log.info("Ceiling :"+ ceilingValue.getValue());	
			
			BigDecimal differenceToFloor = adjustedValueScore.subtract(floorValue.getValue());
			BigDecimal differenceToCeiling = ceilingValue.getValue().subtract(adjustedValueScore);
			
			
			if(differenceToFloor.compareTo(differenceToCeiling) < 0) {
				return new GameStatistic("Spread Moving", floor.getValue().getSpread().setScale(1, RoundingMode.HALF_DOWN).toString());
			} 
			
			return new GameStatistic("Spread Moving", ceiling.getValue().getSpread().setScale(1, RoundingMode.HALF_DOWN).toString());			

		}

	}

	/**
	 * 
	 * @param awayTeam
	 * @param homeTeam
	 * @return
	 */
	private GameStatistic createValueSpread(NbaTeam awayTeam, NbaTeam homeTeam) {
		BigDecimal adjustedValueScore = 
				new BigDecimal(awayTeam.getValueScore()).subtract(new BigDecimal(homeTeam.getValueScore()));
		
		log.info("Adjusted Value Score: "+ adjustedValueScore);

		Entry<BigDecimal, PointValue> floor = valueSpreadMap.floorEntry(adjustedValueScore);
		Entry<BigDecimal, PointValue> ceiling = valueSpreadMap.ceilingEntry(adjustedValueScore);
		
		if(floor == null) {
			return new GameStatistic("Spread", ceiling.getValue().getSpread().setScale(1, RoundingMode.HALF_DOWN).toString());
		} else if(ceiling == null) {
			return new GameStatistic("Spread", floor.getValue().getSpread().setScale(1, RoundingMode.HALF_DOWN).toString());
		} else {
			PointValue floorValue = floor.getValue();
			PointValue ceilingValue = ceiling.getValue();
			
			log.info("Floor: "+ floorValue.getValue());
			log.info("Ceiling :"+ ceilingValue.getValue());	
			
			BigDecimal differenceToFloor = adjustedValueScore.subtract(floorValue.getValue());
			BigDecimal differenceToCeiling = ceilingValue.getValue().subtract(adjustedValueScore);
			
			
			if(differenceToFloor.compareTo(differenceToCeiling) < 0) {
				return new GameStatistic("Spread", floor.getValue().getSpread().setScale(1, RoundingMode.HALF_DOWN).toString());
			} 
			
			return new GameStatistic("Spread", ceiling.getValue().getSpread().setScale(1, RoundingMode.HALF_DOWN).toString());			
		}
	}

	/**
	 * Away team spread minus home team spread minus 3.5
	 * 
	 * @param awayTeam
	 * @param homeTeam
	 * @return
	 */
	private GameStatistic createSpreadMoving(NbaTeam awayTeam, NbaTeam homeTeam) {
		BigDecimal spreadMoving = new BigDecimal(awayTeam.getSpreadScoreMovingAverage())
				.subtract(new BigDecimal(homeTeam.getSpreadScoreMovingAverage()))
				.subtract(new BigDecimal(3.5)).setScale(1, RoundingMode.HALF_DOWN);

		return new GameStatistic("Spread Moving Average", spreadMoving.toString());
	}

	/**
	 * Away team spread minus home team spread minus 3.5
	 * 
	 * @param awayTeam
	 * @param homeTeam
	 * @return
	 */
	private GameStatistic createSpread(NbaTeam awayTeam, NbaTeam homeTeam) {
		BigDecimal spread = new BigDecimal(awayTeam.getSpreadScore())
				.subtract(new BigDecimal(homeTeam.getSpreadScore()))
				.subtract(new BigDecimal(3.5)).setScale(1, RoundingMode.HALF_DOWN);


		return new GameStatistic("Spread", spread.toString());
	}

	/**
	 * 
	 * @param awayTeam
	 * @param homeTeam
	 * @return
	 */
	private GameStatistic createScoreMoving(NbaTeam awayTeam, NbaTeam homeTeam) {
		BigDecimal homeScoreMoving = new BigDecimal(homeTeam.getScoreMovingAverage());
		BigDecimal awayScoreMoving = new BigDecimal(awayTeam.getScoreMovingAverage());

		BigDecimal overUnderValueMoving = homeScoreMoving.add(awayScoreMoving).add(new BigDecimal(210)).setScale(1, RoundingMode.HALF_DOWN);

		return new GameStatistic("Score Moving Average", overUnderValueMoving.toString());
	}

	/**
	 * 
	 * @param awayTeam
	 * @param homeTeam
	 * @return
	 */
	private GameStatistic createScore(NbaTeam awayTeam, NbaTeam homeTeam) {
		BigDecimal homeScore = new BigDecimal(homeTeam.getScore());
		BigDecimal awayScore = new BigDecimal(awayTeam.getScore());

		BigDecimal overUnderValue = homeScore.add(awayScore).add(new BigDecimal(210)).setScale(1, RoundingMode.HALF_DOWN);

		return new GameStatistic("Score", overUnderValue.toString());
	}
	
	
	@PostConstruct
	public void init() {
		valueSpreadMap.put(new BigDecimal("-0.477462143444066"), new PointValue("-25","-0.477462143444066","0.0511797159290997"));
		valueSpreadMap.put(new BigDecimal("-0.473764526352858"), new PointValue("-24.5","-0.473764526352858","0.0548773330203075"));
		valueSpreadMap.put(new BigDecimal("-0.47006690926165"), new PointValue("-24","-0.47006690926165","0.0585749501115154"));
		valueSpreadMap.put(new BigDecimal("-0.466193215166099"), new PointValue("-23.5","-0.466193215166099","0.0624486442070666"));
		valueSpreadMap.put(new BigDecimal("-0.462319521070548"), new PointValue("-23","-0.462319521070548","0.0663223383026177"));
		valueSpreadMap.put(new BigDecimal("-0.4575067496185"), new PointValue("-22.5","-0.4575067496185","0.071135109754666"));
		valueSpreadMap.put(new BigDecimal("-0.452693978166451"), new PointValue("-22","-0.452693978166451","0.0759478812067144"));
		valueSpreadMap.put(new BigDecimal("-0.447352975701373"), new PointValue("-21.5","-0.447352975701373","0.0812888836717925"));
		valueSpreadMap.put(new BigDecimal("-0.442011973236295"), new PointValue("-21","-0.442011973236295","0.0866298861368705"));
		valueSpreadMap.put(new BigDecimal("-0.436025355088625"), new PointValue("-20.5","-0.436025355088625","0.0926165042845404"));
		valueSpreadMap.put(new BigDecimal("-0.430038736940956"), new PointValue("-20","-0.430038736940956","0.0986031224322104"));
		valueSpreadMap.put(new BigDecimal("-0.423934734123723"), new PointValue("-19.5","-0.423934734123723","0.104707125249442"));
		valueSpreadMap.put(new BigDecimal("-0.417830731306491"), new PointValue("-19","-0.417830731306491","0.110811128066674"));
		valueSpreadMap.put(new BigDecimal("-0.41178542082404"), new PointValue("-18.5","-0.41178542082404","0.116856438549125"));
		valueSpreadMap.put(new BigDecimal("-0.405740110341589"), new PointValue("-18","-0.405740110341589","0.122901749031576"));
		valueSpreadMap.put(new BigDecimal("-0.398051414485268"), new PointValue("-17.5","-0.398051414485268","0.130590444887898"));
		valueSpreadMap.put(new BigDecimal("-0.390362718628947"), new PointValue("-17","-0.390362718628947","0.138279140744219"));
		valueSpreadMap.put(new BigDecimal("-0.379915483037915"), new PointValue("-16.5","-0.379915483037915","0.148726376335251"));
		valueSpreadMap.put(new BigDecimal("-0.369468247446883"), new PointValue("-16","-0.369468247446883","0.159173611926282"));
		valueSpreadMap.put(new BigDecimal("-0.358903627186289"), new PointValue("-15.5","-0.358903627186289","0.169738232186876"));
		valueSpreadMap.put(new BigDecimal("-0.348339006925696"), new PointValue("-15","-0.348339006925696","0.18030285244747"));
		valueSpreadMap.put(new BigDecimal("-0.338537387017256"), new PointValue("-14.5","-0.338537387017256","0.19010447235591"));
		valueSpreadMap.put(new BigDecimal("-0.328735767108816"), new PointValue("-14","-0.328735767108816","0.19990609226435"));
		valueSpreadMap.put(new BigDecimal("-0.316938607817819"), new PointValue("-13.5","-0.316938607817819","0.211703251555347"));
		valueSpreadMap.put(new BigDecimal("-0.305141448526822"), new PointValue("-13","-0.305141448526822","0.223500410846343"));
		valueSpreadMap.put(new BigDecimal("-0.292698673553234"), new PointValue("-12.5","-0.292698673553234","0.235943185819932"));
		valueSpreadMap.put(new BigDecimal("-0.280255898579646"), new PointValue("-12","-0.280255898579646","0.24838596079352"));
		valueSpreadMap.put(new BigDecimal("-0.266580584575654"), new PointValue("-11.5","-0.266580584575654","0.262061274797511"));
		valueSpreadMap.put(new BigDecimal("-0.252905270571663"), new PointValue("-11","-0.252905270571663","0.275736588801503"));
		valueSpreadMap.put(new BigDecimal("-0.238643033219861"), new PointValue("-10.5","-0.238643033219861","0.289998826153304"));
		valueSpreadMap.put(new BigDecimal("-0.22438079586806"), new PointValue("-10","-0.22438079586806","0.304261063505106"));
		valueSpreadMap.put(new BigDecimal("-0.208123019133701"), new PointValue("-9.5","-0.208123019133701","0.320518840239465"));
		valueSpreadMap.put(new BigDecimal("-0.191865242399343"), new PointValue("-9","-0.191865242399343","0.336776616973823"));
		valueSpreadMap.put(new BigDecimal("-0.174257541965019"), new PointValue("-8.5","-0.174257541965019","0.354384317408146"));
		valueSpreadMap.put(new BigDecimal("-0.156649841530696"), new PointValue("-8","-0.156649841530696","0.37199201784247"));
		valueSpreadMap.put(new BigDecimal("-0.136577063035568"), new PointValue("-7.5","-0.136577063035568","0.392064796337598"));
		valueSpreadMap.put(new BigDecimal("-0.116504284540439"), new PointValue("-7","-0.116504284540439","0.412137574832727"));
		valueSpreadMap.put(new BigDecimal("-0.100070430801737"), new PointValue("-6.5","-0.100070430801737","0.428571428571429"));
		valueSpreadMap.put(new BigDecimal("-0.0836365770630356"), new PointValue("-6","-0.0836365770630356","0.44500528231013"));
		valueSpreadMap.put(new BigDecimal("-0.0649724146026529"), new PointValue("-5.5","-0.0649724146026529","0.463669444770513"));
		valueSpreadMap.put(new BigDecimal("-0.0463082521422702"), new PointValue("-5","-0.0463082521422702","0.482333607230896"));
		valueSpreadMap.put(new BigDecimal("-0.0312830144383144"), new PointValue("-4.5","-0.0312830144383144","0.497358844934851"));
		valueSpreadMap.put(new BigDecimal("-0.0162577767343585"), new PointValue("-4","-0.0162577767343585","0.512384082638807"));
		valueSpreadMap.put(new BigDecimal("0"), new PointValue("-3.5","0","0.528641859373166"));
		valueSpreadMap.put(new BigDecimal("0.0162577767343585"), new PointValue("-3","0.0162577767343585","0.544899636107524"));
		valueSpreadMap.put(new BigDecimal("0.0296983213992252"), new PointValue("-2.5","0.0296983213992252","0.558340180772391"));
		valueSpreadMap.put(new BigDecimal("0.043138866064092"), new PointValue("-2","0.043138866064092","0.571780725437258"));
		valueSpreadMap.put(new BigDecimal("0.0535274093203427"), new PointValue("-1.5","0.0535274093203427","0.582169268693509"));
		valueSpreadMap.put(new BigDecimal("0.0639159525765934"), new PointValue("-1","0.0639159525765934","0.592557811949759"));
		valueSpreadMap.put(new BigDecimal("0.0685819931916891"), new PointValue("-0.5","0.0685819931916891","0.597223852564855"));
		valueSpreadMap.put(new BigDecimal("0.0732480338067848"), new PointValue("0","0.0732480338067848","0.601889893179951"));
		valueSpreadMap.put(new BigDecimal("0.0779140744218805"), new PointValue("0.5","0.0779140744218805","0.606555933795046"));
		valueSpreadMap.put(new BigDecimal("0.0825801150369762"), new PointValue("1","0.0825801150369762","0.611221974410142"));
		valueSpreadMap.put(new BigDecimal("0.0959619673670619"), new PointValue("1.5","0.0959619673670619","0.624603826740228"));
		valueSpreadMap.put(new BigDecimal("0.109343819697148"), new PointValue("2","0.109343819697148","0.637985679070313"));
		valueSpreadMap.put(new BigDecimal("0.122373518018547"), new PointValue("2.5","0.122373518018547","0.651015377391713"));
		valueSpreadMap.put(new BigDecimal("0.135403216339946"), new PointValue("3","0.135403216339946","0.664045075713112"));
		valueSpreadMap.put(new BigDecimal("0.149724146026529"), new PointValue("3.5","0.149724146026529","0.678366005399695"));
		valueSpreadMap.put(new BigDecimal("0.164045075713112"), new PointValue("4","0.164045075713112","0.692686935086278"));
		valueSpreadMap.put(new BigDecimal("0.177837774386665"), new PointValue("4.5","0.177837774386665","0.706479633759831"));
		valueSpreadMap.put(new BigDecimal("0.191630473060218"), new PointValue("5","0.191630473060218","0.720272332433384"));
		valueSpreadMap.put(new BigDecimal("0.207770865125015"), new PointValue("5.5","0.207770865125015","0.736412724498181"));
		valueSpreadMap.put(new BigDecimal("0.223911257189811"), new PointValue("6","0.223911257189811","0.752553116562977"));
		valueSpreadMap.put(new BigDecimal("0.237703955863364"), new PointValue("6.5","0.237703955863364","0.76634581523653"));
		valueSpreadMap.put(new BigDecimal("0.251496654536917"), new PointValue("7","0.251496654536917","0.780138513910083"));
		valueSpreadMap.put(new BigDecimal("0.26470242986266"), new PointValue("7.5","0.26470242986266","0.793344289235826"));
		valueSpreadMap.put(new BigDecimal("0.277908205188402"), new PointValue("8","0.277908205188402","0.806550064561568"));
		valueSpreadMap.put(new BigDecimal("0.28923582580115"), new PointValue("8.5","0.28923582580115","0.817877685174316"));
		valueSpreadMap.put(new BigDecimal("0.300563446413898"), new PointValue("9","0.300563446413898","0.829205305787064"));
		valueSpreadMap.put(new BigDecimal("0.311186759009273"), new PointValue("9.5","0.311186759009273","0.839828618382439"));
		valueSpreadMap.put(new BigDecimal("0.321810071604648"), new PointValue("10","0.321810071604648","0.850451930977814"));
		valueSpreadMap.put(new BigDecimal("0.330731306491372"), new PointValue("10.5","0.330731306491372","0.859373165864538"));
		valueSpreadMap.put(new BigDecimal("0.339652541378096"), new PointValue("11","0.339652541378096","0.868294400751262"));
		valueSpreadMap.put(new BigDecimal("0.348339006925696"), new PointValue("11.5","0.348339006925696","0.876980866298861"));
		valueSpreadMap.put(new BigDecimal("0.357025472473295"), new PointValue("12","0.357025472473295","0.885667331846461"));
		valueSpreadMap.put(new BigDecimal("0.362659936612278"), new PointValue("12.5","0.362659936612278","0.891301795985444"));
		valueSpreadMap.put(new BigDecimal("0.368294400751262"), new PointValue("13","0.368294400751262","0.896936260124428"));
		valueSpreadMap.put(new BigDecimal("0.375044019251086"), new PointValue("13.5","0.375044019251086","0.903685878624252"));
		valueSpreadMap.put(new BigDecimal("0.38179363775091"), new PointValue("14","0.38179363775091","0.910435497124076"));
		valueSpreadMap.put(new BigDecimal("0.386547716868177"), new PointValue("14.5","0.386547716868177","0.915189576241343"));
		valueSpreadMap.put(new BigDecimal("0.391301795985444"), new PointValue("15","0.391301795985444","0.91994365535861"));
	}

}
