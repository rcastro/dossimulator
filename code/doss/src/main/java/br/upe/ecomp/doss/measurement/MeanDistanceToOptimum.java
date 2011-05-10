package br.upe.ecomp.doss.measurement;

import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.Algorithm;

public class MeanDistanceToOptimum extends Measurement {

    private double sumDistance;
    private double mean;

    /**
     * Default constructor.
     */
    public MeanDistanceToOptimum() {
        init();
    }

    @Override
    public void init() {
        sumDistance = 0;
        mean = 0;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Mean distance to optimum";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "This is the mean euclidean distance between the position of the problem optimum"
                + " and the global best optimum.";
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        sumDistance += MathUtils.distance(algorithm.getProblem().getOptimumPosition(), algorithm.getBestSolution());
        mean = sumDistance / algorithm.getIterations();
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        return mean;
    }

}
