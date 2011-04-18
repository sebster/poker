package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Deck;
import com.sebster.poker.Hole;
import com.sebster.poker.holdem.odds.HoldemThreePlayerPreflopOddsDatabase.IndexedOdds;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Odds;

public class HoldemThreePlayerPreflopOddsDatabaseTest {

	private static final Logger LOG = LoggerFactory.getLogger(HoldemThreePlayerPreflopOddsDatabaseTest.class);

	private static HoldemPreflopOddsCalculator calculator;

	private static HoldemThreePlayerPreflopOddsDatabase database;

	@BeforeClass
	public static void loadDatabases() throws IOException {
		final CompressedHandValueDatabase db = new CompressedHandValueDatabase(new BufferedInputStream(new GZIPInputStream(HoldemThreePlayerPreflopOddsDatabase.class.getResourceAsStream("holdem_hand_value_db.lzfi.gz"))));
		calculator = new FastHoldemPreflopOddsCalculator(db);
		database = HoldemThreePlayerPreflopOddsDatabase.getInstance();
	}

	@Test
	public void testThreePlayerPreflopOddsDatabase() {
		for (int i = 0; i < 10000; i++) {
			final Deck deck = new Deck();
			final Hole[] holes = new Hole[3];
			for (int j = 0; j < 3; j++) {
				holes[j] = Hole.fromCards(deck.draw(), deck.draw());
			}
			LOG.debug("comparing hand odds for hand {}", Arrays.toString(holes));
			final Odds[] calculatedOdds = calculator.calculateOdds(holes);
			final IndexedOdds odds = database.getOdds(holes[0], holes[1], holes[2]);
			final Odds[] odds2 = new Odds[3];
			for (int p = 0; p < 3; p++) {
				final int[] nWaySplits = new int[4];
				for (int n = 0; n <= 3; n++) {
					nWaySplits[n] = odds.getNWaySplits(p, n);
				}
				odds2[p] = new BasicOdds(nWaySplits);
			}
			Assert.assertArrayEquals("odds mismatch for hands " + Arrays.toString(holes), calculatedOdds, odds2);
		}
	}

}
