package com.sebster.poker.webservices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.jabsorb.JSONRPCBridge;
import org.jabsorb.JSONRPCServlet;

import com.sebster.poker.webservices.holdem.HoldemWebServices;

public class PokerWebServicesServlet extends JSONRPCServlet {

	public static final int DEFAULT_THREADS = 2;

	public static final int DEFAULT_QUEUE_SIZE = 50;
	
	public static final int DEFAULT_CACHE_SIZE = 2048;

	private static final long serialVersionUID = 1L;

	private ExecutorService executorService;

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);

		String dbPath = System.getProperty("com.sebster.poker.webservices.holdem.handValueDBLocation");
		if (dbPath == null) {
			dbPath = config.getInitParameter("holdem.handValueDBLocation");
			if (dbPath == null) {
				throw new ServletException("holdem.handValueDBLocation parameter not set");
			}
		}

		int threads = DEFAULT_THREADS;
		String threadsParam = System.getProperty("com.sebster.poker.webservices.holdem.executorThreads");
		if (threadsParam == null) {
			threadsParam = config.getInitParameter("holdem.executorThreads");
		}
		if (threadsParam != null) {
			threads = Integer.parseInt(threadsParam);
		}
		
		int queueSize = DEFAULT_QUEUE_SIZE;
		String queueSizeParam = System.getProperty("com.sebster.poker.webservices.holdem.executorQueueSize");
		if (queueSizeParam == null) {
			config.getInitParameter("holdem.executorQueueSize");
		}
		if (queueSizeParam != null) {
			queueSize = Integer.parseInt(queueSizeParam);
		}
		
		int cacheSize = DEFAULT_CACHE_SIZE;
		String cacheSizeParam = System.getProperty("com.sebster.poker.webservices.holdem.cacheSize");
		if (cacheSizeParam == null) {
			cacheSizeParam = config.getInitParameter("holdem.cacheSize");
		}
		if (cacheSizeParam != null) {
			cacheSize = Integer.parseInt(cacheSizeParam);
		}

		executorService = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueSize));

		// Register our web service.
		try {
			JSONRPCBridge bridge = JSONRPCBridge.getGlobalBridge();
			bridge.registerObject("holdem", new HoldemWebServices(dbPath, cacheSize, executorService));
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
