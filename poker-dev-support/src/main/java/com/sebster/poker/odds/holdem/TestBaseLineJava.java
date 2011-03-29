package com.sebster.poker.odds.holdem;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.sebster.poker.Hole;
import com.sebster.poker.holdem.odds.FastHoldemPreflopOddsCalculator;
import com.sebster.poker.holdem.odds.HoldemPreflopOddsCalculator;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Odds;

public class TestBaseLineJava {

	public static void main(String[] args) throws IOException {

		DataInputStream dis = new DataInputStream(new FileInputStream(args[0]));

		CompressedHandValueDatabase db = new CompressedHandValueDatabase(new GZIPInputStream(new FileInputStream(args[1])));
		HoldemPreflopOddsCalculator preflopOddsCalculator = new FastHoldemPreflopOddsCalculator(db);
		final int numHoles = dis.readInt();
		int numRounds = dis.readInt();
		numRounds = 120;

		Hole[] holes = new Hole[numHoles];
		int t = 0;
		for (int i = 0; i < numRounds; i++) {
			for (int j = 0; j < numHoles; j++) {
				holes[j] = Hole.fromIndex(dis.readInt());
			}
			final long t1 = System.currentTimeMillis();
			Odds[] odds = preflopOddsCalculator.calculateOdds(holes);
			final long t2 = System.currentTimeMillis();
			
			System.out.print("odds[" + i + "]=[");
			for (int k = 0; k <= 10; k++) {
				System.out.print(" " + odds[0].getNWaySplits(k));
			}
			System.out.println(" ] time=" + (t2 - t1));
			if (i > 20) {
				t += (int) (t2 - t1);
			}
		}
		System.out.println("average=" + (t / (numRounds - 20.0)));
	}
}
