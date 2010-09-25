package br.upe.ecomp.doss.util;

import java.util.Random;

public class RandomUtils {

	private static Random rand;

	private static void initRand() {
		if (rand == null) {
			rand = new Random(System.currentTimeMillis());
		}
	}

	public static double generateRandom() {
		initRand();
		double num = ((2 * rand.nextDouble()) - 1);
		return num;
	}

	public static double generateRandom(int limit) {
		initRand();
		double num = limit * ((2 * rand.nextDouble()) - 1);
		return num;
	}

	public static double generateUnsignedRandom(int limit) {
		initRand();
		double num = limit * rand.nextDouble();
		return num;
	}

	public static int generateNumberSignal() {
		initRand();
		if (rand.nextBoolean()) {
			return 1;
		} else {
			return -1;
		}
	}

}
