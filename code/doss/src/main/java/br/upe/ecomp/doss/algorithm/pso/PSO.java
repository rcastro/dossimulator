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

import java.util.Map;
import java.util.Random;

import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * An implementation of the PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public abstract class PSO extends Algorithm {

    private static final double INITIAL_WEIGHT = 0.9;
    private static final double FINAL_WEIGHT = 0.4;

    private int dimensions;
    private double c1;
    private double c2;
    private int swarmSize;
    private double[] gBest;

    // Current inertia factor
    private double inertialWeight;
    private int maxIterations;

    private PSOParticle[] particles;

    /**
     * {@inheritDoc}
     */
    public void init() {
        this.dimensions = getProblem().getDimensionsNumber();

        double[] position;
        for (int i = 0; i < swarmSize; i++) {
            position = getInitialPosition();

            PSOParticle particle = new PSOParticle(dimensions);
            particle.updateCurrentPosition(position, getProblem().getFitness(position));
            particle.updateBestPosition(position.clone(), particle.getCurrentFitness());
            particle.setVelocity(getInitialVelocity());

            particles[i] = particle;
        }
        setParticles(particles);

        // Define the gBest particle of the first iteration
        this.gBest = particles[0].getCurrentPosition().clone();
        for (PSOParticle particle : particles) {
            calculateGBest(particle);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void iterate() {

        // Calculating the pbest and gbest positions
        for (PSOParticle particle : particles) {
            particle.updatePBest(getProblem());

            // stores the better particle's position.
            calculateGBest(particle);
        }

        // Updating the velocity and position for all particles
        for (int i = 0; i < swarmSize; i++) {
            PSOParticle particle = particles[i];

            updateParticleVelocity(particle, i);
            particle.updateCurrentPosition(getProblem());
        }

        // Updating the inertial weight with linear decaiment
        inertialWeight = (inertialWeight - FINAL_WEIGHT) * ((maxIterations - getIterations()) / (double) maxIterations)
                + FINAL_WEIGHT;

        // System.out.println("Current best position ["+ iteration +"/"+ maxIterations +"]: " +
        // problem.getFitness(this.gBest));
    }

    /**
     * Updates the velocity of this particle.
     * 
     * @param currentParticle The particle that we want to update the velocity.
     * @param index The index position of the particle in the swarm.
     */
    protected void updateParticleVelocity(PSOParticle currentParticle, int index) {
        PSOParticle bestParticleNeighborhood;

        bestParticleNeighborhood = getBestParticleNeighborhood(index);
        currentParticle.updateVelocity(inertialWeight, bestParticleNeighborhood.getBestPosition(), c1, c2);
    }

    /**
     * Updates the global best particle based on the particle informed.<br>
     * If the particle informed has a better position them the actual global best particle, she
     * becomes the new global best particle.
     * 
     * @param particle The particle that will have the best position evaluated.
     */
    protected void calculateGBest(PSOParticle particle) {
        double[] pBest = particle.getBestPosition();

        double pBestFitness = getProblem().getFitness(pBest);
        double gBestFitness = getProblem().getFitness(this.gBest);

        if (getProblem().compareFitness(gBestFitness, pBestFitness)) {
            this.gBest = pBest.clone();
        }
    }

    private double[] getInitialPosition() {
        double[] position = new double[this.dimensions];
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < this.dimensions; i++) {
            double value = random.nextDouble();

            position[i] = (getProblem().getUpperLimit(i) - getProblem().getLowerLimit(i)) * value
                    + getProblem().getLowerLimit(i);

            position[i] = (position[i] <= getProblem().getUpperLimit(i)) ? position[i] : getProblem().getUpperLimit(i);
            position[i] = (position[i] >= getProblem().getLowerLimit(i)) ? position[i] : getProblem().getLowerLimit(i);
        }

        return position;
    }

    /**
     * Returns the size of the swarm.
     * 
     * @return The size of the swarm.
     */
    public int getSwarmSize() {
        return swarmSize;
    }

    private double[] getInitialVelocity() {
        double[] velocity = new double[this.dimensions];
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < this.dimensions; i++) {

            // The initial velocity ought be a value between zero and one
            velocity[i] = random.nextDouble();
        }

        return velocity;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Class<?>> getParametersMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setParameterByName(String name, Object value) {

    }

    /**
     * {@inheritDoc}
     */
    public Object getParameterByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the best particle of the neighborhood. The neighborhood is calculated based on the
     * index of the swarm informed and depends on the algorithm topology.
     * 
     * @param index The index of the swarm.
     * @return The best particle of the neighborhood.
     */
    protected abstract PSOParticle getBestParticleNeighborhood(int index);
}
