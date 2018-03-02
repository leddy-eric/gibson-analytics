package com.gibson.analytics.service;

import java.util.List;

import com.gibson.analytics.data.Game;
import com.gibson.analytics.data.GameStatistic;

public interface StatisticsService {

	List<GameStatistic> createStatistics(Game game);

}