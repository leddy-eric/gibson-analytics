package com.gibson.analytics.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gibson.analytics.data.Team;

@RepositoryRestResource(exported=false)
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

}
