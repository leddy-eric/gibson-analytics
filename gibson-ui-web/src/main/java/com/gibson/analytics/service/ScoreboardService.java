package com.gibson.analytics.service;

import java.time.LocalDate;
import java.util.List;

import com.gibson.analytics.data.Scoreboard;

public interface ScoreboardService {

	List<Scoreboard> getScoreboard();

	List<Scoreboard> getScoreboard(LocalDate date);

}