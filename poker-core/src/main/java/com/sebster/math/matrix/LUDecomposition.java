package com.sebster.math.matrix;

import java.io.Serializable;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;

/**
 * LU Decomposition.
 * <P>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit lower triangular matrix L, an n-by-n upper triangular matrix
 * U, and a permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L is m-by-m and U is m-by-n.
 * <P>
 * The LU decompostion with pivoting always exists, even if the matrix is singular, so the constructor will never fail. The primary use of
 * the LU decomposition is in the solution of square systems of simultaneous linear equations. This will fail if isNonsingular() returns
 * false.
 */

public class LUDecomposition<T extends FieldValue<T> & Comparable<T> & Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * ------------------------ Class variables ------------------------
	 */

	/**
	 * Array for internal storage of decomposition.
	 * 
	 * @serial internal array storage.
	 */
	private MatrixImpl<T> LU;

	/**
	 * Row and column dimensions, and pivot sign.
	 * 
	 * @serial column dimension.
	 * @serial row dimension.
	 * @serial pivot sign.
	 */
	private int m, n, pivsign;

	/**
	 * Internal storage of pivot vector.
	 * 
	 * @serial pivot vector.
	 */
	private int[] piv;

	/*
	 * ------------------------ Constructor ------------------------
	 */

	/**
	 * LU Decomposition
	 * 
	 * @param A
	 *            Rectangular matrix
	 * @return Structure to access L, U and piv.
	 */

	public LUDecomposition(MatrixImpl<T> A) {

		// Use a "left-looking", dot-product, Crout/Doolittle algorithm.

		LU = A.copy();
		m = A.getRowDimension();
		n = A.getColumnDimension();
		piv = new int[m];
		for (int i = 0; i < m; i++) {
			piv[i] = i;
		}
		pivsign = 1;

		// Outer loop.

		for (int j = 0; j < n; j++) {

			// Apply previous transformations.

			for (int i = 0; i < m; i++) {

				// Most of the time is spent in the following dot product.

				int kmax = Math.min(i, j);
				T s = LU.getField().getZero();
				for (int k = 0; k < kmax; k++) {
					s = s.plus(LU.get(i, k).times(LU.get(k, j)));
				}

				LU.set(i, j, LU.get(i, j).minus(s));
			}

			// Find pivot and exchange if necessary.

			int p = j;
			for (int i = j + 1; i < m; i++) {
				if (LU.get(i, j).abs().compareTo(LU.get(p, j).abs()) > 0) {
					p = i;
				}
			}
			if (p != j) {
				for (int k = 0; k < n; k++) {
					T t = LU.get(p, k);
					LU.set(p, k, LU.get(j, k));
					LU.set(j, k, t);
				}
				int k = piv[p];
				piv[p] = piv[j];
				piv[j] = k;
				pivsign = -pivsign;
			}

			// Compute multipliers.

			if (j < m & LU.get(j, j).signum() != 0) {
				for (int i = j + 1; i < m; i++) {
					LU.set(i, j, LU.get(i, j).dividedBy(LU.get(j, j)));
				}
			}
		}
	}

	/*
	 * ------------------------ Temporary, experimental code. ------------------------ *\
	 * 
	 * \** LU Decomposition, computed by Gaussian elimination. <P> This constructor computes L and U with the "daxpy"-based elimination
	 * algorithm used in LINPACK and MATLAB. In Java, we suspect the dot-product, Crout algorithm will be faster. We have temporarily
	 * included this constructor until timing experiments confirm this suspicion. <P>
	 * 
	 * @param A Rectangular matrix
	 * 
	 * @param linpackflag Use Gaussian elimination. Actual value ignored.
	 * 
	 * @return Structure to access L, U and piv.\
	 * 
	 * public LUDecomposition (Matrix A, int linpackflag) { // Initialize. LU = A.getArrayCopy(); m = A.getRowDimension(); n =
	 * A.getColumnDimension(); piv = new int[m]; for (int i = 0; i < m; i++) { piv[i] = i; } pivsign = 1; // Main loop. for (int k = 0; k <
	 * n; k++) { // Find pivot. int p = k; for (int i = k+1; i < m; i++) { if (Math.abs(LU[i][k]) > Math.abs(LU[p][k])) { p = i; } } //
	 * Exchange if necessary. if (p != k) { for (int j = 0; j < n; j++) { double t = LU[p][j]; LU[p][j] = LU[k][j]; LU[k][j] = t; } int t =
	 * piv[p]; piv[p] = piv[k]; piv[k] = t; pivsign = -pivsign; } // Compute multipliers and eliminate k-th column. if (LU[k][k] != 0.0) {
	 * for (int i = k+1; i < m; i++) { LU[i][k] /= LU[k][k]; for (int j = k+1; j < n; j++) { LU[i][j] -= LU[i][k]*LU[k][j]; } } } } }
	 * 
	 * \* ------------------------ End of temporary code. ------------------------
	 */

	/*
	 * ------------------------ Public Methods ------------------------
	 */

	/**
	 * Is the matrix nonsingular?
	 * 
	 * @return true if U, and hence A, is nonsingular.
	 */

	public boolean isNonsingular() {
		for (int j = 0; j < n; j++) {
			if (LU.get(j, j).signum() == 0)
				return false;
		}
		return true;
	}

	/**
	 * Return lower triangular factor
	 * 
	 * @return L
	 */

	public MatrixImpl<T> getL() {
		MatrixImpl<T> X = new MatrixImpl<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (i > j) {
					X.set(i, j, LU.get(i, j));
				} else if (i == j) {
					X.set(i, j, LU.getField().getOne());
				} else {
					X.set(i, j, LU.getField().getZero());
				}
			}
		}
		return X;
	}

	/**
	 * Return upper triangular factor
	 * 
	 * @return U
	 */

	public MatrixImpl<T> getU() {
		MatrixImpl<T> X = new MatrixImpl<T>(n, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i <= j) {
					X.set(i, j, LU.get(i, j));
				} else {
					X.set(i, j, LU.getField().getZero());
				}
			}
		}
		return X;
	}

	/**
	 * Return pivot permutation vector
	 * 
	 * @return piv
	 */

	public int[] getPivot() {
		int[] p = new int[m];
		for (int i = 0; i < m; i++) {
			p[i] = piv[i];
		}
		return p;
	}

	/**
	 * Return pivot permutation vector as a one-dimensional double array
	 * 
	 * @return (double) piv
	 */

	public double[] getDoublePivot() {
		double[] vals = new double[m];
		for (int i = 0; i < m; i++) {
			vals[i] = piv[i];
		}
		return vals;
	}

	/**
	 * Determinant
	 * 
	 * @return det(A)
	 * @exception IllegalArgumentException
	 *                Matrix must be square
	 */

	public T det() {
		if (m != n) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		final Field<T> field = LU.getField();
		T d = pivsign == 0 ? field.getZero() : (pivsign > 0 ? field.getOne() : field.getOne().opposite());
		for (int j = 0; j < n; j++) {
			d = d.times(LU.get(j, j));
		}
		return d;
	}

	/**
	 * Solve A*X = B
	 * 
	 * @param B
	 *            A Matrix with as many rows as A and any number of columns.
	 * @return X so that L*U*X = B(piv,:)
	 * @exception IllegalArgumentException
	 *                Matrix row dimensions must agree.
	 * @exception RuntimeException
	 *                Matrix is singular.
	 */

	public MatrixImpl<T> solve(MatrixImpl<T> B) {
		if (B.getRowDimension() != m) {
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		}
		if (!this.isNonsingular()) {
			throw new RuntimeException("Matrix is singular.");
		}

		// Copy right hand side with pivoting
		int nx = B.getColumnDimension();
		MatrixImpl<T> X = B.getMatrix(piv, 0, nx - 1);

		// Solve L*Y = B(piv,:)
		for (int k = 0; k < n; k++) {
			for (int i = k + 1; i < n; i++) {
				for (int j = 0; j < nx; j++) {
					X.set(i, j, X.get(i, j).minus(X.get(k, j).times(LU.get(i, k))));
				}
			}
		}
		// Solve U*X = Y;
		for (int k = n - 1; k >= 0; k--) {
			for (int j = 0; j < nx; j++) {
				X.set(k, j, X.get(k, j).dividedBy(LU.get(k, k)));
			}
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < nx; j++) {
					X.set(i, j, X.get(i, j).minus(X.get(k, j).times(LU.get(i, k))));
				}
			}
		}
		return X;
	}
}
