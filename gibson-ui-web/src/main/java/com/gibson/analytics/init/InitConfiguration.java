package com.gibson.analytics.init;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.gibson.analytics.data.NbaTeam;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.data.TeamStatistic;
import com.gibson.analytics.init.processor.NbaTeamProcessor;
import com.gibson.analytics.init.processor.PitcherStatisticProcessor;
import com.gibson.analytics.init.processor.PlayerRowProcessor;
import com.gibson.analytics.init.processor.PlayerStatisticProcessor;
import com.gibson.analytics.init.processor.TeamStatisticProcessor;
import com.gibson.analytics.repository.NbaTeamRepository;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.PlayerStatisticRepository;
import com.gibson.analytics.repository.TeamRepository;
import com.gibson.analytics.repository.TeamStatisticRepository;

@Configuration
@EnableBatchProcessing
public class InitConfiguration {
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public PlayerRepository playerRepository;
    
    @Autowired
    public NbaTeamRepository nbaTeamRepository;
    
    @Autowired
	public PlayerStatisticRepository playerStatisticsRepository;
    
    @Autowired
    public TeamRepository teamRepository;
    
    @Autowired
	public TeamStatisticRepository teamStatisticsRepository;

    @Value("classpath:pitchers.csv")
    private Resource defaultPitcherData;
    
    @Value("classpath:batters.csv")
    private Resource defaultPlayerData;
    
    @Value("classpath:TeamData.csv")
    private Resource defaultTeamData;
    
    @Value("classpath:NBATeamRankings.csv")
    private Resource nbaTeamData;

    
    /**
     * Gets the player data.
     *
     * @return the player data
     * @throws IOException 
     */
    @Bean
    public CsvSource playerData() throws IOException {
    	CsvSource csvPlayer = new CsvSource();
    	csvPlayer.loadFrom(defaultPlayerData);
    	
    	return csvPlayer;
    }
    
    /**
     * Gets the pitcher data.
     *
     * @return the pitcher data
     */
    @Bean 
    public CsvSource pitcherData() {
    	CsvSource csvPitcher = new CsvSource();
    	csvPitcher.loadFrom(defaultPitcherData);
    	
    	return csvPitcher;
    }
    
    /**
     * Gets the nba team data.
     * 
     * @return
     */
    @Bean
    public CsvSource nbaTeamData() {
    	CsvSource csvNbaTeam = new CsvSource();
    	csvNbaTeam.loadFrom(nbaTeamData);
    	
    	return csvNbaTeam;
    }
    
    @Bean
    public FlatFileItemReader<Map<String, String>> playerReader() {
    	FlatFileItemReader<Map<String, String>> playerReader = new FlatFileItemReader<>();
    	
    	playerReader.setResource(defaultPlayerData);
    	// Skip header
    	playerReader.setLinesToSkip(1);
    	playerReader.setLineMapper(new CsvDataMapper(CsvPlayerConstants.SUMMARY_HEADER));
    	
    	return playerReader;
    	
    }
    
    @Bean
    public FlatFileItemReader<Map<String, String>> pitcherReader() {
    	FlatFileItemReader<Map<String, String>> pitcherReader = new FlatFileItemReader<>();
    	
    	pitcherReader.setResource(defaultPitcherData);
    	// Skip header
    	pitcherReader.setLinesToSkip(1);
    	pitcherReader.setLineMapper(new CsvDataMapper(CsvPitcherConstants.SUMMARY_HEADER));
    	
    	return pitcherReader;	
    }
    
    @Bean
    public FlatFileItemReader<Map<String, String>> teamReader() {
    	FlatFileItemReader<Map<String, String>> teamReader = new FlatFileItemReader<>();
    	
    	teamReader.setResource(defaultTeamData);
    	// Skip header
    	teamReader.setLinesToSkip(1);
    	teamReader.setLineMapper(new CsvDataMapper(CsvTeamConstants.SUMMARY_HEADER));
    	
    	return teamReader;	
    }
    
    @Bean
    public FlatFileItemReader<Map<String, String>> nbaTeamReader() {
    	FlatFileItemReader<Map<String, String>> nbaTeamReader = new FlatFileItemReader<>();
    	
    	nbaTeamReader.setResource(nbaTeamData);
    	
    	// Skip header
    	nbaTeamReader.setLinesToSkip(1);
    	nbaTeamReader.setLineMapper(new CsvDataMapper(CsvNbaTeamConstants.HEADER));
    	
    	return nbaTeamReader;
    }
    
    
    @Bean
    public Job initializationJob(InitJobNotificationListener listener) {
        return jobBuilderFactory.get("initializationJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                //.start(buildPlayers())
                .start(buildBattingStatistics())
                .next(buildPitchingStatistics())
                .next(buildTeamStatistics())
                .next(buildNbaTeamStatistics())
                .build();
    }

//	@Bean
//    public Step buildPlayers() {
//		return stepBuilderFactory.get("buildPlayers")
//                .<Map<String, String>, Player> chunk(100)
//                .reader(playerReader())
//                .processor(new PlayerRowProcessor(playerRepository))
//                .faultTolerant()
//                .skipLimit(500)
//                .skip(UnsupportedOperationException.class)
//                .listener(playerSkipListener())
//                .writer(player -> playerRepository.saveAll(player))
//                .build();
//    }
//	
//	private SkipListener<Map<String, String>, Player> playerSkipListener(){
//		return new PlayerSkipListener();
//	}
    
    @Bean
    public Step buildBattingStatistics() {    	
        return stepBuilderFactory.get("buildBattingStatistics")
                .<Map<String, String>, List<PlayerStatistic>> chunk(100)
                .reader(playerReader())
                .processor(new PlayerStatisticProcessor(playerRepository))
                .faultTolerant()
                .skipLimit(10)
                .skip(IncorrectResultSizeDataAccessException.class)
                .listener(new SkipListenerSupport<>())
                .writer(list -> list.forEach(stats -> playerStatisticsRepository.saveAll(stats)))
                .build();
    }
    
    @Bean
    public Step buildPitchingStatistics() {
        return stepBuilderFactory.get("buildPitchingStatistics")
                .<Map<String, String>, List<PlayerStatistic>> chunk(100)
                .reader(pitcherReader())
                .processor(new PitcherStatisticProcessor(playerRepository))
                .faultTolerant()
                .skipLimit(10)
                .skip(IncorrectResultSizeDataAccessException.class)
                .listener(new SkipListenerSupport<>())
                .writer(list -> list.forEach(stats -> playerStatisticsRepository.saveAll(stats)))
                .build();
    }
    
    @Bean
    public Step buildTeamStatistics() {
        return stepBuilderFactory.get("buildTeamStatistics")
                .<Map<String, String>, List<TeamStatistic>> chunk(100)
                .reader(teamReader())
                .processor(new TeamStatisticProcessor(teamRepository))
                .faultTolerant()
                .skipLimit(10)
                .skip(IncorrectResultSizeDataAccessException.class)
                .listener(new SkipListenerSupport<>())
                .writer(list -> list.forEach(stats -> teamStatisticsRepository.saveAll(stats)))
                .build();
    }
    
    @Bean
    public Step buildNbaTeamStatistics() {
        return stepBuilderFactory.get("buildNbaTeamStatistics")
                .<Map<String, String>, NbaTeam> chunk(100)
                .reader(nbaTeamReader())
                .processor(new NbaTeamProcessor())
                .writer(nbaTeam -> nbaTeamRepository.saveAll(nbaTeam))
                .build();
    }
    
}
