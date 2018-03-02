package com.gibson.analytics.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class InitJobNotificationListener extends JobExecutionListenerSupport {
	final static Logger log = LoggerFactory.getLogger(InitJobNotificationListener.class);
	
	@Override
	public void afterJob(JobExecution jobExecution){
		log.debug("after job execution STATUS = " +jobExecution.getStatus());
	}
}
