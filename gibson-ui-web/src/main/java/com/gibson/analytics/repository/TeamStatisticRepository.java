package com.gibson.analytics.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gibson.analytics.data.TeamStatistic;

@RepositoryRestResource(exported=false)
public interface TeamStatisticRepository extends PagingAndSortingRepository<TeamStatistic, Long> {

	public Optional<TeamStatistic> findDistinctByTeamIdAndName(String teamId, String name);
}
