package com.sebster.poker.webservices;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.zip.GZIPInputStream;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.Hole;
import com.sebster.poker.holdem.odds.PostFlopOddsCalculator;
import com.sebster.poker.holdem.odds.FastHoldemPreflopOddsCalculator;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopOddsDB;
import com.sebster.poker.odds.CompressedHandValueDB;
import com.sebster.poker.odds.Odds;
import com.sebster.poker.odds.TwoPlayerOdds;
import com.sebster.util.arrays.ObjectArrayWrapper;

public class HoldemWebServices {

	private static final Logger logger = LoggerFactory.getLogger(HoldemWebServices.class);

	private final CompressedHandValueDB db;

	private final ExecutorService executor;

	private final ThreadLocal<FastHoldemPreflopOddsCalculator> calculator = new ThreadLocal<FastHoldemPreflopOddsCalculator>();

	private final LRUMap cache;

	public HoldemWebServices(final String dbPath, final int cacheSize, final ExecutorService exector) throws IOException {

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

		// Initialize the cache.
		cache = new LRUMap(cacheSize);
	}

	public Odds[] calculateOdds(final Hole[] holes) throws InterruptedException, ExecutionException {
		return calculateOdds(holes, null);
	}

	public Odds[] calculateOdds(final Hole[] holes, final Card[] board) throws InterruptedException, ExecutionException {
		if (board == null || board.length == 0) {
			if (holes.length == 2) {
				final long t1 = System.currentTimeMillis();
				final TwoPlayerOdds odds = TwoPlayerPreFlopOddsDB.getInstance().getOdds(holes[0], holes[1]);
				final long t2 = System.currentTimeMillis();
				if (logger.isDebugEnabled()) {
					logger.debug("2 player odds calculated in {} ms", t2 - t1);
				}
				return new Odds[] { odds, odds.reverse() };
			}
			final ObjectArrayWrapper<Hole> key = new ObjectArrayWrapper<Hole>(holes);
			synchronized (cache) {
				final Odds[] result = (Odds[]) cache.get(key);
				if (result != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("{} player odds result found in cache", holes.length);
					}
					return result;
				}
			}
			final Odds[] result = executor.submit(new OddsCalculatorCallable(holes)).get();
			synchronized (cache) {
				cache.put(key, result);
			}
			return result;
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
			final long t1 = System.currentTimeMillis();
			FastHoldemPreflopOddsCalculator calculator = HoldemWebServices.this.calculator.get();
			if (calculator == null) {
				calculator = new FastHoldemPreflopOddsCalculator(db);
				HoldemWebServices.this.calculator.set(calculator);
			}
			final Odds[] odds = calculator.calculateOdds(holes);
			final long t2 = System.currentTimeMillis();
			if (logger.isDebugEnabled()) {
				logger.debug("{} player odds calculated in {} ms, expand in {} ms, expand cache hits {}, compare in {} ms", new Object[] { holes.length, t2 - t1, calculator.getLastExpandTime(), calculator.getLastExpandCacheHits(), calculator.getLastCompareTime() });
			}
			return odds;
		}

	}

}
