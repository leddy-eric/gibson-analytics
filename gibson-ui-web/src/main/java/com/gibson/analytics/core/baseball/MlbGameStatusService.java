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
		if(lineupDataExists(lineup)) {
			return statusFromApiData(game, lineup);
		} 
		
		return statusFromIncompleteData(game);
	}


	protected MlbGameStatus statusFromIncompleteData(Game game) {
		if(probablesAssigned(game)) {
			return MlbGameStatus.ESTIMATED;	
		}
		
		return MlbGameStatus.OPEN;
	}

	protected MlbGameStatus statusFromApiData(Game game, Lineup lineup) {
		if(gameIsComplete(game)) {
			return MlbGameStatus.FINAL;
		}
		
		return MlbGameStatus.RECOMMEND;
	}

	protected boolean gameIsComplete(Game game) {
		// TODO - When do we close the game (final scores)?
		return false;
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
		return lineup.getStatus().is2xxSuccessful();
	}
}
