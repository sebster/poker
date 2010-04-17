package com.sebster.poker;

public class GenerateIndexTable {

	public static void main(String[] args) {
		System.out.println("{");
		for (int deckSize = 51; deckSize >= 0; deckSize--) {
			System.out.println("  {");
			for (int length = 1; length <= 8; length++) {
				int i = 0;
				System.out.print("    { 0, ");
				// the first card is x, what is the start index given length
				// cards total
				for (int first = 0; first <= deckSize - length; first++) {
					int k = combinations(deckSize - first, length - 1);
					i += k;
					System.out.print(i + ", ");
				}
				System.out.println("}, ");
			}
			System.out.println("  },");
		}
		System.out.println("}");
	}

	public static int combinations(int n, int m) {
		int result = 1;
		for (int i = 1; i <= m; i++) {
			result = result * (n - m + i) / i;
		}
		return result;
	}

}
