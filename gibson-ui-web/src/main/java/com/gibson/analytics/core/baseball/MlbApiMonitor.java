package com.gibson.analytics.core.baseball;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
		Scoreboard scoreboard = api.getScoreboard(date);
		
		scoreboard.getGames().stream().forEach(g -> refreshGameData(g));
	}


	/**
	 * 
	 * @param g
	 */
	private void refreshGameData(Game g) {
		refreshGameData(g, api.getLineup(g.getGameDataDirectory()));
	}


	/**
	 * 
	 * @param g
	 * @param lineup
	 */
	private void refreshGameData(Game g, Lineup lineup) {
		MlbGameDetail detail = gameService.createGameDetails(g, lineup);
		
		Optional<MlbGameDetail> gameDetail = repository.findByApiId(detail.getApiId());
		
		if(gameDetail.isPresent()) {
			refreshGameDetail(detail, gameDetail.get());			
		} else {
			saveGameDetail(detail);
		}
	}


	private void saveGameDetail(MlbGameDetail detail) {
		log.info("Save game "+detail.getApiId());
		MlbGameStatus status = detail.getStatus();
		
		if(MlbGameStatus.ESTIMATED == detail.getStatus()) {
			log.info("Create default lineups "+detail.getApiId());
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
		Optional<MlbGameDetail> awayGame = repository.findTopByAwayTeamOrderByCreated(team);
		Optional<MlbGameDetail> homeGame = repository.findTopByHomeTeamOrderByCreated(team);
		
		// Both present chose the latest
		if(awayGame.isPresent() && homeGame.isPresent()) {
			if(awayGame.get().getCreated() > homeGame.get().getCreated()) {
				return awayGame.get().getAway().getLineup();
			}
			
			return homeGame.get().getHome().getLineup();
		} else if(awayGame.isPresent()) {
			return awayGame.get().getAway().getLineup();
		} else if(homeGame.isPresent()) {
			return homeGame.get().getHome().getLineup();
		} 
		
		return new ArrayList<>();
	}


	private void refreshGameDetail(MlbGameDetail newDetail, MlbGameDetail oldDetail) {
		log.info("Refresh game "+newDetail.getApiId());
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void onStartup() {
		this.refreshGameData(LocalDate.now().minusDays(2), LocalDate.now().plusDays(5));
	}

}
