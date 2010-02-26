package com.sebster.poker.solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.sebster.poker.holdem.HoleCategory;

public class NESolver {

	public static void main(String[] args) throws IOException {

		Set<EnumSet<HoleCategory>> seenCallRanges = new HashSet<EnumSet<HoleCategory>>();
		Set<EnumSet<HoleCategory>> seenPushRanges = new HashSet<EnumSet<HoleCategory>>();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		EnumSet<HoleCategory> callRange = EnumSet.allOf(HoleCategory.class);
		EnumSet<HoleCategory> pushRange = null;

		seenCallRanges.add(callRange);

		while (true) {
			EnumSet<HoleCategory> newPushRange = Solver.optimalSBStrategy(callRange, 1000, 100);
			EnumSet<HoleCategory> newCallRange = Solver.optimalBBStrategy(newPushRange, 1000, 100);
			System.out.println("newPushRange=" + newPushRange);
			System.out.println("newCallRange=" + newCallRange);
			in.readLine();
			if (newPushRange.equals(pushRange) && newCallRange.equals(callRange)) {
				break;
			}
//			if (seenCallRanges.contains(newCallRange) && seenPushRanges.contains(newPushRange)) {
//				System.out.println("loop detected");
//				break;
//			}
//			seenCallRanges.add(callRange);
//			seenPushRanges.add(newPushRange);
			pushRange = newPushRange;
			callRange = newCallRange;
		}
	}
}
