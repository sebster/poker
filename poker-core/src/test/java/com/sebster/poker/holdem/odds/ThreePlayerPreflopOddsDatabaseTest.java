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
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Odds;

public class ThreePlayerPreflopOddsDatabaseTest {

	private static final Logger LOG = LoggerFactory.getLogger(ThreePlayerPreflopOddsDatabaseTest.class);

	private static HoldemPreflopOddsCalculator calculator;

	private static HoldemThreePlayerPreflopOddsDatabase database;

	@BeforeClass
	public static void loadDatabases() throws IOException {
		calculator = new FastHoldemPreflopOddsCalculator(new CompressedHandValueDatabase(new BufferedInputStream(new GZIPInputStream(HoldemThreePlayerPreflopOddsDatabase.class.getResourceAsStream("holdem_hand_value_db.lzfi.gz")))));
		database = HoldemThreePlayerPreflopOddsDatabase.getInstance();
	}

	@Test
	public void testThreePlayerPreflopOddsDatabase() {
		for (int i = 0; i < 1000; i++) {
			final Deck deck = new Deck();
			final Hole[] holes = new Hole[3];
			for (int j = 0; j < 3; j++) {
				holes[j] = Hole.fromCards(deck.draw(), deck.draw());
			}
			LOG.debug("comparing hand odds for hand {}", Arrays.toString(holes));
			final Odds[] calculatedOdds = calculator.calculateOdds(holes);
			final Odds[] odds = database.getOdds(holes[0], holes[1], holes[2]);
			Assert.assertArrayEquals("odds mismatch for hands " + Arrays.toString(holes), calculatedOdds, odds);
		}
	}

}
