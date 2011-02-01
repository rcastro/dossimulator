/**
 * Copyright (C) 2010
 * Swarm Intelligence Team (SIT)
 * Department of Computer and Systems
 * University of Pernambuco
 * Brazil
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package br.upe.ecomp.doss.algorithm.volitivepso;

import org.apache.commons.math.random.MersenneTwister;

import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.problem.Problem;

/**
 * An specialization of the {@link PSOParticle} class for the WeightedPSO algorithm.
 * 
 * @author George Moraes
 */
public class WeightedPSOParticle extends PSOParticle {

    private double weight;
    private double[] previousPosition;
    private double deltaFitness;

    /**
     * Creates a new instance of this class.
     * 
     * @param dimensions The number of dimensions of the problem we are trying to solve.
     */
    public WeightedPSOParticle(int dimensions) {
        super(dimensions);
        deltaFitness = 0;
        previousPosition = new double[dimensions];
    }

    public void feed(double maxAbsDeltaFitness, double wScale) {
        weight = weight + (deltaFitness / maxAbsDeltaFitness);
        if (weight > wScale) {
            weight = wScale;
        } else if (weight < 1) {
            weight = 1;
        }
    }

    /**
     * Updates the current position of the particle and the fitness variation.
     * 
     * @param problem The problem that we are trying to solve.
     */
    public void updateCurrentPosition(Problem problem) {
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double[] position = getCurrentPosition();
        double[] nextPosition = new double[getDimensions()];
        double newPosition;
        for (int i = 0; i < getDimensions(); i++) {
            newPosition = position[i] + getVelocity()[i];

            // if a particle exceeds the search space limit, so inverts the particle velocity on
            // that dimension which the search space limit was exceeded
            if (newPosition >= problem.getUpperBound(i)) {
                position[i] = problem.getUpperBound(i) - getVelocity()[i] * random.nextDouble();
            } else if (newPosition <= problem.getLowerBound(i)) {
                position[i] = problem.getLowerBound(i) - getVelocity()[i] * random.nextDouble();
            } else {
                position[i] = newPosition;
            }
            nextPosition[i] = position[i];

        }

        deltaFitness = problem.getFitness(nextPosition) - getCurrentFitness();
        if (problem.isFitnessBetterThan(getCurrentFitness(), problem.getFitness(nextPosition)) && deltaFitness < 0) {
            deltaFitness *= -1;
        }

        // The particle never reduces its weight
        if (!problem.isFitnessBetterThan(getCurrentFitness(), problem.getFitness(nextPosition))) {
            deltaFitness = 0;
        }

        previousPosition = getCurrentPosition().clone();
        updateCurrentPosition(nextPosition, problem.getFitness(nextPosition));

        if (problem.isFitnessBetterThan(getBestFitness(), getCurrentFitness())) {
            updateBestPosition(getCurrentPosition(), getCurrentFitness());
        }

    }

    /**
     * Return the current particle weight.
     * 
     * @return The current particle weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the current particle weight.
     * 
     * @param weight The current particle weight.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDeltaFitness() {
        return deltaFitness;
    }

    public void setDeltaFitness(double deltaFitness) {
        this.deltaFitness = deltaFitness;
    }

}
