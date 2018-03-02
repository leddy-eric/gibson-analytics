package com.gibson.analytics.client;

import java.util.Arrays;

import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GibsonRserveClient {
	
	private static final Logger log = LoggerFactory.getLogger(GibsonRserveClient.class);
	
	@Test
	public void testClientConnection() throws Exception {
		RConnection c = new RConnection("127.0.0.1", 6311);
		REXP x = c.eval("R.version.string");
		System.out.println(x.asString());
		
		String playerIds = "noop";
		String frame = "noop";
		
		c.assign("assignPlayerIds", playerIds);
		c.assign("assignDataFrame", frame);
		
		String expression = "TeamBuilder('TeamName', assignPlayerIds, assignDataFrame)";
		
		REXP rResponseObject = c.parseAndEval("try(eval(hello('nonesense')),silent=TRUE)"); 
		if (rResponseObject.inherits("try-error")) { 
			log.error("R Serve Eval Exception : "+ rResponseObject.asString()); 
		} else {
			Arrays.asList(rResponseObject.asStrings()).forEach(s -> log.info(s));
		}
	}
}
