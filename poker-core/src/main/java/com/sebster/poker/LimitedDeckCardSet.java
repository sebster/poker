package com.sebster.poker;

import java.util.EnumSet;

public class LimitedDeckCardSet extends CardSet {

	private final EnumSet<Card> deck;
	
	protected LimitedDeckCardSet(final Card[] cards, final EnumSet<Card> deck) {
		super(cards);
		this.deck = deck;
	}

}
