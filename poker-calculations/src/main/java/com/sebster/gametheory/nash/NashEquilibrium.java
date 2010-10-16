package com.sebster.gametheory.nash;

import java.util.Arrays;

import com.sebster.math.lcp.MultiThreadedSimpleLCPSolver;
import com.sebster.math.lcp.SimpleLCPSolver;
import com.sebster.math.rational.Rational;
import com.sebster.math.rational.matrix.Matrix;
import com.sebster.util.Assert;
import com.sebster.util.collections.ImmutablePair;
import com.sebster.util.collections.Pair;

public final class NashEquilibrium {

	private NashEquilibrium() {
		// Utility class.
	}

	public static Pair<Matrix<Rational>, Matrix<Rational>> solve(final Matrix<Rational> E, final Matrix<Rational> F, final Matrix<Rational> A, final Matrix<Rational> B) {
		return solve(E, F, A, B, 1);
	}

	/**
	 * Calculate the Nash equilibrium of the specified 2 player game.
	 * 
	 * @param E
	 *            the choice matrix of player 1
	 * @param F
	 *            the choice matrix of player 2
	 * @param A
	 *            the payoff matrix of player 1
	 * @param B
	 *            the payoff matrix of player 2
	 * 
	 * @return the strategy profile of player 1 and player 2
	 */
	public static Pair<Matrix<Rational>, Matrix<Rational>> solve(final Matrix<Rational> E, final Matrix<Rational> F, final Matrix<Rational> A, final Matrix<Rational> B, final int threads) {

		Assert.notNull(E, "E is null");
		Assert.notNull(F, "F is null");
		Assert.notNull(A, "A is null");
		Assert.notNull(B, "B is null");

		final int Ecols = E.getColumnDimension();
		final int Erows = E.getRowDimension();
		final int Fcols = F.getColumnDimension();
		final int Frows = F.getRowDimension();

		Assert.isTrue(A.getRowDimension() == Ecols, "A must have same number of rows as E has columns");
		Assert.isTrue(A.getColumnDimension() == Fcols, "A must have same number of columns as F");
		Assert.isTrue(B.getRowDimension() == Ecols, "B must have same number of rows as E has columns");
		Assert.isTrue(B.getColumnDimension() == Fcols, "B must have same number of columns as F");

		Rational max = Rational.ZERO;
		for (int i = 0; i < Ecols; i++) {
			for (int j = 0; j < Fcols; j++) {
				max = max.max(A.get(i, j)).max(B.get(i, j));
			}
		}
		final Matrix<Rational> Ap = new Matrix<Rational>(Ecols, Fcols, Rational.ZERO);
		final Matrix<Rational> Bp = new Matrix<Rational>(Ecols, Fcols, Rational.ZERO);
		for (int i = 0; i < Ecols; i++) {
			for (int j = 0; j < Fcols; j++) {
				Ap.set(i, j, A.get(i, j).subtract(max));
				Bp.set(i, j, B.get(i, j).subtract(max));
			}
		}

		final int Mdim = Ecols + Fcols + Erows * 2 + Frows * 2;

		// Calculate M =
		// ( 0 -A E^T -E^T 0 0)
		// (-B^T 0 0 0 F^T -F^T)
		// ( -E 0 0 0 0 0)
		// ( E 0 0 0 0 0)
		// ( 0 -F 0 0 0 0)
		// ( 0 F 0 0 0 0)

		// 1 - Zero out.
		final Rational[][] M = new Rational[Mdim][Mdim];
		for (int i = 0; i < Mdim; i++) {
			Arrays.fill(M[i], Rational.ZERO);
		}

		// 2 - E's.
		for (int i = 0; i < Erows; i++) {
			for (int j = 0; j < Ecols; j++) {
				M[Ecols + Fcols + i][j] = E.get(i, j).negate(); // -E
				M[Ecols + Fcols + Erows + i][j] = E.get(i, j); // E
				M[j][Ecols + Fcols + i] = E.get(i, j); // E^T
				M[j][Ecols + Fcols + Erows + i] = E.get(i, j).negate(); // -E^T
			}
		}

		// 3 - F's.
		for (int i = 0; i < Frows; i++) {
			for (int j = 0; j < Fcols; j++) {
				M[Ecols + j][Ecols + Fcols + 2 * Erows + i] = F.get(i, j); // F^T
				M[Ecols + j][Ecols + Fcols + 2 * Erows + Frows + i] = F.get(i, j).negate(); // -F^T
				M[Ecols + Fcols + 2 * Erows + i][Ecols + j] = F.get(i, j).negate(); // -F
				M[Ecols + Fcols + 2 * Erows + Frows + i][Ecols + j] = F.get(i, j); // F
			}
		}

		// 4 - -A and -B^T
		for (int i = 0; i < Ecols; i++) {
			for (int j = 0; j < Fcols; j++) {
				M[i][Ecols + j] = Ap.get(i, j).negate(); // -A
				M[Ecols + j][i] = Bp.get(i, j).negate(); // -B^T
			}
		}

		// Calculate b = (0 0 e -e f -f)^T where:
		// the first 0 vector has Ecols rows
		// the second 0 vector has Fcols rows
		// e = (1, 0, ..., 0)^T with Erows rows
		// f = (1, 0, ..., 0)^T with Frows rows
		final Rational[] b = new Rational[Mdim];
		Arrays.fill(b, Rational.ZERO);
		b[Ecols + Fcols] = Rational.ONE;
		b[Ecols + Fcols + Erows] = new Rational(-1);
		b[Ecols + Fcols + 2 * Erows] = Rational.ONE;
		b[Ecols + Fcols + 2 * Erows + Frows] = new Rational(-1);

		// Solve the LCP.
		final Rational[] z;
		if (threads == 1) {
			z = SimpleLCPSolver.solve(M, b);
		} else {
			z = MultiThreadedSimpleLCPSolver.solve(M, b, threads);
		}
		final Matrix<Rational> zM = new Matrix<Rational>(z, z.length);

		// Construct the result.
		final Matrix<Rational> x = zM.getSubMatrix(0, Ecols - 1, 0, 0);
		final Matrix<Rational> y = zM.getSubMatrix(Ecols, Ecols + Fcols - 1, 0, 0);

		// Return the result.
		return new ImmutablePair<Matrix<Rational>, Matrix<Rational>>(x, y);
	}

}
