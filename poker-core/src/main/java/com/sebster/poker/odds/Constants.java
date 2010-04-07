package com.sebster.poker.odds;

public class Constants {

	private Constants() {
		// No instantiation.
	}

	public static final int BOARD_COUNT_0 = 52 * 51 * 50 * 49 * 48 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_1 = 50 * 49 * 48 * 47 * 46 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_2 = 48 * 47 * 46 * 45 * 44 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_3 = 46 * 45 * 44 * 43 * 42 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_4 = 44 * 43 * 42 * 41 * 40 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_5 = 42 * 41 * 40 * 39 * 38 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_6 = 40 * 39 * 38 * 37 * 36 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_7 = 38 * 37 * 36 * 35 * 34 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_8 = 36 * 35 * 34 * 33 * 32 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_9 = 34 * 33 * 32 * 31 * 30 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_10 = 32 * 31 * 30 * 29 * 28 / 5 / 4 / 3 / 2 / 1;

	private static final int[] BOARD_COUNTS = new int[] {
			BOARD_COUNT_0,
			BOARD_COUNT_1,
			BOARD_COUNT_2,
			BOARD_COUNT_3,
			BOARD_COUNT_4,
			BOARD_COUNT_5,
			BOARD_COUNT_6,
			BOARD_COUNT_7,
			BOARD_COUNT_8,
			BOARD_COUNT_9,
			BOARD_COUNT_10
			};

	public static final int HOLE_COUNT = 52 * 51 / 2;

	public static final int HOLE_CATEGORY_COUNT = 13 * 13;

	public static int getBoardCount(final int numPlayers) {
		return BOARD_COUNTS[numPlayers];
	}

	;
}
