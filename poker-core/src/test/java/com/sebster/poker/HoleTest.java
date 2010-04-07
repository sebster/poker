package com.sebster.poker;

import junit.framework.Assert;

import org.junit.Test;


public class HoleTest {

	@Test
	public void testDeck() {
		for (int i = 0; i < 52; i++) {
			for (int j = i + 1; j < 52; j++) {
				final Hole hole = new Hole(Card.values()[i], Card.values()[j]);
				Assert.assertEquals(hole, Hole.fromIndex(hole.getIndex()));
			}
		}
	}

}
