package com.gibson.analytics.init;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.gibson.analytics.data.Batter;
import com.gibson.analytics.data.BattingStatistic;
import com.gibson.analytics.data.NbaTeam;
import com.gibson.analytics.data.Player;
import com.gibson.analytics.init.processor.BatterProcessor;
import com.gibson.analytics.init.processor.BattingStatisticProcessor;
import com.gibson.analytics.init.processor.NbaTeamProcessor;
import com.gibson.analytics.init.processor.PlayerRowProcessor;
import com.gibson.analytics.init.writer.BatterWriter;
import com.gibson.analytics.repository.BattingStatisticRepository;
import com.gibson.analytics.repository.NbaTeamRepository;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

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
	public BattingStatisticRepository battingStatisticsRepository;
    
    @Autowired
    public TeamRepository teamRepository;

    @Value("classpath:2016PlayersWPark.csv")
    private Resource defaultPitcherData;
    
    @Value("classpath:2016PlayersWPark.csv")
    private Resource defaultPlayerData;
    
    @Value("classpath:Lineups.csv")
    private Resource defaultLineupData;
    
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
    	playerReader.setLineMapper(new CsvDataMapper(CsvPlayerConstants.HEADER));
    	
    	return playerReader;
    	
    }
    
    @Bean
    public FlatFileItemReader<Map<String, String>> lineupReader() {
    	FlatFileItemReader<Map<String, String>> lineupReader = new FlatFileItemReader<>();
    	
    	lineupReader.setResource(defaultLineupData);
    	
    	// Skip header
    	lineupReader.setLinesToSkip(1);
    	lineupReader.setLineMapper(new CsvDataMapper(CsvLineupConstants.HEADER));
    	
    	return lineupReader;
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
                .start(buildPlayers())
                .next(buildBattingStatistics())
                .next(buildTeamLineups())
                .next(buildNbaTeamStatistics())
                .build();
    }

    @Bean
    public Step buildPlayers() {
        return stepBuilderFactory.get("buildPlayers")
                .<Map<String, String>, Player> chunk(100)
                .reader(playerReader())
                .processor(new PlayerRowProcessor())
                .writer(player -> playerRepository.save(player))
                .build();
    }
    
    @Bean
    public Step buildBattingStatistics() {    	
        return stepBuilderFactory.get("buildBattingStatistics")
                .<Map<String, String>, List<BattingStatistic>> chunk(100)
                .reader(playerReader())
                .processor(new BattingStatisticProcessor())
                .writer(list -> list.forEach(stats -> battingStatisticsRepository.save(stats)))
                .build();
    }
    
    @Bean
    public Step buildNbaTeamStatistics() {
        return stepBuilderFactory.get("buildNbaTeamStatistics")
                .<Map<String, String>, NbaTeam> chunk(100)
                .reader(nbaTeamReader())
                .processor(new NbaTeamProcessor())
                .writer(nbaTeam -> nbaTeamRepository.save(nbaTeam))
                .build();
    }
    
    @Bean
    public Step buildTeamLineups(){
    	return stepBuilderFactory.get("buildTeamLineups")
    			.<Map<String, String>, Batter> chunk(400)
    			.reader(lineupReader())
    			.processor(new BatterProcessor())
    			.writer(new BatterWriter(playerRepository, teamRepository))
    			.build();
    }
    
}
