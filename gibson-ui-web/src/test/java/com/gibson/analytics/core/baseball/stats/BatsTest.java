package com.gibson.analytics.core.baseball.stats;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BatsTest {
	@Test
	public void testValueOf() {
		assertEquals(Bats.LEFT, Bats.valueOf(BigDecimal.valueOf(-1)));
		assertEquals(Bats.SWITCH, Bats.valueOf(BigDecimal.ZERO));
		assertEquals(Bats.RIGHT, Bats.valueOf(BigDecimal.ONE));
	}

}
