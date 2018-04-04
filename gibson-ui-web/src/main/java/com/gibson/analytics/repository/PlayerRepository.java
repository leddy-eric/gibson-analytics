package com.gibson.analytics.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gibson.analytics.data.Player;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {

	@Query("SELECT DISTINCT team FROM Player")
	public List<String> findTeamNames();

	/**
	 * Find by team.
	 *
	 * @param name of the team
	 * @return the list of players on the team
	 */
	public List<Player> findByTeam(String team);
	
	/**
	 * 
	 * @param team
	 * @param status
	 * @return
	 */
	public List<Player> findByTeamAndStatus(String team, String status);
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Optional<Player> findByName(String name);
	
	/**
	 * Return all matches to the name.
	 * 
	 * @param name
	 * @return
	 */
	public List<Player> findByNameStartsWith(String name);

}
