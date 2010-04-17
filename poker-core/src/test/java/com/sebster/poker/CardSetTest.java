package com.sebster.poker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.sebster.poker.util.Combinatorics;

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
		final CardSet cardSet = CardSet.fromCards(Card.TWO_HEARTS, Card.QUEEN_SPADES, Card.ACE_CLUBS);
		assertEquals("2h,Qs,Ac", cardSet.toString());
	}

	@Test
	public void testFirstCardSet() {
		CardSet cardSet = CardSet.first(3);
		assertArrayEquals(new Card[] { Card.first(), Card.first().next(), Card.first().next().next() }, cardSet.toArray());
	}

	@Test
	public void testLastCardSet() {
		CardSet cardSet = CardSet.last(3);
		assertArrayEquals(new Card[] { Card.last().prev().prev(), Card.last().prev(), Card.last() }, cardSet.toArray());
	}

	@Test
	public void testGet() {
		final CardSet cardSet = CardSet.fromCards(Card.ACE_DIAMONDS, Card.EIGHT_CLUBS, Card.QUEEN_HEARTS);

		assertEquals(Card.EIGHT_CLUBS, cardSet.get(0));
		assertEquals(Card.QUEEN_HEARTS, cardSet.get(1));
		assertEquals(Card.ACE_DIAMONDS, cardSet.get(2));

		try {
			cardSet.get(-1);
			fail("expected array index out of bounds");
		} catch (final ArrayIndexOutOfBoundsException e) {
			// Expected.
		}

		try {
			cardSet.get(3);
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

		// 2c,Ah -> 2c,As
		assertEquals(CardSet.fromString("2c,As"), CardSet.fromString("2c,Ah").next());

		// 2c,5h -> 2c,5s
		assertEquals(CardSet.fromString("2c,5s"), CardSet.fromString("2c,5h").next());

		// 2c,As -> 2d,2h
		assertEquals(CardSet.fromString("2d,2h"), CardSet.fromString("2c,As").next());

		// Ah,As -> null
		assertNull(CardSet.fromString("Ah,As").next());

		// 2c,Ah,As -> 2d,2h,2s
		assertEquals(CardSet.fromString("2d,2h,2s"), CardSet.fromString("2c,Ah,As").next());
	}

	@Test
	public void testPrev() {
		// 2c,5h -> 2c,5d
		assertEquals(CardSet.fromString("2c,5d"), CardSet.fromString("2c,5h").prev());

		// 5d,5h -> 5c,As
		assertEquals(CardSet.fromString("5c,As"), CardSet.fromString("5d,5h").prev());

		// 2c,2d -> null
		assertNull(CardSet.fromString("2c,2d").prev());

		// 8h,8s,9c -> 8d,Ah,As
		assertEquals(CardSet.fromString("8d,Ah,As"), CardSet.fromString("8h,8s,9c").prev());
	}

	@Test
	public void testFirst() {
		assertEquals(Card.byName("8h"), CardSet.fromString("8h,Qs,Kh,As").first());
	}

	@Test
	public void testLast() {
		assertEquals(Card.byName("As"), CardSet.fromString("8h,Qs,Kh,As").last());
	}

	@Test
	public void testIntersects() {
		assertTrue(CardSet.fromString("8h,Ts,Jc,Jd").intersects(CardSet.fromString("2h,Ts,Ah")));
		assertFalse(CardSet.fromString("2h,4c,6h").intersects(CardSet.fromString("3h,5c")));
		assertTrue(CardSet.fromString("2h,4c,6h").intersects(CardSet.fromString("2h,3d,9h")));
		assertTrue(CardSet.fromString("2h,4c,6h").intersects(CardSet.fromString("2c,4d,6h")));
		assertTrue(CardSet.fromString("2h,4c,6h").intersects(CardSet.fromString("2h")));
	}

	@Test
	public void testGetIndex() {
		for (int size = 1; size <= 3; size++) {
			int i = 0;
			for (CardSet cardSet = CardSet.first(size); cardSet != null; cardSet = cardSet.next(), i++) {
//				System.out.println(i + " " + cardSet);
				assertEquals(i, cardSet.getIndex());
			}
		}
		for (int size = 1; size <= 3; size++) {
			int i = Combinatorics.combinations(52, size) - 1;
			for (CardSet cardSet = CardSet.last(size); cardSet != null; cardSet = cardSet.prev(), i--) {
//				System.out.println(i + " " + cardSet);
				assertEquals(i, cardSet.getIndex());
			}
		}
		
//		Deck deck = new Deck(new Random(0));
//		CardSet cardSet = CardSet.fromDeck(deck, 8);
//		assertEquals(0, cardSet.getIndex());
	}

	@Test
	public void testFromIndex() {
		for (int size = 1; size <= 3; size++) {
			final int num = Combinatorics.combinations(52, size);
			CardSet cardSet = CardSet.first(size);
			for (int i = 0; i < num; i++, cardSet = cardSet.next()) {
				System.out.println(i + " " + cardSet);
				assertEquals(cardSet, CardSet.fromIndex(i, size));
			}
		}
		for (int size = 1; size <= 3; size++) {
			CardSet cardSet = CardSet.last(size);
			for (int i = Combinatorics.combinations(52, size) - 1; i >= 0; i--, cardSet = cardSet.prev()) {
//				System.out.println(i + " " + cardSet);
				assertEquals(cardSet, CardSet.fromIndex(i, size));
			}
		}
		
//		Deck deck = new Deck(new Random(0));
//		CardSet cardSet = CardSet.fromDeck(deck, 8);
//		assertEquals(0, cardSet.getIndex());
	}

}
