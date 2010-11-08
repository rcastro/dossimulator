package br.upe.ecomp.doss.problem.df1;

import br.upe.ecomp.doss.util.RandomUtils;

public class LogisticFunction {

	private int hAddSignal;
	private int rAddSignal;
	private int[] xAddSignal;
	private int dimensions;

	public LogisticFunction(int dimensions) {
		this.hAddSignal = RandomUtils.generateNumberSignal();
		this.rAddSignal = RandomUtils.generateNumberSignal();
		this.xAddSignal = new int[dimensions];
		for (int i = 0; i < xAddSignal.length; i++) {
			xAddSignal[i] = RandomUtils.generateNumberSignal();
		}
		this.dimensions = dimensions;
	}

	/**
	 * Retirado de: A test problem generator for non-stationary environments
	 * (Morrison, R.W. De Jong, K.a.) - 2009
	 * 
	 * @param xOld
	 * @param hBase
	 * @param hRange
	 * @param xScale
	 * @param iteration
	 * @return
	 */
	// TODO duplicacao de codigo: remover
	public double generateDynamicH(double xOld, double hBase, double hRange, double xScale,
			int iteration, double a) {
		double result = 0;
		double addNumber = evaluate(iteration, a) * xScale * hRange;

		result = xOld + addNumber * hAddSignal;
		if ((result < hBase)) {
			hAddSignal = 1;
		}
		if ((result > hBase + hRange)) {
			hAddSignal = -1;
		}
		result = xOld + addNumber * hAddSignal;

		return result;
	}

	// TODO duplicacao de codigo: remover
	public double generateDynamicR(double rOld, double rBase, double rRange, double rScale,
			int iteration, double a) {
		double result = 0;
		double addNumber = evaluate(iteration, a) * rScale * rRange;

		result = rOld + addNumber * rAddSignal;
		if ((result < rBase)) {
			rAddSignal = 1;
		}
		if ((result > rBase + rRange)) {
			rAddSignal = -1;
		}
		result = rOld + addNumber * rAddSignal;

		return result;
	}

	// TODO duplicacao de codigo: remover
	public double[] generateDynamicX(double[] xOld, double xBase, double xRange, double xScale,
			int iteration, double a) {
		// mesmo tamanho do array anterior, ou seja, o numero de dimensoes
		double[] result = new double[dimensions];
		double addNumber = evaluate(iteration, a) * xScale * xRange;

		for (int i = 0; i < dimensions; i++) {
			result[i] = xOld[i] + addNumber * xAddSignal[i];
			if ((result[i] < xBase)) {
				xAddSignal[i] = 1;
			}
			if ((result[i] > xBase + xRange)) {
				xAddSignal[i] = -1;
			}
			result[i] = xOld[i] + addNumber * xAddSignal[i];
		}
		return result;
	}

	private double evaluate(int iteration, double a) {
		double y = 0.0001;
		for (int i = 0; i < iteration; i++) {
			y = a * y * (1 - y);
		}
		return y;
	}

}
