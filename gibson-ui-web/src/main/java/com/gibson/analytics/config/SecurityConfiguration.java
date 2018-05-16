package com.gibson.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public interface SecurityConfiguration {

	WebSecurityConfigurerAdapter securityAdapter();

}