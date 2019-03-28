package com.gibson.analytics.init;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.SkipListenerSupport;

import com.gibson.analytics.data.Player;

public class PlayerSkipListener extends SkipListenerSupport<Map<String, String>, Player> {
	final static Logger log = LoggerFactory.getLogger(PlayerSkipListener.class);
	final AtomicInteger errors = new AtomicInteger();

	@Override
	public void onSkipInProcess(Map<String, String> item, Throwable t) {
		StringBuffer error = new StringBuffer("Exception thrown in processor (");
		int errorCount = errors.incrementAndGet();
		error.append(errorCount);
		error.append(") "); 
		error.append(t.getMessage());
		
		if(! UnsupportedOperationException.class.isAssignableFrom(t.getClass())) {
			error.append(" ").append(item.get(CsvPlayerConstants.COLUMN_PLAYERID));
		} 
		
		if(errorCount % 10 == 0) {
			log.warn(error.append(" - ").append(item.get(CsvPlayerConstants.COLUMN_PLAYERID)).toString());			
		}

	}
}
