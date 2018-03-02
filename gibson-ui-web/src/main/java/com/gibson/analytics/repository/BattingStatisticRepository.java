package com.gibson.analytics.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gibson.analytics.data.BattingStatistic;

@RepositoryRestResource(exported=false)
public interface BattingStatisticRepository extends PagingAndSortingRepository<BattingStatistic, Long> {

}
