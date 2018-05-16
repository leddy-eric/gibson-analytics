package com.gibson.analytics.core.baseball.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.enums.MlbTeamLookup;

/**
 * 
 * @author leddy.eric
 *
 */
public interface MlbGameDetailRepository extends JpaRepository<MlbGameDetail, Long> {

	/**
	 * Find all games for the given date.
	 * 
	 * @param date
	 * @return
	 */
	public List<MlbGameDetail> findByGameDate(LocalDate date);
	
	/**
	 * Find the corresponding Game detail from the api game id.
	 * 
	 * @param apiId
	 * @return
	 */
	public Optional<MlbGameDetail> findByApiId(String apiId);
	
	/**
	 * Find the last game for the given home team.
	 * 
	 * @param team
	 * @return
	 */
	public Optional<MlbGameDetail> findTopByHomeTeamOrderByCreated(MlbTeamLookup team);
	
	/**
	 * Find the last game for the given home team.
	 * 
	 * @param team
	 * @return
	 */
	public Optional<MlbGameDetail> findTopByAwayTeamOrderByCreated(MlbTeamLookup team);
}
