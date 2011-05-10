package br.upe.ecomp.doss.measurement;

import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * Measurement that computes the swarm diversity.
 * 
 * @author George Moraes
 * 
 */

public class Diversity extends Measurement {

    private double diversity;

    @Override
    public void init() {
        this.diversity = 0;
    }

    @Override
    public void update(Algorithm algorithm) {
        diversity = 0;
        double[] center = getSwarmCenter(algorithm);
        double sumDistance = 0;
        for (int i = 0; i < algorithm.getParticles().length; i++) {
            // Sum of the distance between each particle and swarm center
            sumDistance += MathUtils.distance(center, algorithm.getParticles()[i].getCurrentPosition());
        }
        diversity = sumDistance / algorithm.getProblem().getDimensionsNumber();
    }

    @Override
    public double getValue() {
        return diversity;
    }

    @Override
    public String getName() {
        return "Diversity";
    }

    @Override
    public String getDescription() {
        return "Measurement that computes the swarm diversity.";
    }

    /**
     * Returns the swarm center.
     * 
     * @return The swarm center.
     */
    private double[] getSwarmCenter(Algorithm algorithm) {
        double[] result = new double[algorithm.getProblem().getDimensionsNumber()];
        for (int i = 0; i < algorithm.getParticles().length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[j] += algorithm.getParticles()[i].getCurrentPosition()[j];
            }
        }

        for (int i = 0; i < result.length; i++) {
            result[i] = result[i] / algorithm.getParticles().length;
        }

        return result;
    }

}
