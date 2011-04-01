package com.sebster.poker;

import java.util.Comparator;

import com.sebster.util.ArrayUtils;

public final class Holes {

	private Holes() {
		// Utility class.
	}

	public static int[] normalize(final Hole[] holes) {
		final int[] indexes = ArrayUtils.trackedInsertionSort(holes, HoleByRankComparator.INSTANCE);
		final SuitPermutation suitPermutation = new SuitPermutation();
		for (final Hole hole : holes) {
			suitPermutation.fixSuitsForHole(hole);
			if (suitPermutation.isFixed()) {
				break;
			}
		}
		suitPermutation.applyTo(holes);
		return indexes;
	}

	private static enum HoleByRankComparator implements Comparator<Hole> {

		INSTANCE;

		@Override
		public int compare(final Hole hole1, final Hole hole2) {
			final int c = hole1.getFirst().getRank().ordinal() - hole2.getFirst().getRank().ordinal();
			if (c != 0) {
				return c;
			}
			return hole1.getSecond().getRank().ordinal() - hole2.getSecond().getRank().ordinal();
		}

	}

	private static final class SuitPermutation {

		private static final Suit[] SUITS = Suit.values();

		private static final int[] SUIT_FOR_MASK = { -1, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0 };
		private static final int[] MASK1 = { 0x0, 0x1, 0x2, 0x1, 0x4, 0x1, 0x2, 0x1, 0x8, 0x1, 0x2, 0x1, 0x4, 0x1, 0x2, 0x1 };
		private static final int[] MASK2 = { 0x0, 0x0, 0x0, 0x3, 0x0, 0x5, 0x6, 0x3, 0x0, 0x9, 0xa, 0x3, 0xc, 0x5, 0x6, 0x3 };

		private final int[] choices = { 0x0f, 0x0f, 0x0f, 0x0f };

		public void applyTo(final Hole[] holes) {
			final int[] permutation = toArray();
			for (int i = 0; i < holes.length; i++) {
				final Hole hole = holes[i];
				final Card card1 = hole.getFirst(), card2 = hole.getSecond();
				final int suit1 = card1.getSuit().ordinal(), newSuit1 = permutation[suit1];
				final int suit2 = card2.getSuit().ordinal(), newSuit2 = permutation[suit2];
				final boolean suit1Changed = suit1 != newSuit1, suit2Changed = suit2 != newSuit2;
				if (suit1Changed || suit2Changed) {
					final Card newCard1 = suit1Changed ? Card.byRankAndSuit(card1.getRank(), SUITS[newSuit1]) : card1;
					final Card newCard2 = suit2Changed ? Card.byRankAndSuit(card2.getRank(), SUITS[newSuit2]) : card2;
					holes[i] = Hole.getInstance(newCard1, newCard2);
				}
			}
		}

		public boolean isFixed() {
			return choices[0] + choices[1] + choices[2] + choices[3] == 0x0f;
		}

		public void fixSuitsForHole(final Hole hole) {
			final Card card1 = hole.getFirst(), card2 = hole.getSecond();
			final int suit1 = card1.getSuit().ordinal(), suit2 = card2.getSuit().ordinal();
			if (hole.isPair()) {
				final int suitChoices1 = choices[suit1], suitChoices2 = choices[suit2];
				final int mask = MASK2[suitChoices1 & suitChoices2];
				if (mask == 0) {
					fixSuit(suit1);
					fixSuit(suit2);
				} else if (suitChoices1 != mask || suitChoices2 != mask) {
					for (int i = 0; i < 4; i++) {
						choices[i] &= ~mask;
					}
					choices[suit1] = mask;
					choices[suit2] = mask;
				}
			} else {
				fixSuit(suit1);
				fixSuit(suit2);
			}
		}

		private void fixSuit(final int suit) {
			final int suitChoices = choices[suit];
			final int mask = MASK1[suitChoices];
			if (suitChoices != mask) {
				for (int i = 0; i < 4; i++) {
					choices[i] &= ~mask;
				}
				choices[suit] = mask;
			}
		}

		private int[] toArray() {
			int mappedMask = 0;
			final int[] permutation = new int[4];
			for (int sourceSuit = 0; sourceSuit < 4; sourceSuit++) {
				final int targetMask = MASK1[choices[sourceSuit] & ~mappedMask];
				permutation[sourceSuit] = SUIT_FOR_MASK[targetMask];
				mappedMask |= targetMask;
			}
			return permutation;
		}

	}

}
