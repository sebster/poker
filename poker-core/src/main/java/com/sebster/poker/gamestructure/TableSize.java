package com.sebster.poker.gamestructure;

public enum TableSize {

	HEADS_UP(2),

	SIX_HANDED(6),

	FULL9(9),

	FULL10(10);

	private final int seats;

	private TableSize(final int seats) {
		this.seats = seats;
	}

	public int getSeats() {
		return seats;
	}

}
