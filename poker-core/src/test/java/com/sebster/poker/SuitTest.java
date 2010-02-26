package com.sebster.poker;

import org.junit.Assert;
import org.junit.Test;

public class SuitTest {

	@Test
	public void verifyOrdinals() throws Exception {
		Assert.assertEquals(0, Suit.CLUBS.ordinal());
		Assert.assertEquals(1, Suit.DIAMONDS.ordinal());
		Assert.assertEquals(2, Suit.HEARTS.ordinal());
		Assert.assertEquals(3, Suit.SPADES.ordinal());
	}

}
