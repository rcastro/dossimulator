package br.upe.ecomp.doss.measurement;

import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * Error measurement: The difference between the global maximum value and the global best fitness.
 * 
 * @author George Moraes
 * 
 */
public class ErrorMeasurement extends Measurement {

    private double error;

    @Override
    public void init() {
    }

    @Override
    public void update(Algorithm algorithm) {
        error = algorithm.getProblem().getOptimumValue() - algorithm.getBestSolutionValue();
    }

    @Override
    public double getValue() {
        return error;
    }

    @Override
    public String getName() {
        return "Error";
    }

    @Override
    public String getDescription() {
        return "The difference between the global maximum value and the global best fitness.";
    }

}
