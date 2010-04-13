package com.sebster.poker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class CardSetTest {

	@Test
	public void testFromCardsArray() {
		try {
			CardSet.fromCards();
			fail("empty card set constructed");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		try {
			CardSet.fromCards((Card[]) null);
			fail("null card set constructed");
		} catch (final NullPointerException e) {
			// Expected.
		}

		try {
			CardSet.fromCards(Card.KING_SPADES, null, Card.ACE_CLUBS);
			fail("card set with null card constrcuted");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		try {
			CardSet.fromCards(Card.ACE_SPADES, Card.KING_HEARTS, Card.ACE_SPADES);
			fail("card set contains duplicate cards");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		CardSet cardSet = CardSet.fromCards(Card.ACE_DIAMONDS, Card.EIGHT_CLUBS, Card.QUEEN_HEARTS);
		assertArrayEquals(new Card[] { Card.EIGHT_CLUBS, Card.QUEEN_HEARTS, Card.ACE_DIAMONDS }, cardSet.toArray());
	}

	@Test
	public void testFromCardsCollection() {
		try {
			CardSet.fromCards(new ArrayList<Card>());
			fail("empty card set constructed");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		try {
			CardSet.fromCards((Collection<Card>) null);
			fail("null card set constructed");
		} catch (final NullPointerException e) {
			// Expected.
		}

		try {
			final List<Card> cards = Arrays.asList(Card.KING_SPADES, null, Card.ACE_CLUBS);
			CardSet.fromCards(cards);
			fail("card set with null card constrcuted");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		try {
			final List<Card> cards = Arrays.asList(Card.ACE_SPADES, Card.KING_HEARTS, Card.ACE_SPADES);
			CardSet.fromCards(cards);
			fail("card set contains duplicate cards");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}
		CardSet cardSet = CardSet.fromCards(Arrays.asList(Card.ACE_DIAMONDS, Card.EIGHT_CLUBS, Card.QUEEN_HEARTS));
		assertArrayEquals(new Card[] { Card.EIGHT_CLUBS, Card.QUEEN_HEARTS, Card.ACE_DIAMONDS }, cardSet.toArray());
	}

	@Test
	public void testFromString() {
		try {
			CardSet.fromString(" ");
			fail("empty card set constructed");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		try {
			CardSet.fromString(null);
			fail("null card set constructed");
		} catch (final NullPointerException e) {
			// Expected.
		}

		try {
			CardSet.fromString("Ks,,Ac");
			fail("card set with null card constrcuted");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}

		try {
			CardSet.fromString("As,Kh,As");
			fail("card set contains duplicate cards");
		} catch (final IllegalArgumentException e) {
			// Expected.
		}
		
		CardSet cardSet;
		
		// Test short names.
		cardSet = CardSet.fromString("Ad,8c,Qh");
		assertArrayEquals(new Card[] { Card.EIGHT_CLUBS, Card.QUEEN_HEARTS, Card.ACE_DIAMONDS }, cardSet.toArray());

		// Test short names with white space.
		cardSet = CardSet.fromString("  Ad ,8c		 , Qh	 ");
		assertArrayEquals(new Card[] { Card.EIGHT_CLUBS, Card.QUEEN_HEARTS, Card.ACE_DIAMONDS }, cardSet.toArray());
		
		// Test some long names with mixed case and extra white space.
		cardSet = CardSet.fromString("  Ace of diamonds, Eight Of Clubs,  	QUEEN of hearts ");
		assertArrayEquals(new Card[] { Card.EIGHT_CLUBS, Card.QUEEN_HEARTS, Card.ACE_DIAMONDS }, cardSet.toArray());

		// Test some mixed stuff.
		cardSet = CardSet.fromString("Ad	,	eight OF clubs , Qh");
		assertArrayEquals(new Card[] { Card.EIGHT_CLUBS, Card.QUEEN_HEARTS, Card.ACE_DIAMONDS }, cardSet.toArray());
	}
	
	@Test
	public void testToString() {
		final CardSet cardSet = CardSet.fromCards(Card.TWO_HEARTS,Card.QUEEN_SPADES, Card.ACE_CLUBS);
		assertEquals("2h,Qs,Ac", cardSet.toString());
	}

	@Test
	public void testFirst() {
		CardSet cardSet = CardSet.first(3);
		assertArrayEquals(new Card[] { Card.first(), Card.first().next(), Card.first().next().next() }, cardSet.toArray());
	}

	@Test
	public void testLast() {
		CardSet cardSet = CardSet.last(3);
		assertArrayEquals(new Card[] { Card.last().prev().prev(), Card.last().prev(), Card.last() }, cardSet.toArray());
	}

	@Test
	public void testGetCard() {
		final CardSet cardSet = CardSet.fromCards(Card.ACE_DIAMONDS, Card.EIGHT_CLUBS, Card.QUEEN_HEARTS);

		assertEquals(Card.EIGHT_CLUBS, cardSet.getCard(0));
		assertEquals(Card.QUEEN_HEARTS, cardSet.getCard(1));
		assertEquals(Card.ACE_DIAMONDS, cardSet.getCard(2));

		try {
			cardSet.getCard(-1);
			fail("expected array index out of bounds");
		} catch (final ArrayIndexOutOfBoundsException e) {
			// Expected.
		}

		try {
			cardSet.getCard(3);
			fail("expected array index out of bounds");
		} catch (final ArrayIndexOutOfBoundsException e) {
			// Expected.
		}
	}

	@Test
	public void testCompareTo() {
		// FIXME implement
	}
	
	@Test
	public void testNext() {
		// 2c,5h -> 2c,5s
		assertEquals(CardSet.fromString("2c,5s"), CardSet.fromString("2c,5h").next());
		
		// 2c,As -> 2d,2h
		assertEquals(CardSet.fromString("2d,2h"), CardSet.fromString("2c,As").next());
	}
	
}
