package com.sebster.poker.webservices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.jabsorb.JSONRPCBridge;
import org.jabsorb.JSONRPCServlet;


public class PokerWebServicesServlet extends JSONRPCServlet {

	public static final int DEFAULT_THREADS = 2;

	public static final int DEFAULT_QUEUE_SIZE = 50;

	public static final int DEFAULT_CACHE_SIZE = 2048;

	private static final long serialVersionUID = 1L;

	private ExecutorService executorService;

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);

		String holdemDbPath = System.getProperty("com.sebster.poker.webservices.holdem.handValueDBLocation");
		if (holdemDbPath == null) {
			holdemDbPath = config.getInitParameter("holdem.handValueDBLocation");
			if (holdemDbPath == null) {
				throw new ServletException("holdem.handValueDBLocation parameter not set");
			}
		}

		String ohamaDbPath = System.getProperty("com.sebster.poker.webservices.ohama.handValueDBLocation");
		if (ohamaDbPath == null) {
			ohamaDbPath = config.getInitParameter("ohama.handValueDBLocation");
			if (ohamaDbPath == null) {
				throw new ServletException("ohama.handValueDBLocation parameter not set");
			}
		}

		int threads = DEFAULT_THREADS;
		String threadsParam = System.getProperty("com.sebster.poker.webservices.executorThreads");
		if (threadsParam == null) {
			threadsParam = config.getInitParameter("executorThreads");
		}
		if (threadsParam != null) {
			threads = Integer.parseInt(threadsParam);
		}

		int queueSize = DEFAULT_QUEUE_SIZE;
		String queueSizeParam = System.getProperty("com.sebster.poker.webservices.executorQueueSize");
		if (queueSizeParam == null) {
			config.getInitParameter("executorQueueSize");
		}
		if (queueSizeParam != null) {
			queueSize = Integer.parseInt(queueSizeParam);
		}

		int cacheSize = DEFAULT_CACHE_SIZE;
		String cacheSizeParam = System.getProperty("com.sebster.poker.webservices.cacheSize");
		if (cacheSizeParam == null) {
			cacheSizeParam = config.getInitParameter("cacheSize");
		}
		if (cacheSizeParam != null) {
			cacheSize = Integer.parseInt(cacheSizeParam);
		}

		executorService = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueSize));

		// Register our web service.
		try {
			final DecompressBufferHolder decompressBufferHolder = new DecompressBufferHolder();
			final JSONRPCBridge bridge = JSONRPCBridge.getGlobalBridge();
			bridge.registerObject("holdem", new HoldemWebServices(holdemDbPath, cacheSize, executorService, decompressBufferHolder));
			bridge.registerObject("ohama", new OhamaWebServices(ohamaDbPath, cacheSize, executorService, decompressBufferHolder));
			bridge.registerSerializer(new HoleSerializer());
			bridge.registerSerializer(new OddsSerializer());
			bridge.registerSerializer(new CardSerializer());
			bridge.setExceptionTransformer(new NoStackTraceErrorTransformer());
		} catch (final Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		if (executorService != null) {
			executorService.shutdown();
		}
		executorService = null;
		super.destroy();
	}

}
