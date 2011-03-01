package com.sebster.lp;

import java.util.Arrays;

import com.sebster.math.rational.Rational;

public class Tableaux {

	private static final int MAX_DEGENERATE_ITERATIONS = 50;
	
	private final int n;

	private final int m;

	private Rational[][] tableaux;

	private Rational[][] solved;

	private int[] basis;

	public Tableaux(final int n, final int m, Rational[][] tableaux) {
		this.n = n;
		this.m = m;
		this.tableaux = new Rational[m + 1][n + m + 1];
		for (int i = 0; i < m + 1; i++) {
			for (int j = 0; j < n + m + 1; j++) {
				this.tableaux[i][j] = tableaux[i][j];
			}
		}
	}

	public void solve() {
		solved = new Rational[m + 1][n + m + 1];
		for (int j = 0; j < m + 1; j++) {
			solved[j] = tableaux[j].clone();
		}
		basis = new int[m];
		for (int i = 0; i < m; i++) {
			basis[i] = n + i;
		}
		Rational negObjectiveValue = tableaux[m][n + m];
		int degenerateIterations = 0;
		
		while (true) {
//			System.out.println(this);
			// Step 1, find pivot column.
			int pivotColumn = -1;
			Rational maxCoeff = Rational.ZERO;
			for (int j = 0; j < n + m + 1; j++) {
				if (solved[m][j].compareTo(maxCoeff) > 0) {
					maxCoeff = solved[m][j];
					pivotColumn = j;
					if (degenerateIterations > MAX_DEGENERATE_ITERATIONS) {
						// Use first subscript rule if too many degenerate iterations.
//						System.out.println("using first subscript rule");
						break;
					}
				}
			}
			if (pivotColumn == -1) {
				// No pivot column, we're done!
				break;
			}

			// Step 2, find the pivot row.
			int pivotRow = -1;
			Rational minRatio = null;
			for (int i = 0; i < m; i++) {
				if (solved[i][pivotColumn].signum() > 0) {
					Rational ratio = solved[i][n + m].dividedBy(solved[i][pivotColumn]);
					if (minRatio == null || ratio.compareTo(minRatio) < 0) {
						minRatio = ratio;
						pivotRow = i;
						if (degenerateIterations > MAX_DEGENERATE_ITERATIONS) {
							// Use first subscript rule if too many degenerate iterations.
//							System.out.println("using first subscript rule");
							break;
						}
					}
				}
			}
			if (pivotRow == -1) {
				// Unbounded problem.
				throw new UnboundedLPException();
			}
			
			// Do the pivot.
			basis[pivotRow] = pivotColumn;

			// Step 3, divide pivot row by pivot number.
			final Rational pivotNumber = solved[pivotRow][pivotColumn];
			for (int j = 0; j < n + m + 1; j++) {
				solved[pivotRow][j] = solved[pivotRow][j].dividedBy(pivotNumber);
			}

			// Step 4, make all other pivot column values zero.
			for (int i = 0; i < m + 1; i++) {
				if (i == pivotRow)
					continue;
				Rational factor = solved[i][pivotColumn];
				for (int j = 0; j < n + m + 1; j++) {
					solved[i][j] = solved[i][j].minus(solved[pivotRow][j].times(factor));
				}
			}
			
			if (negObjectiveValue.equals(solved[m][n + m])) {
				// Degenerate round.
				degenerateIterations++;
//				System.out.println("degenate iterations: " + degenerateIterations);
			} else {
				// Not degenerate.
				degenerateIterations = 0;
			}
			negObjectiveValue = solved[m][n + m];
		}
//		System.out.println(this);
	}

	public Rational getOptimum() {
		return solved[m][n + m].opposite();
	}

	public int[] getBasis() {
		return basis;
	}

	// pas op voor darken monus!!
	public Rational[] getVars() {
		final Rational[] vars = new Rational[n + m];
		for (int j = 0; j < n + m; j++) {
			vars[j] = Rational.ZERO;
		}
		for (int i = 0; i < m; i++) {
			vars[basis[i]] = solved[i][n + m];
		}
		return vars;
	}

	public Rational[] getObjectiveFunction() {
		final Rational[] of = new Rational[n];
		System.arraycopy(tableaux[m], 0, of, 0, n);
		return of;
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("[\n");
		for (int j = 0; j < m + 1; j++) {
			buffer.append("  ");
			buffer.append(Arrays.toString(solved[j]));
			buffer.append("\n");
		}
		buffer.append("]\n");
		return buffer.toString();
	}

}
