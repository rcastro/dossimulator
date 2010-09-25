package br.upe.ecomp.doss.util;

import java.util.ArrayList;
import java.util.List;

public class PointGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double begin = -3.0;
		double end = 3.0;
		double step = 0.4;
		double[] numbers = generatePoints(begin, end, step);
		System.out.println(numbers.toString());
	}

	public static double[] generatePoints(double begin, double end,
			double step) {
		List<Double> numbers = new ArrayList<Double>();
		while( (begin + step) < end) {
			numbers.add(begin);
			begin = begin + step;
		}
		numbers.add(end);
		double[] points = new double[numbers.size()];
		int pos = 0;
		for(Double num : numbers) {
			points[pos] = num;
			pos++;
		}
		return points;
	}

}
