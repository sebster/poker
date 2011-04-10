package com.sebster.poker;

import com.sebster.util.Validate;

/**
 * Utility class to calculate and use the value of the best 5-card combination
 * of a poker hand. The combination value of a hand is given as follows:
 * 
 * <pre>
 * High Card:
 * 		HIGH_CARD      | (kick1 | kick2 | kick3 | kick4 | kick5)
 * Pair:
 * 		PAIR	       | (      | pair  | kick1 | kick2 | kick3)
 * Two Pair:
 * 		TWO_PAIR       | (      |       | pair1 | pair2 | kick )
 * Three of a Kind:
 * 		TRIPS	       | (      |       | trips | kick1 | kick2)
 * Straight:
 * 		STRAIGHT       | (      |       |       |       | high )
 * Flush:
 * 		FLUSH	       | (rank1 | rank2 | rank3 | rank4 | rank5)
 * Full House:
 * 		FULL_HOUSE     | (      |       |       | trips | pair )
 * Four of a Kind:
 * 		QUADS          | (      |       |       | quads | kick )
 * Straight Flush:
 * 		STRAIGHT_FLUSH | (      |       |       |       | high )
 * </pre>
 * 
 * The capital letters indicate the respective constant, and the ( | | | | )
 * notation refers to the lowest 5 nibbles. Each nibble contains the rank of the
 * respective card, or the value 0 if is empty.
 * 
 * @author Sebastiaan van Erk
 */
public class Combination {

	private Combination() {
		// Utility class.
	}

	/**
	 * Get the value of the best 5-card combination that can be made from the
	 * specified card set.
	 * 
	 * @param cardSet
	 *            the card set
	 * @return the value of the best 5-card combination found
	 */
	public static int getBestValue(final CardSet cardSet) {
		Validate.notNull(cardSet, "cardSet");
		Validate.isTrue(cardSet.size() >= 5, "cardSet.size() < 5");

		/*
		 * nOfAKind[i] contains how many (i+1) of a kinds were found (counting 1
		 * of a kinds is not necessary).
		 */
		final int[] nOfAKind = new int[4];

		// The rank and suit of the previous card.
		int previousRank = -1, previousSuit = -1;

		/*
		 * The number of times the previous rank is repeated minus one (so 2
		 * kings, means repeatCount is 1).
		 */
		int repeatCount = 0;

		// The number of cards with each suit.
		final int[] suitCounts = new int[4];

		// Whether or not a flush was found.
		boolean flush = false;

		// The high card of the straight we are currently scanning.
		int straightHighRank = Integer.MAX_VALUE;

		// The length of the straight we are currently scanning.
		int straightLength = 0;

		// Whether or not a straight was found.
		boolean straight = false;

		// Scan the cards for quads, trips, pairs, flushes and straights.
		for (final Card card : cardSet.descendingIterable()) {
			final int rank = card.getRank().getValue();
			final int suit = card.getSuit().getValue();
			if (rank == previousRank) {
				if (suit == previousSuit) {
					// Found the same card twice.
					return -1;
				}
				// Found a repetition of the previous rank.
				repeatCount++;
			} else {
				/*
				 * Found a different card; register the n-of-a-kind found, reset
				 * the repeat count, remember the new rank.
				 */
				nOfAKind[repeatCount]++;
				repeatCount = 0;
				previousRank = rank;
			}
			if (!straight) {
				// No straight was found yet.
				if (straightHighRank > rank + straightLength) {
					/*
					 * The current card is not consecutive to the previous
					 * straight cards, so the current straight is broken. The
					 * current card may be the high card of a straight though.
					 */
					straightHighRank = rank;
					straightLength = 1;
				} else if (straightHighRank == rank + straightLength && ++straightLength == 5) {
					/*
					 * The current card is consecutive to the current straight
					 * cards, and the length of the current straight is 5. This
					 * means we have found a straight.
					 */
					straight = true;
				}
			}
			if (!flush) {
				// No flush was found yet.
				if (++suitCounts[suit] == 5) {
					// We found five cards of the save suit, so we have a flush.
					flush = true;
				}
			}
			previousSuit = suit;
		}
		// Count the last card for the n-of-a-kind.
		nOfAKind[repeatCount]++;

		// Check for an ace-high straight.
		if (!straight && straightHighRank == Rank.FIVE.getValue() && straightLength == 4 && cardSet.last().getRank() == Rank.ACE) {
			straight = true;
		}

		// Get the hand values for the hand based on the scan.

		if (straight && flush) {
			// Check for a straight flush if a straight and a flush are found.
			final int value = getStraightFlushValue(cardSet);
			if (value > 0) {
				// A straight flush was found.
				return value;
			}
		}

		if (nOfAKind[3] > 0) {
			// Four of a kind was found.
			return getQuadsValue(cardSet);
		}

		if (nOfAKind[2] > 0 && (nOfAKind[1] > 0 || nOfAKind[2] > 1)) {
			// A three of a kind and a 2 or another 3 of kind was found, so we
			// have a full house.
			return getFullHouseValue(cardSet);
		}

		if (flush) {
			// A flush was found.
			return getFlushValue(cardSet);
		}

		if (straight) {
			// A straight was found.
			return getStraightValue(cardSet);
		}

		if (nOfAKind[2] > 0) {
			// Three of a kind was found.
			return getTripsValue(cardSet);
		}
		if (nOfAKind[1] > 1) {
			// Two pair was found.
			return getTwoPairValue(cardSet);
		}
		if (nOfAKind[1] > 0) {
			// A pair was found.
			return getPairValue(cardSet);
		}

		// Nothing was found, return high card value.
		return getHighCardValue(cardSet);
	}

	public static int getCombinationType(final int combinationValue) {
		return combinationValue & COMBINATION_TYPE_MASK;
	}

	public static boolean isHighCard(final int combinationValue) {
		return getCombinationType(combinationValue) == HIGH_CARD;
	}

	public static boolean isPair(final int combinationValue) {
		return getCombinationType(combinationValue) == PAIR;
	}

	public static boolean isTwoPair(final int combinationValue) {
		return getCombinationType(combinationValue) == TWO_PAIR;
	}

	public static boolean isTrips(final int combinationValue) {
		return getCombinationType(combinationValue) == TRIPS;
	}

	public static boolean isStraight(final int combinationValue) {
		return getCombinationType(combinationValue) == STRAIGHT;
	}

	public static boolean isFlush(final int combinationValue) {
		return getCombinationType(combinationValue) == FLUSH;
	}

	public static boolean isFullHouse(final int combinationValue) {
		return getCombinationType(combinationValue) == FULL_HOUSE;
	}

	public static boolean isQuads(final int combinationValue) {
		return getCombinationType(combinationValue) == QUADS;
	}

	public static boolean isStraightFlush(final int combinationValue) {
		return getCombinationType(combinationValue) == STRAIGHT_FLUSH;
	}

	public static final String toString(final int combinationValue) {
		// FIXME implement
		return null;
	}

	public static final int fromString(final String combinationString) {
		// FIXME implement
		return -1;
	}

	/**
	 * Get the combination value of a high card hand.
	 * 
	 * @param cardSet
	 *            the sorted cards
	 * @return the high card combination value of the cards
	 */
	protected static int getHighCardValue(final CardSet cardSet) {
		int value = 0;
		// Get the value of the best 5 cards.
		for (int i = 1; i <= 5; i++) {
			value = value << 4 | cardSet.get(cardSet.size() - i).getRank().getValue();
		}
		return HIGH_CARD | value;
	}

	/**
	 * Get the hand value of a pair hand.
	 * 
	 * @param cardSet
	 *            the sorted cards
	 * @return the pair hand value of the cards
	 */
	protected static int getPairValue(final CardSet cardSet) {
		return getNOfAKindValue(cardSet, 2, PAIR);
	}

	/**
	 * Get the combination value of a two pair hand.
	 * 
	 * @param cardSet
	 *            the sorted cards
	 * @return the pair combination value of the cards
	 */
	protected static int getTwoPairValue(final CardSet cardSet) {
		final int[] pairs = new int[2];
		int pair = 0, repeatCount = 0;
		for (int i = cardSet.size() - 1; i >= 0 && pair < 2; i--) {
			final int rank = cardSet.get(i).getRank().getValue();
			if (pairs[pair] != rank) {
				pairs[pair] = rank;
				repeatCount = 1;
			} else if (++repeatCount == 2) {
				repeatCount = 0;
				pair++;
			}
		}
		if (pair == 2) {
			for (int i = cardSet.size() - 1; i >= 0; i--) {
				final int rank = cardSet.get(i).getRank().getValue();
				if (rank != pairs[0] && rank != pairs[1]) {
					return TWO_PAIR | pairs[0] << 8 | pairs[1] << 4 | rank;
				}
			}
		}
		return 0;
	}

	/**
	 * Get the combination value of a three of a kind hand.
	 * 
	 * @param cardSet
	 *            the sorted cards
	 * @return the three of a kind combinatino value of the cards
	 */
	protected static int getTripsValue(final CardSet cardSet) {
		return getNOfAKindValue(cardSet, 3, TRIPS);
	}

	protected static int getStraightValue(final CardSet cardSet) {
		int high = Integer.MAX_VALUE;
		int length = 0;
		for (int i = cardSet.size() - 1; i >= 0; i--) {
			final int rank = cardSet.get(i).getRank().getValue();
			if (high > rank + length) {
				high = rank;
				length = 1;
			} else if (high == rank + length && ++length == 5) {
				return STRAIGHT | high;
			}
		}
		if (high == Rank.FIVE.getValue() && length == 4 && cardSet.last().getRank() == Rank.ACE) {
			return STRAIGHT | high;
		}
		return 0;
	}

	protected static int getFlushValue(final CardSet cardSet) {
		final int[] value = new int[4], count = new int[4];
		for (int i = cardSet.size() - 1; i >= 0; i--) {
			final int suit = cardSet.get(i).getSuit().getValue();
			value[suit] = (value[suit] << 4) + cardSet.get(i).getRank().getValue();
			if (++count[suit] == 5) {
				return FLUSH | value[suit];
			}
		}
		return 0;
	}

	protected static int getFullHouseValue(final CardSet cardSet) {
		int trips = -1, pair = -1, current = -1, count = 0;
		for (int i = cardSet.size() - 1; i >= 0; i--) {
			final int rank = cardSet.get(i).getRank().getValue();
			if (current != rank) {
				current = rank;
				count = 1;
			} else {
				count++;
				if (count == 2 && pair == -1) {
					pair = current;
				} else if (count == 3 && trips == -1) {
					trips = current;
					if (pair == current) {
						pair = -1;
					}
				}
				if (trips >= 0 && pair >= 0) {
					return FULL_HOUSE | trips << 4 | pair;
				}
			}
		}
		return 0;
	}

	/**
	 * Get the combination value of a four of a kind hand.
	 * 
	 * @param cardSet
	 *            the sorted cards
	 * @return the four of a kind combination value of the cards
	 */
	protected static int getQuadsValue(final CardSet cardSet) {
		return getNOfAKindValue(cardSet, 4, QUADS);
	}

	protected static int getStraightFlushValue(final CardSet cardSet) {
		final int[] high = new int[4], length = new int[4];
		for (int i = cardSet.size() - 1; i >= 0; i--) {
			final int rank = cardSet.get(i).getRank().getValue();
			final int suit = cardSet.get(i).getSuit().getValue();
			if (high[suit] != rank + length[suit]) {
				high[suit] = rank;
				length[suit] = 1;
			} else if (++length[suit] == 5) {
				return STRAIGHT_FLUSH | high[suit];
			}
		}
		final boolean[] hasAce = new boolean[4];
		for (int i = cardSet.size() - 1; i >= 0 && cardSet.get(i).getRank() == Rank.ACE; i--) {
			hasAce[cardSet.get(i).getSuit().getValue()] = true;
		}
		for (int suit = 0; suit < 4; suit++) {
			if (high[suit] == Rank.FIVE.getValue() && length[suit] == 4 && hasAce[suit]) {
				return STRAIGHT_FLUSH | high[suit];
			}
		}
		return 0;
	}

	/**
	 * Get the combination value of a n-of-a-kind hand.
	 * 
	 * @param cardSet
	 *            the sorted cards
	 * @param n
	 *            the number of a kind
	 * @param baseCombinationValue
	 *            base combination value of the n-of-a-kind
	 * 
	 * @return the n-of-a-kind hand value of the cards
	 */
	private static int getNOfAKindValue(final CardSet cardSet, final int n, final int baseCombinationValue) {
		int nOfAKindRank = -1, repeatCount = 1;
		for (int i = cardSet.size() - 1; i >= 0 && repeatCount < n; i--) {
			final int rank = cardSet.get(i).getRank().getValue();
			if (nOfAKindRank != rank) {
				nOfAKindRank = rank;
				repeatCount = 1;
			} else {
				repeatCount++;
			}
		}
		if (repeatCount == n) {
			// Found an n-of-a-kind.
			int value = nOfAKindRank;
			// Add the value of the remaining cards from high to low.
			for (int i = cardSet.size() - 1, j = 5 - n; i >= 0 && j > 0; i--) {
				final int rank = cardSet.get(i).getRank().getValue();
				if (rank != nOfAKindRank) {
					value = value << 4 | rank;
					j--;
				}
			}
			return baseCombinationValue | value;
		}
		return 0;
	}

	/**
	 * Base high card value.
	 */
	public static final int HIGH_CARD = 0x00000000;

	/**
	 * Base pair value.
	 */
	public static final int PAIR = 0x00100000;

	/**
	 * Base two pair value.
	 */
	public static final int TWO_PAIR = 0x00200000;

	/**
	 * Base three of a kind value.
	 */
	public static final int TRIPS = 0x00300000;

	/**
	 * Base straight value.
	 */
	public static final int STRAIGHT = 0x00400000;

	/**
	 * Base flush value.
	 */
	public static final int FLUSH = 0x00500000;

	/**
	 * Base full house value.
	 */
	public static final int FULL_HOUSE = 0x00600000;

	/**
	 * Base base four of a kind value.
	 */
	public static final int QUADS = 0x00700000;

	/**
	 * Base straight flush value.
	 */
	public static final int STRAIGHT_FLUSH = 0x00800000;

	/**
	 * The mask to get the combination type.
	 */
	public static final int COMBINATION_TYPE_MASK = 0x00F00000;

}
