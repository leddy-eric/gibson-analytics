package com.gibson.analytics.core.baseball.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gibson.analytics.core.baseball.data.MlbGameDetail;
import com.gibson.analytics.core.baseball.data.MlbGameStatus;
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
	 * 
	 * @param team
	 * @return
	 */
	@Query("SELECT g FROM MlbGameDetail g where g.home.team = ?1 or g.away.team = ?1 and g.status in ('FINAL','RECOMMEND') ORDER BY created DESC")
	public List<MlbGameDetail> findLatestByTeam(MlbTeamLookup team);
}
