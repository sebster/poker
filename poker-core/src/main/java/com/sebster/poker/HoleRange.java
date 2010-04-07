package com.sebster.poker;

import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;


public class HoleRange {

	private HoleRange() {
		// Utility class.
	}

	public static EnumSet<HoleCategory> pairs(final Rank bound1, final Rank bound2) {
		final EnumSet<HoleCategory> pairs = EnumSet.noneOf(HoleCategory.class);
		final int rank1 = bound2.getValue(), rank2 = bound1.getValue();
		final int start = Math.min(rank1, rank2), end = Math.max(rank1, rank2);
		for (int i = start; i <= end; i++) {
			final Rank rank = Rank.byValue(i);
			pairs.add(HoleCategory.byDescription(rank, rank, false));
		}
		return pairs;
	}

	public static EnumSet<HoleCategory> range(final Rank first, final Rank secondBound1, final Rank secondBound2) {
		return range(first, secondBound1, first, secondBound2);
	}

	public static EnumSet<HoleCategory> range(final Rank firstBound1, final Rank secondBound1, final Rank firstBound2, final Rank secondBound2) {
		final EnumSet<HoleCategory> range = range(firstBound1, secondBound1, firstBound2, secondBound2, true);
		range.addAll(range(firstBound1, secondBound1, firstBound2, secondBound2, false));
		return range;
	}

	public static EnumSet<HoleCategory> range(final Rank first, final Rank secondBound1, final Rank secondBound2, final boolean suited) {
		return range(first, secondBound1, first, secondBound2, suited);
	}

	public static EnumSet<HoleCategory> range(final Rank firstBound1, final Rank secondBound1, final Rank firstBound2, final Rank secondBound2, final boolean suited) {
		final int firstRank1 = firstBound1.getValue(), firstRank2 = firstBound2.getValue();
		final int secondRank1 = secondBound1.getValue(), secondRank2 = secondBound2.getValue();
		final int low1 = Math.min(firstRank1, firstRank2), high1 = Math.max(firstRank1, firstRank2);
		final int low2 = Math.min(secondRank1, secondRank2), high2 = Math.max(secondRank1, secondRank2);
		final int delta1 = high1 - low1, delta2 = high2 - low2;
		if (delta1 == 0 && delta2 == 0) {
			return EnumSet.of(HoleCategory.byDescription(firstBound1, secondBound1, suited));
		}
		if (delta1 != 0 && delta2 != 0 && delta1 != delta2) {
			throw new IllegalArgumentException("invalid range");
		}
		final EnumSet<HoleCategory> range = EnumSet.noneOf(HoleCategory.class);
		int i = low1, j = low2;
		do {
			range.add(HoleCategory.byDescription(Rank.byValue(i), Rank.byValue(j), suited));
			if (delta1 > 0)
				i++;
			if (delta2 > 0)
				j++;
		} while (i <= high1 && j <= high2);
		return range;
	}

	/**
	 * Parse a hand range specification. This consists of a list of 1 or more
	 * parts separated by a comma. Each part can be either:
	 * 
	 * <ol>
	 * <li>a single hole category (AJs, KK)
	 * <li>a range of hole categories (AJs-AKs, KQo-T9o, AK-AT, 22-55)
	 * <li>a single hole category and a plus sign, e.g.:
	 * <ol>
	 * <li>ATo+, shorthand for ATo-AKo (the second card goes up to but not
	 * including the first)
	 * <li>33+, shorthand for 33-AA
	 * </ol>
	 * <li>a *, shorthand for all hole categories
	 * <li>a - followed by any of the above; meaning that the specified hole
	 * categories should be removed from the hole categories so far, e.g.,
	 * 33+,-KK means all pairs 3 and up except kings.
	 * </ol>
	 */
	public static EnumSet<HoleCategory> fromString(final String string) {
		final String[] ranges = string.split(",");
		final EnumSet<HoleCategory> result = EnumSet.noneOf(HoleCategory.class);
		for (int i = 0; i < ranges.length; i++) {
			String range = ranges[i].trim();
			boolean remove = false;
			final EnumSet<HoleCategory> partResult;
			if (range.charAt(0) == '-') {
				range = range.substring(1);
				remove = true;
			}
			final int j = range.indexOf('-');
			if (j > 0) {
				final int[] category1 = HoleCategory.parseCategory(range.substring(0, j));
				final int[] category2 = HoleCategory.parseCategory(range.substring(j + 1));
				if (category1[2] != category2[2]) {
					throw new IllegalArgumentException("invalid range: " + range + "; both bounds must have the same suit specifier");
				}
				if (category1[0] == category1[1]) {
					// Pair range.
					if (category2[0] != category2[1]) {
						throw new IllegalArgumentException("invalid range: " + range + "; second bound must be a pair");
					}
					partResult = pairs(Rank.byValue(category1[0]), Rank.byValue(category2[0]));
				} else {
					// Non-pair range.
					if (category2[0] == category2[1]) {
						throw new IllegalArgumentException("invalid range: " + range + "; second bound must not be a pair");
					}
					if (category1[0] == 0) {
						partResult = range(Rank.byValue(category1[0]), Rank.byValue(category1[1]), Rank.byValue(category2[0]), Rank.byValue(category2[1]));
					} else {
						partResult = range(Rank.byValue(category1[0]), Rank.byValue(category1[1]), Rank.byValue(category2[0]), Rank.byValue(category2[1]), category1[2] == 1);
					}
				}
			} else if (range.charAt(range.length() - 1) == '+') {
				final int[] category = HoleCategory.parseCategory(range.substring(0, range.length() - 1));
				if (category[0] == category[1]) {
					// Pair range.
					partResult = pairs(Rank.byValue(category[0]), Rank.ACE);
				} else if (category[0] > category[1]) {
					// Non-pair range.
					if (category[2] == 0) {
						partResult = range(Rank.byValue(category[0]), Rank.byValue(category[1]), Rank.byValue(category[0] - 1));
					} else {
						partResult = range(Rank.byValue(category[0]), Rank.byValue(category[1]), Rank.byValue(category[0] - 1), category[2] == 1);
					}
				} else {
					throw new IllegalArgumentException("invalid range: " + range + "; first card rank must be higher than second card rank");
				}
			} else if ("*".equals(range)) {
				// All categories.
				partResult = EnumSet.allOf(HoleCategory.class);
			} else {
				// Single category.
				final int[] category = HoleCategory.parseCategory(range);
				partResult = EnumSet.noneOf(HoleCategory.class);
				if (category[2] != 2) {
					partResult.add(HoleCategory.byDescription(Rank.byValue(category[0]), Rank.byValue(category[1]), true));
				}
				if (category[2] != 1) {
					partResult.add(HoleCategory.byDescription(Rank.byValue(category[0]), Rank.byValue(category[1]), false));
				}
			}
			if (remove) {
				result.removeAll(partResult);
			} else {
				result.addAll(partResult);
			}

		}
		return result;
	}

	public static String toString(final EnumSet<HoleCategory> range) {
		final EnumSet<HoleCategory> copy = EnumSet.copyOf(range);
		final Set<RangeSpec> parts = new TreeSet<RangeSpec>();
		while (!copy.isEmpty()) {
			final HoleCategory category = copy.iterator().next();
			copy.remove(category);
			if (category.isPair()) {
				final Rank first = category.getHighRank();
				Rank last = first;
				do {
					last = last.next();
				} while (last != null && copy.remove(HoleCategory.byDescription(last, last, false)));
				parts.add(new RangeSpec(null, first, last != null ? last.prev() : Rank.ACE, 0));
			} else {
				final Rank first = category.getHighRank(), second = category.getLowRank();
				final boolean suited = category.isSuited();
				boolean offsuitToo = suited;
				Rank last = second;
				do {
					if (offsuitToo) {
						offsuitToo = copy.contains(HoleCategory.byDescription(first, last, false));
					}
					last = last.next();
				} while (last != first && copy.remove(HoleCategory.byDescription(first, last, suited)));
				if (offsuitToo) {
					offsuitToo = last == first || !copy.contains(HoleCategory.byDescription(first, last, false));
					for (Rank rank = second; rank != last; rank = rank.next()) {
						copy.remove(HoleCategory.byDescription(first, rank, false));
					}
				}
				parts.add(new RangeSpec(first, second, (last != null ? last : first).prev(), suited ? (offsuitToo ? 0 : 1) : 2));
			}
		}
		// Create string result of sorted parts.
		final StringBuilder result = new StringBuilder();
		if (parts.size() > 0) {
			for (final RangeSpec part : parts) {
				result.append(part);
				result.append(',');
			}
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}

	private static class RangeSpec implements Comparable<RangeSpec> {

		private Rank first; // null = pair, non-null = XY[so]-XZ[so]
		private Rank secondLow;
		private Rank secondHigh;
		private int type; // (0 = both, 1 = suited, 2 = offsuit)

		public RangeSpec(final Rank first, final Rank secondLow, final Rank secondHigh, final int type) {
			this.first = first;
			this.secondLow = secondLow;
			this.secondHigh = secondHigh;
			this.type = type;
		}

		@Override
		public int compareTo(com.sebster.poker.HoleRange.RangeSpec o) {
			final int i = first != null ? (o.first != null ? o.first.compareTo(first) : 1) : (o.first != null ? -1 : 0);
			if (i == 0) {
				final int j = secondLow.compareTo(o.secondLow);
				if (j == 0) {
					return type - o.type;
				}
				return j;
			}
			return i;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append(first != null ? first : secondLow);
			buffer.append(secondLow.toString());
			if (type > 0) {
				buffer.append(type == 1 ? 's' : 'o');
			}
			if (secondHigh != secondLow) {
				if (secondHigh == (first != null ? first.prev() : Rank.ACE)) {
					buffer.append('+');
				} else {
					buffer.append('-');
					buffer.append(first != null ? first : secondHigh);
					buffer.append(secondHigh);
					if (type > 0) {
						buffer.append(type == 1 ? 's' : 'o');
					}
				}
			}
			return buffer.toString();
		}
	}

}
