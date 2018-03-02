package com.gibson.analytics.client.hockey;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class HockeyTeamCodes {
	HashMap<String, String> apiCodes = new HashMap<>();

	
	@PostConstruct
	public void init() {
	}
}
