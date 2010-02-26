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

	private static final long serialVersionUID = 1L;

	private ExecutorService executorService;

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);

		final String dbPath = config.getInitParameter("handValueDbLocation");

		int threads = DEFAULT_THREADS;
		final String threadsParam = config.getInitParameter("threads");
		if (threadsParam != null) {
			threads = Integer.parseInt(threadsParam);

		}
		int queueSize = DEFAULT_QUEUE_SIZE;
		final String queueSizeParam = config.getInitParameter("queueSize");
		if (queueSizeParam != null) {
			queueSize = Integer.parseInt(queueSizeParam);
		}

		// Register our web service.
		try {
			JSONRPCBridge bridge = JSONRPCBridge.getGlobalBridge();
			executorService = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueSize));
			bridge.registerObject("holdem", new HoldemWebServices(dbPath, executorService));
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
