package com.sebster.poker;

import java.util.Comparator;

import com.sebster.util.ArrayUtils;

public final class Holes {

	private Holes() {
		// Utility class.
	}

	public static int[] normalize(final Hole[] holes) {
		final int[] indexes = ArrayUtils.trackedInsertionSort(holes, HoleRankComparator.INSTANCE);
		final Permutation permutation = new Permutation();
		for (final Hole hole : holes) {
			permutation.fix(hole);
			if (permutation.isFixed()) {
				break;
			}
		}
		permutation.applyTo(holes);
		return indexes;
	}

	private static enum HoleRankComparator implements Comparator<Hole> {

		INSTANCE;

		@Override
		public int compare(final Hole hole1, final Hole hole2) {
			final int c = hole1.getFirst().getRank().compareTo(hole2.getFirst().getRank());
			if (c != 0) {
				return c;
			}
			return hole1.getSecond().getRank().compareTo(hole2.getSecond().getRank());
		}

	}

	private static class Permutation {

		private final int[] choices = new int[] { 0x0f, 0x0f, 0x0f, 0x0f };

		public void applyTo(final Hole[] holes) {
			final int[] permutation = asArray();
			final Suit[] suits = Suit.values();
			for (int i = 0; i < holes.length; i++) {
				final Hole hole = holes[i];
				final Card card1 = hole.getFirst(), card2 = hole.getSecond();
				final int suit1 = card1.getSuit().ordinal(), newSuit1 = permutation[suit1];
				final int suit2 = card2.getSuit().ordinal(), newSuit2 = permutation[suit2];
				final boolean suit1Changed = suit1 != newSuit1, suit2Changed = suit2 != newSuit2;
				if (suit1Changed || suit2Changed) {
					final Card newCard1 = suit1Changed ? Card.byRankAndSuit(card1.getRank(), suits[newSuit1]) : card1;
					final Card newCard2 = suit2Changed ? Card.byRankAndSuit(card2.getRank(), suits[newSuit2]) : card2;
					holes[i] = Hole.getInstance(newCard1, newCard2);
				}
			}
		}

		public boolean isFixed() {
			for (int suit = 0; suit < 4; suit++) {
				final int suitChoices = choices[suit];
				if (suitChoices != firstMask(suitChoices)) {
					// More than one choice for this suit.
					return false;
				}
			}
			// All suits have 1 choice only.
			return true;
		}

		private int[] asArray() {
			int mappedTargetSuits = 0;
			final int[] permutation = new int[4];
			for (int sourceSuit = 0; sourceSuit < 4; sourceSuit++) {
				final int suitChoices = choices[sourceSuit];
				int targetSuit = 0, targetMask = 1;
				while ((suitChoices & targetMask) == 0 || (mappedTargetSuits & targetMask) != 0) {
					targetMask <<= 1;
					targetSuit++;
				}
				mappedTargetSuits = mappedTargetSuits | targetMask;
				permutation[sourceSuit] = targetSuit;
			}
			return permutation;
		}

		private int firstMask(final int value) {
			int mask = 1;
			for (int i = 0; i < 4; i++) {
				if ((value & mask) != 0) {
					return mask;
				}
				mask <<= 1;
			}
			return 0;
		}

		private int firstTwoMask(final int value) {
			int count = 0, mask = 1, result = 0;
			for (int i = 0; i < 4; i++) {
				if ((value & mask) > 0) {
					result |= mask;
					if (++count == 2) {
						break;
					}
				}
				mask <<= 1;
			}
			return count == 2 ? result : 0;
		}

		private void fix(final Card card) {
			final int suit = card.getSuit().ordinal();
			final int suitChoices = choices[suit];
			final int mask = firstMask(suitChoices);
			if (suitChoices != mask) {
				choices[suit] = mask;
				for (int i = 0; i < 4; i++) {
					if (suit != i) {
						choices[i] &= ~mask;
					}
				}
			}
		}

		public void fix(final Hole hole) {
			final Card card1 = hole.getFirst(), card2 = hole.getSecond();
			if (!hole.isPair()) {
				fix(card1);
				fix(card2);
			} else {
				final int suit1 = card1.getSuit().ordinal(), suit2 = card2.getSuit().ordinal();
				final int mask = firstTwoMask(choices[suit1] & choices[suit2]);
				if (mask == 0) {
					fix(card1);
					fix(card2);
				} else {
					choices[suit1] = choices[suit2] = mask;
					for (int i = 0; i < 4; i++) {
						if (i != suit1 && i != suit2) {
							choices[i] &= ~mask;
						}
					}
				}
			}
		}

	}

}
