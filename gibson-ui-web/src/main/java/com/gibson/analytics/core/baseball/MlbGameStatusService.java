package com.gibson.analytics.core.baseball;

import org.springframework.stereotype.Service;

import com.gibson.analytics.client.baseball.MlbMetadata;
import com.gibson.analytics.core.baseball.data.MlbGameStatus;
import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameTeam;
import com.gibson.analytics.data.Lineup;

@Service
public class MlbGameStatusService {

	/**
	 * Returns the status of a game based on the data available.
	 * 
	 * @param game - from api
	 * @param lineup - from api
	 * @return - the MlbGameStatus
	 */
	public MlbGameStatus statusFromApiResults(Game game, Lineup lineup) {
		if(gameIsComplete(game)) {
			return MlbGameStatus.FINAL;
		} else if(lineupDataExists(lineup) && probablesAssigned(game)) {
			return MlbGameStatus.RECOMMEND;
		} else if(probablesAssigned(game)) {
			return MlbGameStatus.ESTIMATED;
		}
		
		return MlbGameStatus.OPEN;
	}
	
	/**
	 * This means the game is complete from an estimation perspective only. 
	 * 
	 * @param game
	 * @return
	 */
	protected boolean gameIsComplete(Game game) {
		return game.getStatus().equals("Final") || game.getStatus().equals("In Progress");
	}

	protected boolean probablesAssigned(Game game) {
		return (hasProbable(game.getHome()) && hasProbable(game.getAway()));
	}

	protected boolean hasProbable(GameTeam team) {
		if(team.getMetadata() != null) {
			return team.getMetadata().containsKey(MlbMetadata.KEY_STARTER);
		}
		
		return false;
	}

	protected boolean lineupDataExists(Lineup lineup) {
		return lineup.getStatus().is2xxSuccessful() && lineup.isValid();
	}
}
