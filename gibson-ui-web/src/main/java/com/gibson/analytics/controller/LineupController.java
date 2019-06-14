package com.gibson.analytics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gibson.analytics.core.baseball.MlbLineupService;
import com.gibson.analytics.core.baseball.data.MlbGameActive;
import com.gibson.analytics.enums.MlbTeamLookup;

@RestController
@RequestMapping("/lineup")
public class LineupController {
	@Autowired
	MlbLineupService service;
	
	@RequestMapping
    public List<MlbGameActive> getDetail(@RequestParam("teamId") String id) {
		MlbTeamLookup team = MlbTeamLookup.lookupFrom(id);
		
		return service.findLatestGameLineup(team);
    }
}
