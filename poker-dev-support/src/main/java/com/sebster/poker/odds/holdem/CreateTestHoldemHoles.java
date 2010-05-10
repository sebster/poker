package com.sebster.poker.odds.holdem;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.sebster.poker.Deck;
import com.sebster.poker.Hole;

public class CreateTestHoldemHoles {

	public static void main(String[] args) throws IOException {

		final int numHoles = 10;
		final int numRounds = 1000;
		
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("/tmp/holdem_test_data.dat"));
		
		dos.writeInt(numHoles);
		dos.writeInt(numRounds);
		
		final Random random = new Random(0);
		final Deck deck = new Deck(random);
		for (int i = 0; i < numRounds; i++) {
			final Hole[] holes = new Hole[numHoles];
			final int[] holeIndexes = new int[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = Hole.fromDeck(deck);
				holeIndexes[j] = holes[j].getIndex();
				dos.writeInt(holeIndexes[j]);
			}
			deck.shuffle();
			
		}
		
		dos.close();

	}
}
