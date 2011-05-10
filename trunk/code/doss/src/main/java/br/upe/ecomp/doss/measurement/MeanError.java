package br.upe.ecomp.doss.measurement;

import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * This is the average of the error measurements over all iterations performed so far.
 * 
 * @author George Moraes
 * 
 */
public class MeanError extends Measurement {

    private double sumError;
    private double mean;

    @Override
    public void init() {
        sumError = 0;
        mean = 0;
    }

    @Override
    public void update(Algorithm algorithm) {
        sumError += algorithm.getProblem().getOptimumValue() - algorithm.getBestSolutionValue();
        mean = sumError / algorithm.getIterations();
    }

    @Override
    public double getValue() {
        return mean;
    }

    @Override
    public String getName() {
        return "Mean Error";
    }

    @Override
    public String getDescription() {
        return "This is the average of the error measurements over all iterations performed so far.";
    }

}
