package com.sebster.poker.holdem;

import com.sebster.poker.Rank;
import com.sebster.poker.odds.Constants;
import com.sebster.util.LinearOrder;

public enum HoleCategory implements LinearOrder<HoleCategory> {

	p22, s32, s42, s52, s62, s72, s82, s92, sT2, sJ2, sQ2, sK2, sA2,
	o32, p33, s43, s53, s63, s73, s83, s93, sT3, sJ3, sQ3, sK3, sA3,
	o42, o43, p44, s54, s64, s74, s84, s94, sT4, sJ4, sQ4, sK4, sA4,
	o52, o53, o54, p55, s65, s75, s85, s95, sT5, sJ5, sQ5, sK5, sA5,
	o62, o63, o64, o65, p66, s76, s86, s96, sT6, sJ6, sQ6, sK6, sA6,
	o72, o73, o74, o75, o76, p77, s87, s97, sT7, sJ7, sQ7, sK7, sA7,
	o82, o83, o84, o85, o86, o87, p88, s98, sT8, sJ8, sQ8, sK8, sA8,
	o92, o93, o94, o95, o96, o97, o98, p99, sT9, sJ9, sQ9, sK9, sA9,
	oT2, oT3, oT4, oT5, oT6, oT7, oT8, oT9, pTT, sJT, sQT, sKT, sAT,
	oJ2, oJ3, oJ4, oJ5, oJ6, oJ7, oJ8, oJ9, oJT, pJJ, sQJ, sKJ, sAJ,
	oQ2, oQ3, oQ4, oQ5, oQ6, oQ7, oQ8, oQ9, oQT, oQJ, pQQ, sKQ, sAQ,
	oK2, oK3, oK4, oK5, oK6, oK7, oK8, oK9, oKT, oKJ, oKQ, pKK, sAK,
	oA2, oA3, oA4, oA5, oA6, oA7, oA8, oA9, oAT, oAJ, oAQ, oAK, pAA;

	public boolean isSuited() {
		return ordinal() / 13 < ordinal() % 13;
	}

	public boolean isPair() {
		return ordinal() / 13 == ordinal() % 13;
	}

	public Rank getHighRank() {
		return Rank.byValue((isSuited() ? ordinal() % 13 : ordinal() / 13) + 2);
	}

	public Rank getLowRank() {
		return Rank.byValue((isSuited() ? ordinal() / 13 : ordinal() % 13) + 2);
	}

	public int getSize() {
		return isSuited() ? 4 : (isPair() ? 6 : 12);
	}

	public double getProbability() {
		return ((double) getSize()) / Constants.HOLE_COUNT;
	}

	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		final String name = name();
		buffer.append(name.charAt(1));
		buffer.append(name.charAt(2));
		final char type = name.charAt(0);
		if (type != 'p') {
			buffer.append(type);
		}
		return buffer.toString();
	}

	/**
	 * Get the first hole category, which is a pair of dueces.
	 * 
	 * @return the first hole category
	 */
	public HoleCategory first() {
		return values()[0];
	}

	/**
	 * Get the last hole category, which is a pair of aces.
	 * 
	 * @return the last hole category
	 */
	public HoleCategory last() {
		return values()[values().length - 1];
	}

	/**
	 * Get the next hole category, or null if this is the last hole category.
	 * 
	 * @return the next hole category or null if this is the last hole category
	 */
	public HoleCategory next() {
		final int i = ordinal() + 1;
		if (i < values().length) {
			return values()[i];
		}
		return null;
	}

	/**
	 * Get the previous hole category, or null if this is the previous hole
	 * category.
	 * 
	 * @return the previous hole category or null if this is the first hole
	 *         category
	 */
	public HoleCategory prev() {
		final int i = ordinal() - 1;
		if (i >= 0) {
			return values()[i];
		}
		return null;
	}

	public static HoleCategory fromString(final String string) {
		final int[] category = parseCategory(string);
		if (category[0] != category[1] && category[2] == 0) {
			throw new IllegalArgumentException("invalid hole: " + string + "; missing suit specifier");
		}
		return byDescription(Rank.byValue(category[0]), Rank.byValue(category[1]), category[2] == 1);
	}

	public static HoleCategory byDescription(final Rank rank1, final Rank rank2, final boolean suited) {
		int r1 = rank1.ordinal(), r2 = rank2.ordinal();
		if (r1 < r2) {
			final int tmp = r1;
			r1 = r2;
			r2 = tmp;
		}
		return values()[suited ? r2 * 13 + r1 : r1 * 13 + r2];
	}

	static int[] parseCategory(final String category) {
		if (category == null) {
			throw new IllegalArgumentException("invalid hole: null");
		}
		if (category.length() < 2 || category.length() > 3) {
			throw new IllegalArgumentException("invalid hole: " + category + "; must be 2 or 3 characters long");
		}
		final int[] result = new int[3];
		try {
			result[0] = Rank.byName(category.charAt(0)).getValue();
			result[1] = Rank.byName(category.charAt(1)).getValue();
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException("invalid hole: " + category + "; " + e.getMessage());
		}
		if (category.length() == 3) {
			final char type = Character.toLowerCase(category.charAt(2));
			if (type != 'o' && type != 's') {
				throw new IllegalArgumentException("invalid hole: " + category + "; suit specifier must be 's' or 'o'");
			}
			if (result[0] == result[1]) {
				throw new IllegalArgumentException("invalid hole: " + category + "; pairs may not have a suit specifier");
			}
			result[2] = type == 's' ? 1 : 2;
		}
		return result;
	}

}
