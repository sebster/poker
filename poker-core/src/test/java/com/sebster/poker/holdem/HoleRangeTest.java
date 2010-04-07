package com.sebster.poker.holdem;

import java.util.Arrays;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.HoleCategory;
import com.sebster.poker.HoleRange;
import com.sebster.poker.Rank;

import static com.sebster.poker.HoleCategory.*;

public class HoleRangeTest {

	@Test
	public void testRanges() {
		final EnumSet<HoleCategory> range = HoleRange.fromString("22+,-33-55,AJ+,-AQs-AKs,AQs,KQs");
		for (final Rank rank : Rank.values()) {
			final HoleCategory pair = HoleCategory.byDescription(rank, rank, false);
			Assert.assertEquals(range.contains(pair), rank.getValue() == 2 || rank.getValue() > 5);
		}
		final EnumSet<HoleCategory> expected = EnumSet.copyOf(Arrays.asList(new HoleCategory[] { p22, p66, p77, p88, p99, pTT, pJJ, pQQ, pKK, pAA, oAJ, oAQ, oAK, sAJ, sAQ, sKQ }));
		Assert.assertEquals(expected, range);
	}
	
	@Test
	public void testToString() {
		EnumSet<HoleCategory> range; 
		
		range = HoleRange.fromString("55-77,A3s+,K5o-K7o,A3o+,TT+,K3s+,QJ,J5o+");
		Assert.assertEquals("55-77,TT+,A3+,K3s+,K5o-K7o,QJ,J5o+", HoleRange.toString(range));

		range = HoleRange.fromString("22,A2-A5,KQs-T9s,55-77,A2s-AKs");
		Assert.assertEquals("22,55-77,A2s+,A2o-A5o,KQs,QJs,JTs,T9s", HoleRange.toString(range));
	}

}
