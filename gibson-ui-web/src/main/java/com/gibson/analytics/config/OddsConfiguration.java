package com.gibson.analytics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.gibson.analytics.client.odds.OddsAPI;
import com.gibson.analytics.client.odds.OddsAPIConfiguration;
import com.gibson.analytics.client.odds.OddsAPIImpl;
import com.gibson.analytics.client.odds.OddsUriBuilder;
import com.gibson.analytics.service.OddsService;
import com.gibson.analytics.service.OddsServiceImpl;

@Configuration
@ConditionalOnProperty("sportsradar.api.key")
public class OddsConfiguration {
	
	@Autowired
	Environment environment;
	
	@Bean
	public OddsService oddsService() {
		return new OddsServiceImpl();
	}
	
	@Bean
	public OddsUriBuilder oddsUriBuilder(){
		OddsAPIConfiguration apiConfiguration = new OddsAPIConfiguration();
		
		String apiKey = environment.getProperty("sportsradar.api.key");
		System.out.println("******************** CONFIGURING ODDS API *******************");
		System.out.println("******************** API KEY: "+ apiKey +" *******************");
		apiConfiguration.setApiKey(apiKey);
		
		return new OddsUriBuilder(apiConfiguration);		
	}
	
	@Bean
	public OddsAPI oddsAPI() {
		return new OddsAPIImpl();
	}

}
