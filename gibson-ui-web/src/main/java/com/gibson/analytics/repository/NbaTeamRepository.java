package com.gibson.analytics.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gibson.analytics.data.NbaTeam;

@RepositoryRestResource(exported=false)
public interface NbaTeamRepository extends PagingAndSortingRepository<NbaTeam, String> {

}
