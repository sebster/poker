package com.sebster.math.lcp;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.math.rational.PerturbedRational;
import com.sebster.math.rational.Rational;
import com.sebster.util.Validate;

public final class MultiThreadedSimpleLCPSolver {

	private static final Logger logger = LoggerFactory.getLogger(MultiThreadedSimpleLCPSolver.class);

	private MultiThreadedSimpleLCPSolver() {
		// Utility class.
	}

	public static Rational[] solve(final Rational[][] M, final Rational[] b, final int threads) {

		// ----------------------------------
		// Validation.

		Validate.notNull(M, "M is null");
		Validate.notNull(b, "b is null");
		Validate.isTrue(M.length == b.length, "row dimensions of M and b must be equal");

		final int n = M.length;
		final int m = n + 1 + n;
		logger.debug("doing LCP of size {}", n);

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
		final Rational[][] tableaux = new Rational[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				tableaux[i][j] = (i == j) ? Rational.ONE : Rational.ZERO;
			}
			tableaux[i][n] = Rational.ONE.negate();
			for (int j = 0; j < n; j++) {
				tableaux[i][n + 1 + j] = M[i][j].negate();
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

		final ExecutorService executor = Executors.newFixedThreadPool(threads);

		while (true) {
			// Step 0: Update basis.
			final int leavingVariable = basis[leavingIndex];
			basis[leavingIndex] = enteringVariable;
			logger.debug("entering variable = {}, leaving variable = {}", varToString(enteringVariable, n), varToString(leavingVariable, n));

			// Step 1: Divide pivot row by pivot number.
			final Rational pivotNumber = tableaux[leavingIndex][enteringVariable];
			for (int i = 0; i < n + 1 + n; i++) {
				tableaux[leavingIndex][i] = tableaux[leavingIndex][i].divide(pivotNumber);
			}
			q[leavingIndex] = q[leavingIndex].divide(pivotNumber);

			// Step 2: Subtract pivot row appropriate number of times from each row.
			final int fEnteringVariable = enteringVariable;
			final int fLeavingIndex = leavingIndex;
			final CountDownLatch latch = new CountDownLatch(n - 1);
			for (int i = 0; i < n; i++) {
				if (i == leavingIndex)
					continue;
				final int fi = i;
				executor.submit(new Runnable() {
					@Override
					public void run() {
						final Rational factor = tableaux[fi][fEnteringVariable];
						for (int j = 0; j < m; j++) {
							tableaux[fi][j] = tableaux[fi][j].subtract(tableaux[fLeavingIndex][j].multiply(factor));
						}
						q[fi] = q[fi].subtract(q[fLeavingIndex].multiply(factor));
						latch.countDown();
					}
				});
			}
			try {
				latch.await();
			} catch (final InterruptedException e) {
				throw new RuntimeException("interrupted", e);
			}
			
			// Step 3: Enter complement.
			if (leavingVariable == n) {
				// z0 has no complement, so we're done.
				return makeResult(basis, q);
			}
			// 0..(n - 1) <-> (n + 1)..2n
			enteringVariable = leavingVariable + (leavingVariable < n ? (n + 1) : (-n - 1));

			// Step 4: Find new leaving index.
			leavingIndex = -1;
			PerturbedRational minRatio = null;
			for (int i = 0; i < n; i++) {
				if (tableaux[i][enteringVariable].signum() > 0) {
					final PerturbedRational ratio = q[i].divide(tableaux[i][enteringVariable]);
					if (leavingIndex == -1 || ratio.compareTo(minRatio) < 0) {
						leavingIndex = i;
						minRatio = ratio;
					}
				}
			}
			if (leavingIndex == -1) {
				// FIXME proper exception
				throw new IllegalStateException("no leaving variable found");
			}
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

}
