package com.sebster.math.lcp;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.math.rational.Rational;
import com.sebster.util.Validate;
import com.sebster.util.collections.ImmutablePair;
import com.sebster.util.collections.Pair;

public final class FineMultiThreadedSimpleLCPSolver {

	private static final Logger logger = LoggerFactory.getLogger(FineMultiThreadedSimpleLCPSolver.class);

	private FineMultiThreadedSimpleLCPSolver() {
		// Utility class.
	}

	public static Rational[] solve(final Rational[][] M, final Rational[] b, final int threads) {

		try {

			// ----------------------------------
			// Validation.

			Validate.notNull(M, "M is null");
			Validate.notNull(b, "b is null");
			Validate.isTrue(M.length == b.length, "row dimensions of M and b must be equal");

			final int n = M.length;
			logger.debug("doing LCP of size {}", n);

			final BoundedExecutor executor = new BoundedExecutor(Executors.newFixedThreadPool(threads), threads + 1000);

			// ----------------------------------
			// Initialization.

			// Perturb b.
			final PerturbedRational[] q = new PerturbedRational[n];
			q[0] = new PerturbedRational(n, b[0]);
			for (int i = 1; i < n; i++) {
				q[i] = new PerturbedRational(n, b[i]);
				q[i].set(i, Rational.ONE);
			}

			// I -d -M (initial tableaux), d = [1, 1, ..., 1]^T
			final int m = n + 1 + n;
			final Rational[][] tableaux = new Rational[n][m];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					tableaux[i][j] = (i == j) ? Rational.ONE : Rational.ZERO;
				}
				tableaux[i][n] = Rational.ONE.opposite();
				for (int j = 0; j < n; j++) {
					tableaux[i][n + 1 + j] = M[i][j].opposite();
				}
			}

			// Calculate initial basis = { w1, w2, ..., wn }.
			final int[] basis = new int[n];
			for (int i = 0; i < n; i++) {
				basis[i] = i;
			}

			// Calculate initial entering variable.
			int enteringVariable = n; // z0

			// Find initial leaving variable.
			int leavingIndex = -1;
			PerturbedRational qMin = PerturbedRational.zero(n);
			for (int i = 0; i < n; i++) {
				if (q[i].compareTo(qMin) < 0) {
					qMin = q[i];
					leavingIndex = i;
				}
			}

			// Check if we found a leaving variable.
			if (leavingIndex == -1) {
				// No leaving variable found, z0 == 0, z = 0, stop.
				return makeResult(basis, q);
			}

			// ----------------------------------
			// Main loop.
			while (true) {
				final int fLeavingIndex = leavingIndex;

				// Step 0: Update basis.
				final int leavingVariable = basis[leavingIndex];
				basis[leavingIndex] = enteringVariable;
				logger.debug("entering variable = {}, leaving variable = {}", varToString(enteringVariable, n), varToString(leavingVariable, n));

				// Step 1: Divide pivot row by pivot number.
				final CountDownLatch latch1 = new CountDownLatch(m + 1);
				final Rational pivotNumber = tableaux[leavingIndex][enteringVariable];
				executor.submit(new Runnable() {
					public void run() {
						q[fLeavingIndex] = q[fLeavingIndex].dividedBy(pivotNumber);
						latch1.countDown();
					}
				});
				for (int i = 0; i < m; i++) {
					final int fi = i;
					executor.submit(new Runnable() {
						public void run() {
							tableaux[fLeavingIndex][fi] = tableaux[fLeavingIndex][fi].dividedBy(pivotNumber);
							latch1.countDown();
						}
					});
				}
				latch1.await();

				// Step 2: Subtract pivot row appropriate number of times from
				// each row.
				final CountDownLatch latch2 = new CountDownLatch((n - 1) * m + n - 1);
				for (int i = 0; i < n; i++) {
					final int fi = i;
					if (i == leavingIndex)
						continue;
					final Rational factor = tableaux[i][enteringVariable];
					executor.submit(new Runnable() {
						public void run() {
							q[fi] = q[fi].minus(q[fLeavingIndex].times(factor));
							latch2.countDown();
						}
					});
				}
				for (int i = 0; i < n; i++) {
					final int fi = i;
					if (i == leavingIndex)
						continue;
					final Rational factor = tableaux[i][enteringVariable];
					for (int j = 0; j < m; j++) {
						final int fj = j;
						executor.submit(new Runnable() {
							public void run() {
								tableaux[fi][fj] = tableaux[fi][fj].minus(tableaux[fLeavingIndex][fj].times(factor));
								latch2.countDown();
							}
						});
					}
				}
				latch2.await();

				// Step 3: Enter complement.
				if (leavingVariable == n) {
					// z0 has no complement, so we're done.
					return makeResult(basis, q);
				}
				// 0..(n - 1) <-> (n + 1)..2n
				enteringVariable = leavingVariable + (leavingVariable < n ? (n + 1) : (-n - 1));

				// Step 4: Find new leaving index.
				leavingIndex = -1;
				final BlockingQueue<Pair<PerturbedRational, Integer>> ratios = new LinkedBlockingDeque<Pair<PerturbedRational, Integer>>();
				final int fEnteringVariable = enteringVariable;
				int compares = -1;
				for (int i = 0; i < n; i++) {
					final int fi = i;
					if (tableaux[i][enteringVariable].signum() > 0) {
						executor.submit(new Runnable() {
							public void run() {
								final PerturbedRational ratio = q[fi].dividedBy(tableaux[fi][fEnteringVariable]);
								ratios.add(new ImmutablePair<PerturbedRational, Integer>(ratio, fi));
							};
						});
						compares++;
					}
				}
				if (compares >= 0) {
					final CountDownLatch latch3 = new CountDownLatch(compares);
					for (int i = 0; i < compares; i++) {
						executor.submit(new Runnable() {
							public void run() {
								try {
									final Pair<PerturbedRational, Integer> first, second;
									synchronized (ratios) {
										first = ratios.take();
										second = ratios.take();
									}
									if (first.getFirst().compareTo(second.getFirst()) < 0) {
										ratios.put(first);
									} else {
										ratios.put(second);
									}
									latch3.countDown();
								} catch (final InterruptedException e) {
									// FIXME this causes the main thread to
									// block.
									throw new RuntimeException(e);
								}
							};
						});
					}
					latch3.await();
					leavingIndex = ratios.take().getSecond();
				} else {
					throw new IllegalStateException("no leaving variable found");
				}
			}

		} catch (final InterruptedException e) {
			// FIXME better error handling
			throw new RuntimeException(e);
		}
	}

	private static Rational[] makeResult(final int[] basis, final PerturbedRational[] q) {
		final int n = basis.length;
		final Rational[] z = new Rational[n];
		Arrays.fill(z, Rational.ZERO);
		for (int i = 0; i < n; i++) {
			final int variable = basis[i];
			final Rational value = q[i].rationalValue();
			if (variable > n) {
				z[variable - n - 1] = value;
			}
		}
		return z;
	}

	private static String varToString(final int i, final int n) {
		return i < n ? ("w" + (i + 1)) : "z" + (i - n);
	}

	public static class BoundedExecutor {
		private final Executor executor;
		private final Semaphore semaphore;

		public BoundedExecutor(final Executor executor, final int bound) {
			this.executor = executor;
			this.semaphore = new Semaphore(bound);
		}

		public void submit(final Runnable command) throws InterruptedException, RejectedExecutionException {
			semaphore.acquire();
			try {
				executor.execute(new Runnable() {
					public void run() {
						try {
							command.run();
						} finally {
							semaphore.release();
						}
					}
				});
			} catch (RejectedExecutionException e) {
				semaphore.release();
				throw e;
			}
		}
	}

}
