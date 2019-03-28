package com.gibson.analytics.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.repository.PlayerRepository;

@RestController
@RequestMapping("/player")
public class PlayerController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PlayerRepository playerRepository;
	
    @RequestMapping(method = RequestMethod.GET)
    public List<Player> getRoster(@RequestParam("name")  String name) {
    	return playerRepository.findByNameStartsWith(name);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Player getRoster(@PathVariable long id) {
    	return playerRepository
    			.findById(id)
    			.orElseThrow(ResourceNotFoundException::new);
    }


}
