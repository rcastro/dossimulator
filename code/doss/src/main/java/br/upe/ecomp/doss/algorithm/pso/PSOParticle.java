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
package br.upe.ecomp.doss.algorithm.pso;

import java.util.Random;

import org.apache.commons.math.random.MersenneTwister;

import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.problem.Problem;

/**
 * An specialization of the {@link Particle} class for the PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public class PSOParticle extends Particle {

    private double[] velocity;
    private double velocityClampingPercent = 0.5;

    /**
     * Creates a new instance of this class.
     * 
     * @param dimensions The number of dimensions of the problem we are trying to solve.
     */
    public PSOParticle(int dimensions) {
        super(dimensions);

        velocity = new double[dimensions];
    }

    /**
     * Updates the best position found by this particle.
     * 
     * @param problem The problem we are trying to solve.
     */
    public void updatePBest(Problem problem) {
        double currentParticleFitness = problem.getFitness(getCurrentPosition());
        double pBestFitness = problem.getFitness(getBestPosition());

        if (problem.isFitnessBetterThan(pBestFitness, currentParticleFitness)) {
            updateBestPosition(getCurrentPosition().clone(), currentParticleFitness);
        }
    }

    /**
     * Updates the current velocity of the particle.
     * 
     * @param inertialWeight The inertia weight
     * @param bestParticleNeighborhood The best particle in the neighborhood
     * @param c1 The cognitive component
     * @param c2 The social component
     * @param problem The problem that we are trying to solve.
     */
    public void updateVelocity(double inertialWeight, double[] bestParticleNeighborhood, double c1, double c2,
            Problem problem) {
        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        double[] pBest = getBestPosition();
        for (int i = 0; i < getDimensions(); i++) {
            velocity[i] = inertialWeight * velocity[i] + c1 * r1 * (pBest[i] - getCurrentPosition()[i]) + c2 * r2
                    * (bestParticleNeighborhood[i] - getCurrentPosition()[i]);

            double maxVelocity = (problem.getUpperBound(i) - problem.getLowerBound(i)) * velocityClampingPercent;
            velocity[i] = (velocity[i] > maxVelocity) ? maxVelocity : velocity[i];
            velocity[i] = (velocity[i] < -maxVelocity) ? -maxVelocity : velocity[i];
        }
    }

    /**
     * Updates the current position of the particle.
     * 
     * @param problem The problem that we are trying to solve.
     */
    public void updateCurrentPosition(Problem problem) {
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double[] position = getCurrentPosition().clone();
        double nextPosition;
        double positionLimiter = 0.5;
        for (int i = 0; i < getDimensions(); i++) {
            nextPosition = position[i] + velocity[i];

            // if a particle exceeds the search space limit, so inverts the particle velocity on
            // that dimension which the search space limit was exceeded
            if (nextPosition >= problem.getUpperBound(i)) {
                position[i] = problem.getUpperBound(i) - velocity[i] * random.nextDouble() * positionLimiter;
            } else if (nextPosition <= problem.getLowerBound(i)) {
                position[i] = problem.getLowerBound(i) - velocity[i] * random.nextDouble() * positionLimiter;
            } else {
                position[i] = nextPosition;
            }
        }
        updateCurrentPosition(position, problem.getFitness(position));
    }

    /**
     * Return the current velocity of the particle.
     * 
     * @return The current velocity of the particle.
     */
    public double[] getVelocity() {
        return velocity;
    }

    /**
     * Sets the current velocity of the particle.
     * 
     * @param velocity The current velocity of the particle.
     */
    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

}
