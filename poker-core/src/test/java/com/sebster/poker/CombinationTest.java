package com.sebster.poker;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class CombinationTest {

	@Test
	public void testHighCardValue() {
		final CardSet cards1 = CardSet.fromString("Js,Ah,2d,8s,Qc,Tc,3d");
		assertEquals(Combination.HIGH_CARD + (((((((Rank.ACE.getValue() << 4) + Rank.QUEEN.getValue()) << 4) + Rank.JACK.getValue()) << 4) + Rank.TEN.getValue()) << 4) + Rank.EIGHT.getValue(), Combination.getHighCardValue(cards1));

		final CardSet cards2 = CardSet.fromString("Js,Ah,2d,8s,Qc,Tc,9d");
		assertTrue(Combination.getHighCardValue(cards2) > Combination.getHighCardValue(cards1));
	}

	@Test
	public void testPairValue() {
		final CardSet cards = CardSet.fromString("Js,Ah,8s,2c,Tc,Td,9d");
		assertEquals(Combination.PAIR + ((((((Rank.TEN.getValue() << 4) + Rank.ACE.getValue()) << 4) + Rank.JACK.getValue()) << 4) + Rank.NINE.getValue()), Combination.getPairValue(cards));
	}

	@Test
	public void testTwoPairValue() {
		final CardSet cards = CardSet.fromString("Qs,Jh,Qh,2c,Tc,Td,9d");
		assertEquals(Combination.TWO_PAIR + ((((Rank.QUEEN.getValue() << 4) + Rank.TEN.getValue()) << 4) + Rank.JACK.getValue()), Combination.getTwoPairValue(cards));
	}

	@Test
	public void testTripsValue() {
		final CardSet cards = CardSet.fromString("Ks,5h,2s,Tc,Td,Th,3d");
		assertEquals(Combination.TRIPS + ((((Rank.TEN.getValue() << 4) + Rank.KING.getValue()) << 4) + Rank.FIVE.getValue()), Combination.getTripsValue(cards));
	}

	@Test
	public void testStraightValue() {
		final CardSet cards = CardSet.fromString("Js,Ah,8s,Qc,Tc,Td,9d");
		assertEquals(Combination.STRAIGHT + Rank.QUEEN.getValue(), Combination.getStraightValue(cards));
	}

	@Test
	public void testFlushValue() {
		final CardSet cards1 = CardSet.fromString("Js,Ah,8S,Qs,2s,3s,9s");
		assertEquals(Combination.FLUSH + (((((((Rank.QUEEN.getValue() << 4) + Rank.JACK.getValue()) << 4) + Rank.NINE.getValue()) << 4) + Rank.EIGHT.getValue()) << 4) + Rank.THREE.getValue(), Combination.getFlushValue(cards1));

		final CardSet cards2 = CardSet.fromString("Js,Ah,8s,Qs,2h,3s,9d");
		assertEquals(0, Combination.getFlushValue(cards2));
	}

	@Test
	public void testFullHouseValue() {
		final CardSet cards1 = CardSet.fromString("Ad,Js,Jh,Jd,Tc,Th,9d");
		assertEquals(Combination.FULL_HOUSE + (Rank.JACK.getValue() << 4) + Rank.TEN.getValue(), Combination.getFullHouseValue(cards1));

		final CardSet cards2 = CardSet.fromString("8d,Js,Jh,Td,Tc,Th,Ad");
		assertEquals(Combination.FULL_HOUSE + (Rank.TEN.getValue() << 4) + Rank.JACK.getValue(), Combination.getFullHouseValue(cards2));

		final CardSet cards3 = CardSet.fromString("8d,Js,Jh,Jd,2c,2h,2d");
		assertEquals(Combination.FULL_HOUSE + (Rank.JACK.getValue() << 4) + Rank.TWO.getValue(), Combination.getFullHouseValue(cards3));
	}

	@Test
	public void testQuadsValue() {
		final CardSet cards1 = CardSet.fromString("8d,Js,Jh,Jd,Tc,Jc,9d");
		assertEquals(Combination.QUADS + (Rank.JACK.getValue() << 4) + Rank.TEN.getValue(), Combination.getQuadsValue(cards1));

		final CardSet cards2 = CardSet.fromString("8d,Js,Jh,Jd,Qc,Jc,Ad");
		assertEquals(Combination.QUADS + (Rank.JACK.getValue() << 4) + Rank.ACE.getValue(), Combination.getQuadsValue(cards2));

		final CardSet cards3 = CardSet.fromString("8d,Js,2h,Jd,Qc,Jc,Ad");
		assertEquals(0, Combination.getQuadsValue(cards3));
	}

	@Test
	public void testStraightFlushValue() {
		final CardSet cards1 = CardSet.fromString("Js,As,8s,Qs,Ts,Tc,9s");
		assertEquals(Combination.STRAIGHT_FLUSH + Rank.QUEEN.getValue(), Combination.getStraightFlushValue(cards1));

		final CardSet cards2 = CardSet.fromString("Js,Ah,8s,Qc,Tc,Td,9d");
		assertEquals(0, Combination.getStraightFlushValue(cards2));
	}

}
