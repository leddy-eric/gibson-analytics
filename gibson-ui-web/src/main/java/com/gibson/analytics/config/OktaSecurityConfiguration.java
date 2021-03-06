package com.gibson.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Profile("default")
public class OktaSecurityConfiguration implements SecurityConfiguration {
	
	/* (non-Javadoc)
	 * @see com.gibson.analytics.config.SecurityConfiguration#securityAdapter()
	 */
	@Override
	@Bean
	public WebSecurityConfigurerAdapter securityAdapter() {
		return new WebSecurityConfigurerAdapter() {
			protected void configure(HttpSecurity http) throws Exception {
				http
					.authorizeRequests()
						.anyRequest().authenticated()
						.and()
					.oauth2Login();
			}
		};
	}

}
