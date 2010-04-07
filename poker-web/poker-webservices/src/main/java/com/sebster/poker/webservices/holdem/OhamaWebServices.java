package com.sebster.poker.webservices.holdem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.zip.GZIPInputStream;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.Hole4;
import com.sebster.poker.odds.CompressedHandValueDB;
import com.sebster.poker.odds.Odds;
import com.sebster.poker.ohama.odds.PreFlopOddsCalculator;

public class OhamaWebServices {

	private static final Logger logger = LoggerFactory.getLogger(OhamaWebServices.class);

	private final CompressedHandValueDB db;

	private final ExecutorService executor;

	private final ThreadLocal<PreFlopOddsCalculator> calculator = new ThreadLocal<PreFlopOddsCalculator>();

	private final LRUMap cache;

	public OhamaWebServices(final String dbPath, final int cacheSize, final ExecutorService exector) throws IOException {

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

	public Odds[] calculateOdds(final Hole4[] holes) throws InterruptedException, ExecutionException {
		return calculateOdds(holes, null);
	}

	public Odds[] calculateOdds(final Hole4[] holes, final Card[] board) throws InterruptedException, ExecutionException {
		if (board == null || board.length == 0) {
			final List<Hole4> key = Arrays.asList(holes);
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
			throw new UnsupportedOperationException();
//			return PostFlopOddsCalculator.getInstance().calculateOdds(holes, board);
		}
	}

	private class OddsCalculatorCallable implements Callable<Odds[]> {

		private final Hole4[] holes;

		public OddsCalculatorCallable(final Hole4[] holes) {
			this.holes = holes;
		}

		@Override
		public Odds[] call() throws Exception {
			final long t1 = System.currentTimeMillis();
			PreFlopOddsCalculator calculator = OhamaWebServices.this.calculator.get();
			if (calculator == null) {
				calculator = new PreFlopOddsCalculator(db);
				OhamaWebServices.this.calculator.set(calculator);
			}
			final Odds[] odds = calculator.calculateOdds(holes);
			final long t2 = System.currentTimeMillis();
			if (logger.isDebugEnabled()) {
				logger.debug("{} player odds calculated in {} ms, expand in {} ms, compare in {} ms", new Object[] { holes.length, t2 - t1, calculator.getLastExpandTime(), calculator.getLastCompareTime() });
			}
			return odds;
		}

	}

}
