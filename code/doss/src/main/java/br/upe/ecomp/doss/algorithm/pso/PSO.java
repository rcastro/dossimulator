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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;

/**
 * An implementation of the PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public abstract class PSO extends Algorithm {

    public static final String C2 = "C2";
    public static final String C1 = "C1";
    public static final String SWARM_SIZE = "Swarm size";

    private static final double INITIAL_WEIGHT = 0.9;
    private static final double FINAL_WEIGHT = 0.4;

    private int dimensions;
    private double c1;
    private double c2;
    private int swarmSize;
    private double[] gBest;

    // Current inertia factor
    private double inertialWeight;
    private Integer maxIterations;

    private PSOParticle[] particles;
    private Map<String, Class<?>> parametersMap;

    /**
     * Default constructor.
     */
    public PSO() {
        initParametersMap();
    }

    private void initParametersMap() {
        this.parametersMap = new HashMap<String, Class<?>>();
        parametersMap.put(SWARM_SIZE, Integer.class);
        parametersMap.put(C1, Double.class);
        parametersMap.put(C2, Double.class);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        this.particles = new PSOParticle[swarmSize];
        this.dimensions = getProblem().getDimensionsNumber();

        inertialWeight = INITIAL_WEIGHT;

        // TODO Melhorar esta parte do codigo. Nao usar hard code "get(0)"
        this.maxIterations = (Integer) getStopConditions().get(0).getParameterByName(
                MaximumIterationsStopCondition.MAXIMUM_ITERATIONS);

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

            // Stores the better particle's position.
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

        // System.out.println("Current best position ["+ iteration +"/"+
        // maxIterations +"]: " +
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
     * If the particle informed has a better position them the actual global
     * best particle, it becomes the new global best particle.
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

            position[i] = (getProblem().getUpperBound(i) - getProblem().getLowerBound(i)) * value
                    + getProblem().getLowerBound(i);

            position[i] = (position[i] <= getProblem().getUpperBound(i)) ? position[i] : getProblem().getUpperBound(i);
            position[i] = (position[i] >= getProblem().getLowerBound(i)) ? position[i] : getProblem().getLowerBound(i);
        }

        return position;
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
    public double[] getBestSolution() {
        return gBest;
    }

    /**
     * {@inheritDoc}
     */
    public double getBestSolutionValue() {
        return getProblem().getFitness(gBest);
    }

    /**
     * Returns the size of the swarm.
     * 
     * @return The size of the swarm.
     */
    public int getSwarmSize() {
        return swarmSize;
    }

    /**
     * Sets the size of the swarm.
     * 
     * @param swarmSize the size of the swarm.
     */
    public void setSwarmSize(int swarmSize) {
        this.swarmSize = swarmSize;
    }

    /**
     * Sets the maximum quantity of iterations of the algorithm.
     * 
     * @param maxIterations the maximum quantity of iterations of the algorithm.
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    /**
     * Returns the cognitive component.
     * 
     * @return The cognitive component.
     */
    public double getC1() {
        return c1;
    }

    /**
     * Sets the cognitive component.
     * 
     * @param c1 The cognitive component.
     */
    public void setC1(double c1) {
        this.c1 = c1;
    }

    /**
     * Returns the social component.
     * 
     * @return The social component.
     */
    public double getC2() {
        return c2;
    }

    /**
     * Sets the social component.
     * 
     * @param c2 The social component.
     */
    public void setC2(double c2) {
        this.c2 = c2;
    }

    /**
     * Returns The Global Best.
     * 
     * @return The Global Best.
     */
    public double[] getgBest() {
        return gBest;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Class<?>> getParametersMap() {
        return parametersMap;
    }

    /**
     * {@inheritDoc}
     */
    public void setParameterByName(String name, Object value) {
        if (name.equals(SWARM_SIZE)) {
            setSwarmSize((Integer) value);
        } else if (name.equals(C1)) {
            setC1((Double) value);
        } else if (name.equals(C2)) {
            setC2((Double) value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getParameterByName(String name) {
        Object value = null;
        if (name.equals(SWARM_SIZE)) {
            value = swarmSize;
        } else if (name.equals(C1)) {
            value = c1;
        } else if (name.equals(C2)) {
            value = c2;
        }
        return value;
    }

    /**
     * Returns the best particle of the neighborhood. The neighborhood is
     * calculated based on the index of the swarm informed and depends on the
     * algorithm topology.
     * 
     * @param index The index of the swarm.
     * @return The best particle of the neighborhood.
     */
    protected abstract PSOParticle getBestParticleNeighborhood(int index);
}
