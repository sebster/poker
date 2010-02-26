package com.sebster.poker;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import static com.sebster.poker.Card.*;

public class HandValueTest {

	@Test
	public void testHighCardValue() {
		final Card[] cards1 = new Card[] { JACK_SPADES, ACE_HEARTS, TWO_DIAMONDS, EIGHT_SPADES, QUEEN_CLUBS, TEN_CLUBS, THREE_DIAMONDS, };
		Arrays.sort(cards1);
		Assert.assertEquals(Combination.HIGH_CARD + (((((((Rank.ACE.getValue() << 4) + Rank.QUEEN.getValue()) << 4) + Rank.JACK.getValue()) << 4) + Rank.TEN.getValue()) << 4) + Rank.EIGHT.getValue(), Combination.getHighCardValue(cards1));

		final Card[] cards2 = new Card[] { JACK_SPADES, ACE_HEARTS, TWO_DIAMONDS, EIGHT_SPADES, QUEEN_CLUBS, TEN_CLUBS, NINE_DIAMONDS, };
		Arrays.sort(cards2);
		Assert.assertTrue(Combination.getHighCardValue(cards2) > Combination.getHighCardValue(cards1));
	}

	@Test
	public void testPairValue() {
		final Card[] cards;

		cards = new Card[] { JACK_SPADES, ACE_HEARTS, EIGHT_SPADES, TWO_CLUBS, TEN_CLUBS, TEN_DIAMONDS, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.PAIR + ((((((Rank.TEN.getValue() << 4) + Rank.ACE.getValue()) << 4) + Rank.JACK.getValue()) << 4) + Rank.NINE.getValue()), Combination.getPairValue(cards));
	}

	@Test
	public void testTwoPairValue() {
		final Card[] cards;

		cards = new Card[] { QUEEN_SPADES, JACK_HEARTS, QUEEN_SPADES, TWO_CLUBS, TEN_CLUBS, TEN_DIAMONDS, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.TWO_PAIR + ((((Rank.QUEEN.getValue() << 4) + Rank.TEN.getValue()) << 4) + Rank.JACK.getValue()), Combination.getTwoPairValue(cards));
	}

	@Test
	public void testTripsValue() {
		final Card[] cards;

		cards = new Card[] { KING_SPADES, FIVE_HEARTS, TWO_SPADES, TEN_CLUBS, TEN_CLUBS, TEN_DIAMONDS, THREE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.TRIPS + ((((Rank.TEN.getValue() << 4) + Rank.KING.getValue()) << 4) + Rank.FIVE.getValue()), Combination.getTripsValue(cards));
	}

	@Test
	public void testStraightValue() {
		final Card[] cards;

		cards = new Card[] { JACK_SPADES, ACE_HEARTS, EIGHT_SPADES, QUEEN_CLUBS, TEN_CLUBS, TEN_DIAMONDS, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.STRAIGHT + Rank.QUEEN.getValue(), Combination.getStraightValue(cards));
	}

	@Test
	public void testFlushValue() {
		Card[] cards;

		cards = new Card[] { JACK_SPADES, ACE_HEARTS, EIGHT_SPADES, QUEEN_SPADES, TWO_SPADES, THREE_SPADES, NINE_SPADES, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.FLUSH + (((((((Rank.QUEEN.getValue() << 4) + Rank.JACK.getValue()) << 4) + Rank.NINE.getValue()) << 4) + Rank.EIGHT.getValue()) << 4) + Rank.THREE.getValue(), Combination.getFlushValue(cards));

		cards = new Card[] { JACK_SPADES, ACE_HEARTS, EIGHT_SPADES, QUEEN_SPADES, TWO_HEARTS, THREE_SPADES, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(0, Combination.getFlushValue(cards));

	}

	@Test
	public void testFullHouseValue() {
		Card[] cards;

		cards = new Card[] { ACE_DIAMONDS, JACK_SPADES, JACK_HEARTS, JACK_DIAMONDS, TEN_CLUBS, TEN_HEARTS, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.FULL_HOUSE + (Rank.JACK.getValue() << 4) + Rank.TEN.getValue(), Combination.getFullHouseValue(cards));

		cards = new Card[] { EIGHT_DIAMONDS, JACK_SPADES, JACK_HEARTS, TEN_DIAMONDS, TEN_CLUBS, TEN_HEARTS, ACE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.FULL_HOUSE + (Rank.TEN.getValue() << 4) + Rank.JACK.getValue(), Combination.getFullHouseValue(cards));

		cards = new Card[] { EIGHT_DIAMONDS, JACK_SPADES, JACK_HEARTS, JACK_DIAMONDS, TWO_CLUBS, TWO_HEARTS, TWO_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.FULL_HOUSE + (Rank.JACK.getValue() << 4) + Rank.TWO.getValue(), Combination.getFullHouseValue(cards));
	}

	@Test
	public void testQuadsValue() {
		Card[] cards;

		cards = new Card[] { EIGHT_DIAMONDS, JACK_SPADES, JACK_HEARTS, JACK_DIAMONDS, TEN_CLUBS, JACK_CLUBS, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.QUADS + (Rank.JACK.getValue() << 4) + Rank.TEN.getValue(), Combination.getQuadsValue(cards));

		cards = new Card[] { EIGHT_DIAMONDS, JACK_SPADES, JACK_HEARTS, JACK_DIAMONDS, QUEEN_CLUBS, JACK_CLUBS, ACE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.QUADS + (Rank.JACK.getValue() << 4) + Rank.ACE.getValue(), Combination.getQuadsValue(cards));

		cards = new Card[] { EIGHT_DIAMONDS, JACK_SPADES, TWO_HEARTS, JACK_DIAMONDS, QUEEN_CLUBS, JACK_CLUBS, ACE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(0, Combination.getQuadsValue(cards));
	}

	@Test
	public void testStraightFlushValue() {
		Card[] cards;

		cards = new Card[] { JACK_SPADES, ACE_SPADES, EIGHT_SPADES, QUEEN_SPADES, TEN_SPADES, TEN_CLUBS, NINE_SPADES, };
		Arrays.sort(cards);
		Assert.assertEquals(Combination.STRAIGHT_FLUSH + Rank.QUEEN.getValue(), Combination.getStraightFlushValue(cards));

		cards = new Card[] { JACK_SPADES, ACE_HEARTS, EIGHT_SPADES, QUEEN_CLUBS, TEN_CLUBS, TEN_DIAMONDS, NINE_DIAMONDS, };
		Arrays.sort(cards);
		Assert.assertEquals(0, Combination.getStraightFlushValue(cards));
	}

}
