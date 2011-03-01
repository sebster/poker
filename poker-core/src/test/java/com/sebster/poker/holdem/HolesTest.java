package com.sebster.poker.holdem;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Hole;
import com.sebster.poker.Holes;
import com.sebster.poker.IndexedHole;
import com.sebster.util.arrays.ShortArrayWrapper;
import com.sebster.util.collections.ImmutablePair;
import com.sebster.util.collections.Pair;

public class HolesTest {

	private static final Logger LOG = LoggerFactory.getLogger(HolesTest.class);

	@Test
	public void testNormalize() {
		Assert.assertArrayEquals(
				new IndexedHole[] { new IndexedHole(Hole.fromString("2c,2d"), 0), new IndexedHole(Hole.fromString("2h,2s"), 1) },
				Holes.normalize(Hole.fromString("2c,2h"), Hole.fromString("2d,2s")));
		Assert.assertArrayEquals(
				new IndexedHole[] { new IndexedHole(Hole.fromString("2c,2d"), 1), new IndexedHole(Hole.fromString("2h,3c"), 0) },
				Holes.normalize(Hole.fromString("2c,3d"), Hole.fromString("2s,2d")));
	}

	@Test
	public void testNumberOf2PlayerNormalizedHoleMatchups() {
		final Set<Pair<Hole, Hole>> holeMatchups = new HashSet<Pair<Hole, Hole>>();
		Hole hole1 = Hole.firstHole();
		while (hole1 != null) {
			Hole hole2 = hole1.next();
			while (hole2 != null) {
				if (!hole1.intersects(hole2)) {
					final IndexedHole[] holeMatchup = Holes.normalize(hole1, hole2);
					holeMatchups.add(new ImmutablePair<Hole, Hole>(holeMatchup[0].getHole(), holeMatchup[1].getHole()));
				}
				hole2 = hole2.next();
			}
			hole1 = hole1.next();
		}
		Assert.assertEquals(47164, holeMatchups.size());
	}

	@Test
	@Ignore("takes very long and requires lots of memory")
	public void testNumberOf3PlayerNormalizedHoleMatchups() {
		final Set<ShortArrayWrapper> holeMatchups = new HashSet<ShortArrayWrapper>();
		Hole hole1 = Hole.firstHole();
		while (hole1 != null) {
			Hole hole2 = hole1.next();
			while (hole2 != null) {
				LOG.debug("hole1: " + hole1 + " hole2: " + hole2 + " size: " + holeMatchups.size());
				if (!hole1.intersects(hole2)) {
					Hole hole3 = hole2.next();
					while (hole3 != null) {
						if (!hole1.intersects(hole3) && !hole2.intersects(hole3)) {
							final IndexedHole[] holeMatchup = Holes.normalize(hole1, hole2, hole3);
							holeMatchups.add(new ShortArrayWrapper(new short[] {
									(short) holeMatchup[0].getHole().getIndex(),
									(short) holeMatchup[1].getHole().getIndex(),
									(short) holeMatchup[2].getHole().getIndex()
							}));
						}
						hole3 = hole3.next();
					}
				}
				hole2 = hole2.next();
			}
			hole1 = hole1.next();
		}
		Assert.assertEquals(14025414, holeMatchups.size());
	}
}
