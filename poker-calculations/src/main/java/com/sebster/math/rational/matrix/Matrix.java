package com.sebster.math.rational.matrix;

import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.sebster.math.MathUtil;
import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

/**
 * Jama = Java Matrix class.
 * <P>
 * The Java Matrix Class provides the fundamental operations of numerical linear algebra. Various constructors create Matrices from two
 * dimensional arrays of double precision floating point numbers. Various "gets" and "sets" provide access to submatrices and matrix
 * elements. Several methods implement basic matrix arithmetic, including matrix addition and multiplication, matrix norms, and
 * element-by-element array operations. Methods for reading and printing matrices are also included. All the operations in this version of
 * the Matrix Class involve real matrices. Complex matrices may be handled in a future version.
 * <P>
 * Five fundamental matrix decompositions, which consist of pairs or triples of matrices, permutation vectors, and the like, produce results
 * in five decomposition classes. These decompositions are accessed by the Matrix class to compute solutions of simultaneous linear
 * equations, determinants, inverses and other matrix functions. The five decompositions are:
 * <P>
 * <UL>
 * <LI>Cholesky Decomposition of symmetric, positive definite matrices.
 * <LI>LU Decomposition of rectangular matrices.
 * <LI>QR Decomposition of rectangular matrices.
 * <LI>Singular Value Decomposition of rectangular matrices.
 * <LI>Eigenvalue Decomposition of both symmetric and nonsymmetric square matrices.
 * </UL>
 * <DL>
 * <DT><B>Example of use:</B></DT>
 * <P>
 * <DD>Solve a linear system A x = b and compute the residual norm, ||b - A x||.
 * <P>
 * 
 * <PRE>
 * double[][] vals = { { 1., 2., 3 }, { 4., 5., 6. }, { 7., 8., 10. } };
 * Matrix A = new Matrix(vals);
 * Matrix b = Matrix.random(3, 1);
 * Matrix x = A.solve(b);
 * Matrix r = A.times(x).minus(b);
 * double rnorm = r.normInf();
 * </PRE>
 * 
 * </DD>
 * </DL>
 * 
 * @author The MathWorks, Inc. and the National Institute of Standards and Technology.
 * @version 5 August 1998
 */

public class Matrix<T extends FieldValue<T> & Comparable<T> & Serializable> implements Cloneable, Serializable {

	/*
	 * ------------------------ Class variables ------------------------
	 */

	/**
	 * Array for internal storage of elements.
	 * 
	 * @serial internal array storage.
	 */
	private Object[][] a;

	/**
	 * Row and column dimensions.
	 * 
	 * @serial row dimension.
	 * @serial column dimension.
	 */
	private final int m, n;

	/*
	 * ------------------------ Constructors ------------------------
	 */

	protected Matrix(final int m, final int n) {
		Validate.isTrue(m > 0, "m must be non-negative");
		Validate.isTrue(n > 0, "n must be non-negative");
		this.m = m;
		this.n = n;
		a = new Object[m][n];
	}

	/**
	 * Construct an m-by-n constant matrix.
	 * 
	 * @param m
	 *            Number of rows.
	 * @param n
	 *            Number of colums.
	 * @param s
	 *            Fill the matrix with this scalar value.
	 */

	public Matrix(final int m, final int n, final T s) {
		this(m, n);
		Validate.notNull(s, "s == null");
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, s);
			}
		}
	}

	/**
	 * Construct a matrix from a 2-D array.
	 * 
	 * @param a
	 *            Two-dimensional array of doubles.
	 * @exception IllegalArgumentException
	 *                All rows must have the same length
	 */
	public Matrix(final T[][] a) {
		m = a.length;
		Validate.isTrue(m > 0, "m must be non-negative");
		n = a[0].length;
		Validate.isTrue(n > 0, "n must be non-negative");
		this.a = new Object[m][n];
		for (int i = 0; i < m; i++) {
			Validate.isTrue(a[i].length == n, "all rows must have the same length");
			System.arraycopy(a[i], 0, this.a[i], 0, n);
		}
	}

	/**
	 * Construct a matrix from a one-dimensional packed array
	 * 
	 * @param vals
	 *            One-dimensional array of doubles, packed by columns (ala Fortran).
	 * @param m
	 *            Number of rows.
	 * @exception IllegalArgumentException
	 *                Array length must be a multiple of m.
	 */
	public Matrix(final T vals[], final int m) {
		this.m = m;
		n = (m != 0 ? vals.length / m : 0);
		if (m * n != vals.length) {
			throw new IllegalArgumentException("Array length must be a multiple of m.");
		}
		a = new Object[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, vals[i + j * m]);
			}
		}
	}

	/*
	 * ------------------------ Public Methods ------------------------
	 */

	/**
	 * Make a deep copy of a matrix
	 */

	public Matrix<T> copy() {
		final Matrix<T> x = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				x.set(i, j, get(i, j));
			}
		}
		return x;
	}

	/**
	 * Clone the Matrix object.
	 */

	@Override
	public Matrix<T> clone() {
		return this.copy();
	}

	/**
	 * Get the field.
	 * 
	 * @return the field.
	 */
	public Field<T> getField() {
		return get(0, 0).getField();
	}

	/**
	 * Get row dimension.
	 * 
	 * @return m, the number of rows.
	 */

	public int getRowDimension() {
		return m;
	}

	/**
	 * Get column dimension.
	 * 
	 * @return n, the number of columns.
	 */

	public int getColumnDimension() {
		return n;
	}

	/**
	 * Get a single element.
	 * 
	 * @param i
	 *            Row index.
	 * @param j
	 *            Column index.
	 * @return A(i,j)
	 * @exception ArrayIndexOutOfBoundsException
	 */

	@SuppressWarnings("unchecked")
	public T get(int i, int j) {
		return (T) a[i][j];
	}

	/**
	 * Get a submatrix.
	 * 
	 * @param i0
	 *            Initial row index
	 * @param i1
	 *            Final row index
	 * @param j0
	 *            Initial column index
	 * @param j1
	 *            Final column index
	 * @return A(i0:i1,j0:j1)
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public Matrix<T> getSubMatrix(int i0, int i1, int j0, int j1) {
		Matrix<T> x = new Matrix<T>(i1 - i0 + 1, j1 - j0 + 1);
		try {
			for (int i = i0; i <= i1; i++) {
				for (int j = j0; j <= j1; j++) {
					x.set(i - i0, j - j0, get(i, j));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return x;
	}

	/**
	 * Get a submatrix.
	 * 
	 * @param r
	 *            Array of row indices.
	 * @param c
	 *            Array of column indices.
	 * @return A(r(:),c(:))
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public Matrix<T> getSubMatrix(int[] r, int[] c) {
		Matrix<T> x = new Matrix<T>(r.length, c.length);
		try {
			for (int i = 0; i < r.length; i++) {
				for (int j = 0; j < c.length; j++) {
					x.set(i, j, get(r[i], c[j]));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return x;
	}

	/**
	 * Get a submatrix.
	 * 
	 * @param i0
	 *            Initial row index
	 * @param i1
	 *            Final row index
	 * @param c
	 *            Array of column indices.
	 * @return A(i0:i1,c(:))
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public Matrix<T> getSubMatrix(int i0, int i1, int[] c) {
		Matrix<T> x = new Matrix<T>(i1 - i0 + 1, c.length);
		try {
			for (int i = i0; i <= i1; i++) {
				for (int j = 0; j < c.length; j++) {
					x.set(i - i0, j, get(i, c[j]));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return x;
	}

	/**
	 * Get a submatrix.
	 * 
	 * @param r
	 *            Array of row indices.
	 * @param i0
	 *            Initial column index
	 * @param i1
	 *            Final column index
	 * @return A(r(:),j0:j1)
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public Matrix<T> getMatrix(int[] r, int j0, int j1) {
		Matrix<T> x = new Matrix<T>(r.length, j1 - j0 + 1);
		try {
			for (int i = 0; i < r.length; i++) {
				for (int j = j0; j <= j1; j++) {
					x.set(i, j - j0, get(r[i], j));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return x;
	}

	/**
	 * Set a single element.
	 * 
	 * @param i
	 *            Row index.
	 * @param j
	 *            Column index.
	 * @param s
	 *            A(i,j).
	 * @exception ArrayIndexOutOfBoundsException
	 */

	public void set(int i, int j, T s) {
		a[i][j] = s;
	}

	/**
	 * Set a submatrix.
	 * 
	 * @param i0
	 *            Initial row index
	 * @param i1
	 *            Final row index
	 * @param j0
	 *            Initial column index
	 * @param j1
	 *            Final column index
	 * @param X
	 *            A(i0:i1,j0:j1)
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public void setMatrix(int i0, int i1, int j0, int j1, Matrix<T> X) {
		try {
			for (int i = i0; i <= i1; i++) {
				for (int j = j0; j <= j1; j++) {
					set(i, j, X.get(i - i0, j - j0));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/**
	 * Set a submatrix.
	 * 
	 * @param r
	 *            Array of row indices.
	 * @param c
	 *            Array of column indices.
	 * @param X
	 *            A(r(:),c(:))
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public void setMatrix(int[] r, int[] c, Matrix<T> X) {
		try {
			for (int i = 0; i < r.length; i++) {
				for (int j = 0; j < c.length; j++) {
					set(r[i], c[j], X.get(i, j));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/**
	 * Set a submatrix.
	 * 
	 * @param r
	 *            Array of row indices.
	 * @param j0
	 *            Initial column index
	 * @param j1
	 *            Final column index
	 * @param X
	 *            A(r(:),j0:j1)
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public void setMatrix(int[] r, int j0, int j1, Matrix<T> X) {
		try {
			for (int i = 0; i < r.length; i++) {
				for (int j = j0; j <= j1; j++) {
					set(r[i], j, X.get(i, j - j0));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/**
	 * Set a submatrix.
	 * 
	 * @param i0
	 *            Initial row index
	 * @param i1
	 *            Final row index
	 * @param c
	 *            Array of column indices.
	 * @param X
	 *            A(i0:i1,c(:))
	 * @exception ArrayIndexOutOfBoundsException
	 *                Submatrix indices
	 */

	public void setMatrix(int i0, int i1, int[] c, Matrix<T> X) {
		try {
			for (int i = i0; i <= i1; i++) {
				for (int j = 0; j < c.length; j++) {
					set(i, c[j], X.get(i - i0, j));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	/**
	 * Matrix transpose.
	 * 
	 * @return A'
	 */

	public Matrix<T> transpose() {
		Matrix<T> X = new Matrix<T>(n, m);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(j, i, get(i, j));
			}
		}
		return X;
	}

	/**
	 * One norm
	 * 
	 * @return maximum column sum.
	 */

	public T norm1() {
		T f = getField().getZero();
		for (int j = 0; j < n; j++) {
			T s = getField().getZero();
			for (int i = 0; i < m; i++) {
				s = s.add(get(i, j).abs());
			}
			f = MathUtil.max(f, s);
		}
		return f;
	}

	/**
	 * Two norm
	 * 
	 * @return maximum singular value.
	 */

	// public Rational norm2 () {
	// return (new SingularValueDecomposition(this).norm2());
	// }

	/**
	 * Infinity norm
	 * 
	 * @return maximum row sum.
	 */

	public T normInf() {
		T f = getField().getZero();
		for (int i = 0; i < m; i++) {
			T s = getField().getZero();
			for (int j = 0; j < n; j++) {
				s = s.add(get(i, j).abs());
			}
			f = MathUtil.max(f, s);
		}
		return f;
	}

	// /** Frobenius norm
	// @return sqrt of sum of squares of all elements.
	// */
	//
	// public double normF () {
	// double f = 0;
	// for (int i = 0; i < m; i++) {
	// for (int j = 0; j < n; j++) {
	// f = Maths.hypot(f,A[i][j]);
	// }
	// }
	// return f;
	// }

	/**
	 * Unary minus
	 * 
	 * @return -A
	 */

	public Matrix<T> uminus() {
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, get(i, j).negate());
			}
		}
		return X;
	}

	/**
	 * C = A + B
	 * 
	 * @param B
	 *            another matrix
	 * @return A + B
	 */

	public Matrix<T> plus(Matrix<T> B) {
		checkMatrixDimensions(B);
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, get(i, j).add(B.get(i, j)));
			}
		}
		return X;
	}

	/**
	 * A = A + B
	 * 
	 * @param B
	 *            another matrix
	 * @return A + B
	 */

	public Matrix<T> plusEquals(Matrix<T> B) {
		checkMatrixDimensions(B);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, get(i, j).add(B.get(i, j)));
			}
		}
		return this;
	}

	/**
	 * C = A - B
	 * 
	 * @param B
	 *            another matrix
	 * @return A - B
	 */

	public Matrix<T> minus(Matrix<T> B) {
		checkMatrixDimensions(B);
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, get(i, j).subtract(B.get(i, j)));
			}
		}
		return X;
	}

	/**
	 * A = A - B
	 * 
	 * @param B
	 *            another matrix
	 * @return A - B
	 */

	public Matrix<T> minusEquals(Matrix<T> B) {
		checkMatrixDimensions(B);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, get(i, j).subtract(B.get(i, j)));
			}
		}
		return this;
	}

	/**
	 * Element-by-element multiplication, C = A.*B
	 * 
	 * @param B
	 *            another matrix
	 * @return A.*B
	 */

	public Matrix<T> arrayTimes(Matrix<T> B) {
		checkMatrixDimensions(B);
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, get(i, j).multiply(B.get(i, j)));
			}
		}
		return X;
	}

	/**
	 * Element-by-element multiplication in place, A = A.*B
	 * 
	 * @param B
	 *            another matrix
	 * @return A.*B
	 */

	public Matrix<T> arrayTimesEquals(Matrix<T> B) {
		checkMatrixDimensions(B);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, get(i, j).multiply(B.get(i, j)));
			}
		}
		return this;
	}

	/**
	 * Element-by-element right division, C = A./B
	 * 
	 * @param B
	 *            another matrix
	 * @return A./B
	 */

	public Matrix<T> arrayRightDivide(Matrix<T> B) {
		checkMatrixDimensions(B);
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, get(i, j).divide(B.get(i, j)));
			}
		}
		return X;
	}

	/**
	 * Element-by-element right division in place, A = A./B
	 * 
	 * @param B
	 *            another matrix
	 * @return A./B
	 */

	public Matrix<T> arrayRightDivideEquals(Matrix<T> B) {
		checkMatrixDimensions(B);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, get(i, j).divide(B.get(i, j)));
			}
		}
		return this;
	}

	/**
	 * Element-by-element left division, C = A.\B
	 * 
	 * @param B
	 *            another matrix
	 * @return A.\B
	 */

	public Matrix<T> arrayLeftDivide(Matrix<T> B) {
		checkMatrixDimensions(B);
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, B.get(i, j).divide(get(i, j)));
			}
		}
		return X;
	}

	/**
	 * Element-by-element left division in place, A = A.\B
	 * 
	 * @param B
	 *            another matrix
	 * @return A.\B
	 */

	public Matrix<T> arrayLeftDivideEquals(Matrix<T> B) {
		checkMatrixDimensions(B);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, B.get(i, j).divide(get(i, j)));
			}
		}
		return this;
	}

	/**
	 * Multiply a matrix by a scalar, C = s*A
	 * 
	 * @param s
	 *            scalar
	 * @return s*A
	 */

	public Matrix<T> times(T s) {
		Matrix<T> X = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				X.set(i, j, s.multiply(get(i, j)));
			}
		}
		return X;
	}

	/**
	 * Multiply a matrix by a scalar in place, A = s*A
	 * 
	 * @param s
	 *            scalar
	 * @return replace A by s*A
	 */

	public Matrix<T> timesEquals(T s) {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				set(i, j, s.multiply(get(i, j)));
			}
		}
		return this;
	}

	/**
	 * Linear algebraic matrix multiplication, A * B
	 * 
	 * @param B
	 *            another matrix
	 * @return Matrix product, A * B
	 * @exception IllegalArgumentException
	 *                Matrix inner dimensions must agree.
	 */

	public Matrix<T> times(Matrix<T> B) {
		if (B.m != n) {
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		}
		Matrix<T> X = new Matrix<T>(m, B.n);
		for (int j = 0; j < B.n; j++) {
			for (int i = 0; i < m; i++) {
				T s = getField().getZero();
				for (int k = 0; k < n; k++) {
					s = s.add(get(i, k).multiply(B.get(k, j)));
				}
				X.set(i, j, s);
			}
		}
		return X;
	}

	/**
	 * LU Decomposition
	 * 
	 * @return LUDecomposition
	 * @see LUDecomposition
	 */

	public LUDecomposition<T> lu() {
		return new LUDecomposition<T>(this);
	}

	// /**
	// * QR Decomposition
	// *
	// * @return QRDecomposition
	// * @see QRDecomposition
	// */
	//
	// public QRDecomposition qr() {
	// return new QRDecomposition(this);
	// }

	// /**
	// * Cholesky Decomposition
	// *
	// * @return CholeskyDecomposition
	// * @see CholeskyDecomposition
	// */
	//
	// public CholeskyDecomposition chol() {
	// return new CholeskyDecomposition(this);
	// }

	// /** Singular Value Decomposition
	// @return SingularValueDecomposition
	// @see SingularValueDecomposition
	// */
	//
	// public SingularValueDecomposition svd () {
	// return new SingularValueDecomposition(this);
	// }

	// /** Eigenvalue Decomposition
	// @return EigenvalueDecomposition
	// @see EigenvalueDecomposition
	// */
	//
	// public EigenvalueDecomposition eig () {
	// return new EigenvalueDecomposition(this);
	// }

	/**
	 * Solve A*X = B
	 * 
	 * @param B
	 *            right hand side
	 * @return solution if A is square, least squares solution otherwise
	 */

	public Matrix<T> solve(Matrix<T> B) {
		if (m == n) {
			return new LUDecomposition<T>(this).solve(B);
		}
		throw new UnsupportedOperationException();
		// (new QRDecomposition(this)).solve(B));
	}

	/**
	 * Solve X*A = B, which is also A'*X' = B'
	 * 
	 * @param B
	 *            right hand side
	 * @return solution if A is square, least squares solution otherwise.
	 */

	public Matrix<T> solveTranspose(Matrix<T> B) {
		return transpose().solve(B.transpose());
	}

	/**
	 * Matrix inverse or pseudoinverse
	 * 
	 * @return inverse(A) if A is square, pseudoinverse otherwise.
	 */

	public Matrix<T> inverse() {
		return solve(identity(m, m, getField()));
	}

	/**
	 * Matrix determinant
	 * 
	 * @return determinant
	 */

	public T det() {
		return new LUDecomposition<T>(this).det();
	}

	// /**
	// * Matrix rank
	// *
	// * @return effective numerical rank, obtained from SVD.
	// */
	//
	// public int rank() {
	// return new SingularValueDecomposition(this).rank();
	// }

	// /**
	// * Matrix condition (2 norm)
	// *
	// * @return ratio of largest to smallest singular value.
	// */
	//
	// public Rational cond() {
	// return new SingularValueDecomposition(this).cond();
	// }

	/**
	 * Matrix trace.
	 * 
	 * @return sum of the diagonal elements.
	 */

	public T trace() {
		T t = getField().getZero();
		for (int i = 0; i < Math.min(m, n); i++) {
			t = t.add(get(i, i));
		}
		return t;
	}

	// /** Generate matrix with random elements
	// @param m Number of rows.
	// @param n Number of colums.
	// @return An m-by-n matrix with uniformly distributed random elements.
	// */
	//
	// public static Matrix random (int m, int n) {
	// Matrix A = new Matrix(m,n);
	// Rational[][] X = A.getArray();
	// for (int i = 0; i < m; i++) {
	// for (int j = 0; j < n; j++) {
	// X[i][j] = Math.random();
	// }
	// }
	// return A;
	// }

	/**
	 * Generate identity matrix
	 * 
	 * @param m
	 *            Number of rows.
	 * @param n
	 *            Number of colums.
	 * @return An m-by-n matrix with ones on the diagonal and zeros elsewhere.
	 */

	public static <T extends FieldValue<T> & Comparable<T> & Serializable> Matrix<T> identity(int m, int n, Field<T> field) {
		final Matrix<T> A = new Matrix<T>(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A.set(i, j, i == j ? field.getOne() : field.getZero());
			}
		}
		return A;
	}

	/**
	 * Print the matrix to stdout. Line the elements up in columns with a Fortran-like 'Fw.d' style format.
	 * 
	 * @param w
	 *            Column width.
	 * @param d
	 *            Number of digits after the decimal.
	 */

	public void print(int w, int d) {
		print(new PrintWriter(System.out, true), w, d);
	}

	/**
	 * Print the matrix to the output stream. Line the elements up in columns with a Fortran-like 'Fw.d' style format.
	 * 
	 * @param output
	 *            Output stream.
	 * @param w
	 *            Column width.
	 * @param d
	 *            Number of digits after the decimal.
	 */

	public void print(PrintWriter output, int w, int d) {
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		format.setMinimumIntegerDigits(1);
		format.setMaximumFractionDigits(d);
		format.setMinimumFractionDigits(d);
		format.setGroupingUsed(false);
		print(output, format, w + 2);
	}

	/**
	 * Print the matrix to stdout. Line the elements up in columns. Use the format object, and right justify within columns of width
	 * characters. Note that is the matrix is to be read back in, you probably will want to use a NumberFormat that is set to US Locale.
	 * 
	 * @param format
	 *            A Formatting object for individual elements.
	 * @param width
	 *            Field width for each column.
	 * @see java.text.DecimalFormat#setDecimalFormatSymbols
	 */

	public void print(NumberFormat format, int width) {
		print(new PrintWriter(System.out, true), format, width);
	}

	// DecimalFormat is a little disappointing coming from Fortran or C's
	// printf.
	// Since it doesn't pad on the left, the elements will come out different
	// widths. Consequently, we'll pass the desired column width in as an
	// argument and do the extra padding ourselves.

	/**
	 * Print the matrix to the output stream. Line the elements up in columns. Use the format object, and right justify within columns of
	 * width characters. Note that is the matrix is to be read back in, you probably will want to use a NumberFormat that is set to US
	 * Locale.
	 * 
	 * @param output
	 *            the output stream.
	 * @param format
	 *            A formatting object to format the matrix elements
	 * @param width
	 *            Column width.
	 * @see java.text.DecimalFormat#setDecimalFormatSymbols
	 */

	public void print(PrintWriter output, NumberFormat format, int width) {
		output.println(); // start on new line.
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				String s = format.format(get(i, j)); // format the number
				int padding = Math.max(1, width - s.length()); // At _least_ 1
				// space
				for (int k = 0; k < padding; k++)
					output.print(' ');
				output.print(s);
			}
			output.println();
		}
		output.println(); // end with blank line.
	}

	// /**
	// * Read a matrix from a stream. The format is the same the print method, so
	// * printed matrices can be read back in (provided they were printed using US
	// * Locale). Elements are separated by whitespace, all the elements for each
	// * row appear on a single line, the last row is followed by a blank line.
	// *
	// * @param input
	// * the input stream.
	// */
	//
	// public static Matrix read(BufferedReader input) throws java.io.IOException {
	// StreamTokenizer tokenizer = new StreamTokenizer(input);
	//
	// // Although StreamTokenizer will parse numbers, it doesn't recognize
	// // scientific notation (E or D); however, Double.valueOf does.
	// // The strategy here is to disable StreamTokenizer's number parsing.
	// // We'll only get whitespace delimited words, EOL's and EOF's.
	// // These words should all be numbers, for Double.valueOf to parse.
	//
	// tokenizer.resetSyntax();
	// tokenizer.wordChars(0, 255);
	// tokenizer.whitespaceChars(0, ' ');
	// tokenizer.eolIsSignificant(true);
	// java.util.Vector v = new java.util.Vector();
	//
	// // Ignore initial empty lines
	// while (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
	// // Do nothing.
	// }
	// if (tokenizer.ttype == StreamTokenizer.TT_EOF)
	// throw new java.io.IOException("Unexpected EOF on matrix read.");
	// do {
	// v.addElement(Double.valueOf(tokenizer.sval)); // Read & store 1st
	// // row.
	// } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
	//
	// int n = v.size(); // Now we've got the number of columns!
	// double row[] = new double[n];
	// for (int j = 0; j < n; j++)
	// // extract the elements of the 1st row.
	// row[j] = ((Double) v.elementAt(j)).doubleValue();
	// v.removeAllElements();
	// v.addElement(row); // Start storing rows instead of columns.
	// while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
	// // While non-empty lines
	// v.addElement(row = new double[n]);
	// int j = 0;
	// do {
	// if (j >= n)
	// throw new java.io.IOException("Row " + v.size() + " is too long.");
	// row[j++] = Double.valueOf(tokenizer.sval).doubleValue();
	// } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
	// if (j < n)
	// throw new java.io.IOException("Row " + v.size() + " is too short.");
	// }
	// int m = v.size(); // Now we've got the number of rows.
	// double[][] A = new double[m][];
	// v.copyInto(A); // copy the rows out of the vector
	// return new Matrix(A);
	// }

	/*
	 * ------------------------ Private Methods ------------------------
	 */

	/** Check if size(A) == size(B) **/

	private void checkMatrixDimensions(Matrix<T> B) {
		if (B.m != m || B.n != n) {
			throw new IllegalArgumentException("Matrix dimensions must agree.");
		}
	}

	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < m; i++) {
			if (i == 0) {
				buffer.append("/ ");
			} else if (i == m - 1) {
				buffer.append("\\ ");
			} else {
				buffer.append("| ");
			}
			for (int j = 0; j < n; j++) {
				final T e = get(i, j);
				final String value = String.valueOf(e);
				for (int k = 0; k < 8 - value.length(); k++) {
					buffer.append(" ");
				}
				buffer.append(value);
				buffer.append(" ");
			}
			if (i == 0) {
				buffer.append("\\");
			} else if (i == m - 1) {
				buffer.append("/");
			} else {
				buffer.append("|");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + m;
		result = prime * result + n;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				final T e = get(i, j);
				result = prime * result + (e == null ? 0 : e.hashCode());
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix<?> other = (Matrix<?>) obj;
		if (m != other.m)
			return false;
		if (n != other.n)
			return false;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				final Object e1 = get(i, j);
				final Object e2 = other.get(i, j);
				if (e1 == null) {
					if (e2 != null) {
						return false;
					}
				} else {
					if (!e1.equals(e2)) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
