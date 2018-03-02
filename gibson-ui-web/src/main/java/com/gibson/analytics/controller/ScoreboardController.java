package com.gibson.analytics.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gibson.analytics.data.Scoreboard;
import com.gibson.analytics.service.ScoreboardService;

@RestController
@RequestMapping("/scoreboard")
public class ScoreboardController {

	@Autowired
	ScoreboardService service;


	@RequestMapping(value = "/{date}", method = RequestMethod.GET)
	public List<Scoreboard> getScoreboardByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
		return service.getScoreboard(date.toLocalDate());
	}

	@RequestMapping(value = "/today", method = RequestMethod.GET)
	public List<Scoreboard> getScoreboard(){
		return service.getScoreboard();
	}

}
