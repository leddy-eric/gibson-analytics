package com.gibson.analytics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

/**
 * Simple controller for team names.
 * 
 * @author leddy.eric
 *
 */
@RestController
@RequestMapping("/team")
public class TeamController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	TeamRepository teamRepository;
	
    @RequestMapping(method = RequestMethod.GET)
	public List<Team> findAll() {
    	List<Team> response = new ArrayList<>();
    	
    	Iterable<Team> teams = 
    			teamRepository.findAll();
    	
    	for (Team team : teams) {
    		team.getTeamStatistics().size();
    		response.add(team);
		}
    	
		return response;
	}
    
    @RequestMapping(value = "/roster/{name}", method = RequestMethod.GET)
    public List<String> getRosterInfo(@PathVariable String name) {
    	 return playerRepository.findByTeam(name)
    			 	.stream()
    			 	.map(Player::getName)
    			 	.sorted()
    			 	.collect(Collectors.toList());
    }
    
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public List<Player> getRoster(@PathVariable String name) {
    	return playerRepository.findByTeam(name);
    }
	
}
