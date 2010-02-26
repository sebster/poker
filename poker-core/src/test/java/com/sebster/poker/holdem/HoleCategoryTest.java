package com.sebster.poker.holdem;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.Rank;

public class HoleCategoryTest {

	@Test
	public void testStringConversions() {
		Assert.assertEquals("AJs", HoleCategory.sAJ.toString());
		Assert.assertEquals("QQ", HoleCategory.pQQ.toString());
		Assert.assertEquals("72o", HoleCategory.o72.toString());
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			Assert.assertEquals(holeCategory, HoleCategory.fromString(holeCategory.toString()));
		}
	}

	@Test
	public void testIsPair() {
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			Assert.assertEquals(holeCategory.isPair(), holeCategory.toString().charAt(0) == holeCategory.toString().charAt(1));
		}
	}

	@Test
	public void testIsSuited() {
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			Assert.assertEquals(holeCategory.isSuited(), holeCategory.toString().endsWith("s"));
		}
	}

	@Test
	public void testGetRank() {
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			Assert.assertEquals(Rank.byName(holeCategory.toString().charAt(0)), holeCategory.getHighRank());
			Assert.assertEquals(Rank.byName(holeCategory.toString().charAt(1)), holeCategory.getLowRank());
		}
	}

	@Test
	public void testByDescription() {
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			Assert.assertEquals(holeCategory, HoleCategory.byDescription(holeCategory.getHighRank(), holeCategory.getLowRank(), holeCategory.isSuited()));
			Assert.assertEquals(holeCategory, HoleCategory.byDescription(holeCategory.getLowRank(), holeCategory.getHighRank(), holeCategory.isSuited()));
		}
	}

}
