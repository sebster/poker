package com.sebster.poker.webservices.holdem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.holdem.Hole;
import com.sebster.poker.holdem.odds.CompressedHandValueDB;
import com.sebster.poker.holdem.odds.Odds;
import com.sebster.poker.holdem.odds.PostFlopOddsCalculator;
import com.sebster.poker.holdem.odds.PreFlopOddsCalculator;
import com.sebster.poker.holdem.odds.TwoPlayerOdds;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopOddsDB;

public class HoldemWebServices {

	private static final Logger logger = LoggerFactory.getLogger(HoldemWebServices.class);

	private final CompressedHandValueDB db;

	private final ExecutorService executor;

	private final ThreadLocal<PreFlopOddsCalculator> calculator = new ThreadLocal<PreFlopOddsCalculator>();

	public HoldemWebServices(final String dbPath, final ExecutorService exector) throws IOException {

		// Initialize compressed hand value db.
		InputStream in = null;
		try {
			in = new GZIPInputStream(new FileInputStream(dbPath));
			db = new CompressedHandValueDB(in);
		} finally {
			IOUtils.closeQuietly(in);
		}

		// Initialize task thread pool.
		this.executor = exector;
	}

	public Odds[] calculateOdds(final Hole[] holes) throws InterruptedException, ExecutionException {
		return calculateOdds(holes, null);
	}

	public Odds[] calculateOdds(final Hole[] holes, final Card[] board) throws InterruptedException, ExecutionException {
		if (board == null || board.length == 0) {
			if (holes.length == 2) {
				logger.debug("2 player odds requested");
				final long t1 = System.currentTimeMillis();
				final TwoPlayerOdds odds = TwoPlayerPreFlopOddsDB.getInstance().getOdds(holes[0], holes[1]);
				final long t2 = System.currentTimeMillis();
				logger.debug("2 player odds calculated in {} ms", t2 - t1);
				return new Odds[] { odds, odds.reverse() };
			}
			return executor.submit(new OddsCalculatorCallable(holes)).get();
		} else {
			return PostFlopOddsCalculator.getInstance().calculateOdds(holes, board);
		}
	}

	private class OddsCalculatorCallable implements Callable<Odds[]> {

		private final Hole[] holes;

		public OddsCalculatorCallable(final Hole[] holes) {
			this.holes = holes;
		}

		@Override
		public Odds[] call() throws Exception {
			logger.debug("n player odds requested, n={}", holes.length);
			final long t1 = System.currentTimeMillis();
			PreFlopOddsCalculator calculator = HoldemWebServices.this.calculator.get();
			if (calculator == null) {
				calculator = new PreFlopOddsCalculator(db);
				HoldemWebServices.this.calculator.set(calculator);
			}
			final Odds[] odds = calculator.calculateOdds(holes);
			final long t2 = System.currentTimeMillis();
			logger.debug("n player odds calculated in {} ms, expand in {} ms, compare in {} ms", new Object[] { t2 - t1, calculator.getLastExpandTime(), calculator.getLastExpandTime() });
			return odds;
		}

	}

}
