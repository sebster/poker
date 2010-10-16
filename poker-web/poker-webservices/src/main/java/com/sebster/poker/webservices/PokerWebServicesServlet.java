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

	private static final long serialVersionUID = 1L;

	/** Service keys used for exporting */
	private static final String SERVICE_KEY_HOLDEM = "holdem";
	private static final String SERVICE_KEY_OMAHA = "omaha";

	/** Init parameter names */
	private static final String PARAM_HOLDEM_DB_LOCATION = "holdem.handValueDBLocation";
	private static final String PARAM_OMAHA_DB_LOCATION = "omaha.handValueDBLocation";
	private static final String PARAM_EXECUTOR_THREADS = "executorThreads";
	private static final String PARAM_EXECUTOR_QUEUE_SIZE = "executorQueueSize";
	private static final String PARAM_CACHE_SIZE = "cacheSize";

	/** System property names */
	private static final String PROPERTY_HOLDEM_ENABLE = "com.sebster.poker.webservices.holdem.disable";
	private static final String PROPERTY_HOLDEM_DB_LOCATION = "com.sebster.poker.webservices.holdem.handValueDBLocation";
	private static final String PROPERTY_OMAHA_ENABLE = "com.sebster.poker.webservices.omaha.disable";
	private static final String PROPERTY_OMAHA_DB_LOCATION = "com.sebster.poker.webservices.omaha.handValueDBLocation";
	private static final String PROPERTY_EXECUTOR_THREADS = "com.sebster.poker.webservices.executorThreads";
	private static final String PROPERTY_EXECUTOR_QUEUE_SIZE = "com.sebster.poker.webservices.executorQueueSize";
	private static final String PROPERTY_CACHE_SIZE = "com.sebster.poker.webservices.cacheSize";

	/** Default values */
	public static final int DEFAULT_THREADS = 2;
	public static final int DEFAULT_QUEUE_SIZE = 50;
	public static final int DEFAULT_CACHE_SIZE = 2048;

	/** Executor */
	// FIXME How do I make the Servlet stateless?
	private transient ExecutorService executorService;

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);

		int buffers = 0;

		boolean holdemEnable = !Boolean.valueOf(System.getProperty(PROPERTY_HOLDEM_ENABLE));
		String holdemDbPath = System.getProperty(PROPERTY_HOLDEM_DB_LOCATION);
		if (holdemEnable) {
			if (holdemDbPath == null) {
				holdemDbPath = config.getInitParameter(PARAM_HOLDEM_DB_LOCATION);
				if (holdemDbPath == null) {
					throw new ServletException(PARAM_HOLDEM_DB_LOCATION + " parameter not set");
				}
			}
			buffers = 10;
		}

		boolean omahaEnable = !Boolean.valueOf(System.getProperty(PROPERTY_OMAHA_ENABLE));
		String omahaDbPath = System.getProperty(PROPERTY_OMAHA_DB_LOCATION);
		if (omahaEnable) {
			if (omahaDbPath == null) {
				omahaDbPath = config.getInitParameter(PARAM_OMAHA_DB_LOCATION);
				if (omahaDbPath == null) {
					throw new ServletException(PARAM_OMAHA_DB_LOCATION + " parameter not set");
				}
			}
			buffers = 36;
		}

		int threads = DEFAULT_THREADS;
		String threadsParam = System.getProperty(PROPERTY_EXECUTOR_THREADS);
		if (threadsParam == null) {
			threadsParam = config.getInitParameter(PARAM_EXECUTOR_THREADS);
		}
		if (threadsParam != null) {
			threads = Integer.parseInt(threadsParam);
		}

		int queueSize = DEFAULT_QUEUE_SIZE;
		String queueSizeParam = System.getProperty(PROPERTY_EXECUTOR_QUEUE_SIZE);
		if (queueSizeParam == null) {
			config.getInitParameter(PARAM_EXECUTOR_QUEUE_SIZE);
		}
		if (queueSizeParam != null) {
			queueSize = Integer.parseInt(queueSizeParam);
		}

		int cacheSize = DEFAULT_CACHE_SIZE;
		String cacheSizeParam = System.getProperty(PROPERTY_CACHE_SIZE);
		if (cacheSizeParam == null) {
			cacheSizeParam = config.getInitParameter(PARAM_CACHE_SIZE);
		}
		if (cacheSizeParam != null) {
			cacheSize = Integer.parseInt(cacheSizeParam);
		}

		executorService = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueSize));

		// Register our web service.
		try {
			final DecompressBufferHolder decompressBufferHolder = new DecompressBufferHolder(buffers);
			final JSONRPCBridge bridge = JSONRPCBridge.getGlobalBridge();
			if (holdemEnable) {
				bridge.registerObject(SERVICE_KEY_HOLDEM, new HoldemWebServices(holdemDbPath, cacheSize, executorService, decompressBufferHolder));
			}
			if (omahaEnable) {
				bridge.registerObject(SERVICE_KEY_OMAHA, new OmahaWebServices(omahaDbPath, cacheSize, executorService, decompressBufferHolder));
			}
			bridge.registerSerializer(new HoleSerializer());
			bridge.registerSerializer(new Hole4Serializer());
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
