package com.gibson.analytics.core.baseball;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gibson.analytics.client.BaseballAPI;
import com.gibson.analytics.core.baseball.data.MlbGameActive;
import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.data.MlbGameRoster;
import com.gibson.analytics.core.baseball.data.MlbGameStatus;
import com.gibson.analytics.core.baseball.repository.MlbGameDetailRepository;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.Lineup;
import com.gibson.analytics.data.Scoreboard;
import com.gibson.analytics.enums.MlbTeamLookup;

/**
 * This class periodically checks the MLB API for game details. It is responsible for updating the database with 
 * the latest information and statistics currently published in the API.
 * 
 * @author leddy.eric
 *
 */
@Component
public class MlbApiMonitor {
	
	final static Logger log = LoggerFactory.getLogger(MlbApiMonitor.class);
	
	@Autowired
	private BaseballAPI api;
	
	@Autowired
	private MlbGameService gameService;
	
	@Autowired
	private MlbGameDetailRepository repository; 
	
	@Autowired
	private JobLauncher jobLauncher;
	
    @Autowired
    private Job job;
    
    @Autowired
    private Environment environment;
	
	// Used to build the first pass
	private AtomicBoolean initializing = new AtomicBoolean(true);


	/**
	 * Refresh the game date for the 
	 * @param start
	 * @param end
	 */
	public void refreshGameData(LocalDate start, LocalDate end) {
		int days = Period.between(start, end).getDays();

		for (int i = 0; i < days; i++) {
			refreshGameData(start.plusDays(i));
		}
	}


	/**
	 * Refresh the game data for the date specified.
	 * 
	 * @param date
	 */
	public void refreshGameData(LocalDate date) {
		try {
			Scoreboard scoreboard = api.getScoreboard(date);
			scoreboard.getGames().stream().forEach(g -> refreshGameData(g));			
		} catch (Exception e) {
			log.error("Error refreshing game data "+date, e);
		}

	}
	
	@Scheduled(cron="0 0/5 * * * *")
	public void refreshGameData() {
		if(!initializing.get()) {
			refreshGameData(LocalDate.now());
		} else {
			log.warn("Skipping refreshGameData... app initializing");
		}
	}


	/**
	 * 
	 * @param g
	 */
	private void refreshGameData(Game g) {
		try {
			refreshGameData(g, api.getLineup(g.getGameDataDirectory()));			
		} catch (Exception e) {
			log.error("Error refreshing game data "+ g.getId(), e);
		}
	}


	/**
	 * 
	 * @param g
	 * @param lineup
	 */
	private void refreshGameData(Game g, Lineup lineup) {
		MlbGameDetail latest = gameService.createGameDetails(g, lineup);

		Optional<MlbGameDetail> existing = repository.findByApiId(latest.getApiId());

		if(existing.isPresent()) {
			refreshGameDetail(latest, existing.get());			
		} else {
			// Do not save garbage lineups
			if(MlbGameStatus.FINAL == latest.getStatus()) {
				latest.getHome().setLineup(null);
				latest.getAway().setLineup(null);
			}
			
			saveGameDetail(latest);
		} 


	}


	private void saveGameDetail(MlbGameDetail detail) {
		log.debug("Save game "+ detail.getApiId());
		MlbGameStatus status = detail.getStatus();
		
		if(MlbGameStatus.ESTIMATED == detail.getStatus()) {
			log.debug("Create default lineups "+detail.getApiId());
			List<MlbGameActive> homeLineup = createDefaultLineup(detail.getHome());
			List<MlbGameActive> awayLineup = createDefaultLineup(detail.getAway());
			
			detail.getHome().setLineup(homeLineup);
			detail.getAway().setLineup(awayLineup);
		}
		
		repository.save(detail);
	}


	private List<MlbGameActive> createDefaultLineup(MlbGameRoster roster) {
		List<MlbGameActive> defaultLineup = new ArrayList<>();
		List<MlbGameActive> lineup = findLatestLineup(roster.getTeam());
		
		for (MlbGameActive lastActive : lineup) {
			MlbGameActive active = new MlbGameActive();
			active.setBattingOrder(lastActive.getBattingOrder());
			active.setPosition(lastActive.getPosition());
			active.setPlayer(lastActive.getPlayer());
			defaultLineup.add(active);
		}
		
		return defaultLineup;
	}


	/**
	 * This is a bad way of doing this, add a custom query in the repo to figure this out.
	 * 
	 * @param team
	 * @return
	 */
	private List<MlbGameActive> findLatestLineup(MlbTeamLookup team) {
		Optional<MlbGameDetail> latest = repository.findLatestByTeam(team).stream().findFirst();
		
		if(latest.isPresent()) {			
			MlbGameRoster away = latest.get().getAway();
			MlbGameRoster home = latest.get().getHome();
			
			if(team.equals(away.getTeam())) {
				return away.getLineup();
			} else {
				return home.getLineup();
			}
		}
		
		return new ArrayList<>();
	}


	private void refreshGameDetail(MlbGameDetail newDetail, MlbGameDetail oldDetail) {
		if(statusChanged(newDetail, oldDetail)) {
			log.info("Refresh game "+newDetail.getApiId());
			MlbGameStatus status = newDetail.getStatus();
			
			if(status == MlbGameStatus.ESTIMATED) {
				log.info("Refresh default lineups "+newDetail.getApiId());
				List<MlbGameActive> homeLineup = createDefaultLineup(newDetail.getHome());
				List<MlbGameActive> awayLineup = createDefaultLineup(newDetail.getAway());
				
				newDetail.getHome().setLineup(homeLineup);
				newDetail.getAway().setLineup(awayLineup);
				
				oldDetail.setHome(newDetail.getHome());
				oldDetail.setAway(newDetail.getAway());
				
			} else if(status == MlbGameStatus.RECOMMEND) {
				log.info("Refresh api lineups "+newDetail.getApiId());
				
				oldDetail.setHome(newDetail.getHome());
				oldDetail.setAway(newDetail.getAway());
			} 

			oldDetail.setStatus(status);
			
			repository.save(oldDetail);			
		}
	}

	private boolean statusChanged(MlbGameDetail newDetail, MlbGameDetail oldDetail) {
		return newDetail.getStatus() != oldDetail.getStatus();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		log.info("ApplicationReadyEvent recieved - Launching init Job ");
		startupInitialization();
	}

	/**
	 * Wrap the startup logic and handle the exceptions
	 */
	private void startupInitialization() {
		try {
			JobExecution execution = jobLauncher.run(job, new JobParameters());
			
			boolean initOnStartup = Boolean.parseBoolean(environment.getProperty("mlb.refresh.on.init", "true"));
			int start = Integer.parseInt(environment.getProperty("mlb.refresh.window.min", "1"));
			int end = Integer.parseInt(environment.getProperty("mlb.refresh.window.max", "3"));
			
			if(ExitStatus.COMPLETED.equals(execution.getExitStatus()) && initOnStartup) {
				log.info("Init Job Completed - Launching refresh data ");
				this.refreshGameData(LocalDate.now().minusDays(start), LocalDate.now().plusDays(end));			
			} else {
				log.error("Startup Initialization failed exit status was -" + execution.getExitStatus());
			}
		} catch (JobExecutionAlreadyRunningException e) {
			log.error("Startup Initialization failed" , e);
		} catch (JobRestartException e) {
			log.error("Startup Initialization failed" , e);
		} catch (JobInstanceAlreadyCompleteException e) {
			log.error("Startup Initialization failed" , e);
		} catch (JobParametersInvalidException e) {
			log.error("Startup Initialization failed" , e);
		} finally {
			initializing.set(false);
		}
	}

}
