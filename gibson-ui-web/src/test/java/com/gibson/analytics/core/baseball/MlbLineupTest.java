package com.gibson.analytics.core.baseball;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class MlbLineupTest extends BaseMlbLineupTest {

	@Before
	public void init() {
		// Test CLE
		addPlayer("Francisco Lindor", "SS", 2.972, 14.639, 0.357 ,0.532);
		addPlayer("Michael Brantley","LF", 2.8, -11.6, 0.362, 0.47);
		this.order.incrementAndGet();
		addPlayer("Yonder Alonso","1B", -2.6, -14.3, 0.355, 0.493);
		addPlayer("Melky Cabrera","LF", -3.8, -17.7, 0.329, 0.43);
		addPlayer("Jason Kipnis","2B", 3.3, 1.9, 0.304, 0.384);
		addPlayer("Roberto Perez","C", -1.3, 6.1, 0.299, 0.382);
		addPlayer("Greg Allen",	"CF", 1, -2.4, 0.309, 0.398);
		addPlayer("Trevor Bauer","P", 0, 0.7, 0.312, 0.406);

	}
	
	@Test
	public void testLineup() {
		MlbLineup lineup = getTestLineup();
		
		BigDecimal teamObp = lineup.calculateTeamOnBasePercentage();
		assertTrue( "Did not match expercted, was "+teamObp.doubleValue(), teamObp.doubleValue() ==  0.28916384508206895168788458);
	}
}
