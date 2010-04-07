package com.sebster.poker.odds;

public class Constants {

	private Constants() {
		// No instantiation.
	}

	public static final int BOARD_COUNT_52 = 52 * 51 * 50 * 49 * 48 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_50 = 50 * 49 * 48 * 47 * 46 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_48 = 48 * 47 * 46 * 45 * 44 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_46 = 46 * 45 * 44 * 43 * 42 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_44 = 44 * 43 * 42 * 41 * 40 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_42 = 42 * 41 * 40 * 39 * 38 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_40 = 40 * 39 * 38 * 37 * 36 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_38 = 38 * 37 * 36 * 35 * 34 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_36 = 36 * 35 * 34 * 33 * 32 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_34 = 34 * 33 * 32 * 31 * 30 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_32 = 32 * 31 * 30 * 29 * 28 / 5 / 4 / 3 / 2 / 1;
	public static final int BOARD_COUNT_28 = 28 * 27 * 26 * 25 * 24 / 5 / 4 / 3 / 2 / 1;

	private static final int[] HOLE2_BOARD_COUNTS = new int[] {
			BOARD_COUNT_52,
			BOARD_COUNT_50,
			BOARD_COUNT_48,
			BOARD_COUNT_46,
			BOARD_COUNT_44,
			BOARD_COUNT_42,
			BOARD_COUNT_40,
			BOARD_COUNT_38,
			BOARD_COUNT_36,
			BOARD_COUNT_34,
			BOARD_COUNT_32
			};
	
	private static final int[] HOLE4_BOARD_COUNTS = new int[] {
		BOARD_COUNT_52,
		BOARD_COUNT_48,
		BOARD_COUNT_44,
		BOARD_COUNT_40,
		BOARD_COUNT_36,
		BOARD_COUNT_32,
		BOARD_COUNT_28
	};

	public static final int HOLE_COUNT = 52 * 51 / 2;

	public static final int HOLE_CATEGORY_COUNT = 13 * 13;
	
	public static final int HOLE4_COUNT = 52 * 51 * 50 * 49 / 4 / 3 / 2 / 1;

	public static int getHole2BoardCount(final int numPlayers) {
		return HOLE2_BOARD_COUNTS[numPlayers];
	}

	public static int getHole4BoardCount(final int numPlayers) {
		return HOLE4_BOARD_COUNTS[numPlayers];
	}

}
