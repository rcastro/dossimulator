package br.upe.ecomp.doss.measurement;

import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.Algorithm;

public class DistanceToOptimum extends Measurement {

    private double distanceToOptimum;

    /**
     * Default constructor.
     */
    public DistanceToOptimum() {
        init();
    }

    @Override
    public void init() {
        distanceToOptimum = Double.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Distance to optimum";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "This is the euclidean distance between the position of the problem optimum and the best "
                + "position found at current iteration.";
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        distanceToOptimum = MathUtils
                .distance(algorithm.getProblem().getOptimumPosition(), algorithm.getBestSolution());
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        return distanceToOptimum;
    }

}
