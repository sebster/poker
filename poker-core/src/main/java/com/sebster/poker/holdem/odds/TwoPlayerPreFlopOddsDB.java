package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.Suit;
import com.sebster.poker.holdem.Hole;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.TwoPlayerOdds;
import com.sebster.poker.util.Utils;
import com.sebster.util.ArrayUtils;

public class TwoPlayerPreFlopOddsDB {

	private static final Logger logger = LoggerFactory.getLogger(TwoPlayerPreFlopOddsDB.class);

	public static final String DB_RESOURCE_NAME = "preflop_odds_2p_full.bin";

	/*
	 * Initialize-on-demand holder class idiom.
	 */
	private static class DBHolder {

		public static final TwoPlayerPreFlopOddsDB db;

		static {
			InputStream in = null;
			try {
				in = TwoPlayerPreFlopOddsDB.class.getResourceAsStream(DB_RESOURCE_NAME);
				db = new TwoPlayerPreFlopOddsDB(in);
			} catch (final IOException e) {
				// Should not happen.
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(in);
			}
		}

	}

	public static final TwoPlayerPreFlopOddsDB getInstance() {
		return DBHolder.db;
	}

	private final int[] hands;

	private final TwoPlayerOdds[] odds;

	public TwoPlayerPreFlopOddsDB(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		final DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
		final int rows = dis.readInt();
		hands = new int[rows];
		odds = new TwoPlayerOdds[rows];
		for (int i = 0; i < rows; i++) {
			hands[i] = dis.readInt();
			final int wins = dis.readInt(), losses = dis.readInt();
			odds[i] = new TwoPlayerOdds(wins, losses, Constants.BOARD_COUNT_2 - wins - losses);
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("db init in {} ms", t2 - t1);
	}

	public TwoPlayerOdds getOdds(final Hole hole1, final Hole hole2) {

		// Make sure they don't contain duplicate cards.
		if (hole1.intersects(hole2)) {
			throw new IllegalArgumentException("hole1 and hole2 contain common cards");
		}
		
		// Normalize hand.
		final Card[] cards = { hole1.getFirst(), hole1.getSecond(), hole2.getFirst(), hole2.getSecond() };

		// Make sure the cards are lexicographically ordered.
		final int v1 = (cards[0].getRank().getValue() << 4) + cards[1].getRank().getValue();
		final int v2 = (cards[2].getRank().getValue() << 4) + cards[3].getRank().getValue();
		if (v1 > v2) {
			ArrayUtils.swap(cards, 0, 2);
			ArrayUtils.swap(cards, 1, 3);
		}

		// Make sure the first suit is clubs.
		Suit suit0 = cards[0].getSuit();
		if (suit0 != Suit.CLUBS) {
			Utils.permuteSuits(cards, suit0, Suit.CLUBS);
			suit0 = Suit.CLUBS;
		}

		// Make sure the seconds suit is clubs or diamonds.
		Suit suit1 = cards[1].getSuit();
		if (suit1 != Suit.CLUBS && suit1 != Suit.DIAMONDS) {
			Utils.permuteSuits(cards, suit1, Suit.DIAMONDS);
			suit1 = Suit.DIAMONDS;
		}

		Suit suit2 = cards[2].getSuit();
		Suit suit3 = cards[3].getSuit();

		if (suit1 == Suit.CLUBS && (suit2 == Suit.DIAMONDS || suit3 == Suit.DIAMONDS)) {
			/*
			 * If there are no diamonds in the first hand, we don't need them in
			 * the second hand (because it contains at most 2 suits different
			 * from the clubs and we already have hearts and spades).
			 */
			Utils.permuteSuits(cards, (suit2 != Suit.HEARTS && suit3 != Suit.HEARTS) ? Suit.HEARTS : Suit.SPADES, Suit.DIAMONDS);
			suit2 = cards[2].getSuit();
			suit3 = cards[3].getSuit();
		}

		if (suit2 == Suit.SPADES && suit3 == Suit.HEARTS) {
			/*
			 * If there is a heart AND a spade we can always permute the suits
			 * so that the heart is first.
			 */
			Utils.permuteSuits(cards, Suit.HEARTS, Suit.SPADES);
			suit2 = Suit.HEARTS;
			suit3 = Suit.SPADES;
		} else if (suit2 != Suit.HEARTS && suit3 != Suit.HEARTS && (suit2 == Suit.SPADES || suit3 == Suit.SPADES)) {
			/*
			 * If there is no heart and a spade we can always permute the suits
			 * so that the spade becomes a heart.
			 */
			Utils.permuteSuits(cards, Suit.HEARTS, Suit.SPADES);
			suit2 = cards[2].getSuit();
			suit3 = cards[3].getSuit();
		}

		// If the second hand is suited, we don't need spades.
		if (suit2 == suit3 && suit2 == Suit.SPADES) {
			Utils.permuteSuits(cards, Suit.HEARTS, Suit.SPADES);
			suit2 = suit3 = Suit.HEARTS;
		}
		/*
		 * If the first hand is a pair, and the second hand has diamonds but no
		 * club, we can permute the pair and switch clubs and diamonds.
		 */
		if (cards[0].getRank() == cards[1].getRank() && (suit2 != Suit.CLUBS && suit3 != Suit.CLUBS && (suit2 == Suit.DIAMONDS || suit3 == Suit.DIAMONDS))) {
			Utils.permuteSuits(cards, Suit.CLUBS, Suit.DIAMONDS);
			ArrayUtils.swap(cards, 0, 1);
		}

		// Make sure the second hand is still sorted properly.
		if (cards[2].compareTo(cards[3]) > 0) {
			ArrayUtils.swap(cards, 2, 3);
		}

		/* Find the hand odds. */
		final int hand = (cards[0].ordinal() << 24) | (cards[1].ordinal() << 16) | (cards[2].ordinal() << 8) | cards[3].ordinal();
		final int index = Arrays.binarySearch(hands, hand);
		assert (index >= 0);
		return v1 > v2 ? odds[index].reverse() : odds[index];
	}

}
