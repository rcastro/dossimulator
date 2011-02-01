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

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.algorithm.pso.topology.ITopology;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * An implementation of the PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public abstract class PSO extends Algorithm {

    @Parameter(name = "C1")
    private double c1;

    @Parameter(name = "C2")
    private double c2;

    @Parameter(name = "Swarm size")
    private int swarmSize;

    @Parameter(name = "Initial inertia weight")
    private double initialInertiaWeight;

    @Parameter(name = "Final inertia weight")
    private double finalInertiaWeight;

    @Parameter(name = "Linear decay according environment changes")
    private boolean linearDecay;

    private double inertiaWeight;
    private int dimensions;
    private double[] gBest;

    private PSOParticle[] particles;

    private ITopology topology;

    /**
     * Default constructor.
     */
    public PSO() {
    }

    /**
     * {@inheritDoc}
     */
    public void init() {

        this.inertiaWeight = this.initialInertiaWeight;

        this.particles = new PSOParticle[swarmSize];
        this.dimensions = getProblem().getDimensionsNumber();

        createParticles();

        // Define the gBest particle of the first iteration
        this.gBest = particles[0].getCurrentPosition().clone();
        for (PSOParticle particle : particles) {
            calculateGBest(particle);
        }
    }

    protected void createParticles() {
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
    }

    /**
     * {@inheritDoc}
     */
    public void iterate() {

        // Calculating the pbest and gbest positions
        for (PSOParticle particle : particles) {
            particle.updatePBest(getProblem());

            // Stores the best particle's position.
            calculateGBest(particle);
        }

        // Updating the velocity and position of all particles
        for (int i = 0; i < swarmSize; i++) {
            PSOParticle particle = particles[i];

            updateParticleVelocity(particle, i);
            particle.updateCurrentPosition(getProblem());
        }

        if (linearDecay) {
            if (getIterations() % getProblem().getChangeStep() != 0) {
                inertiaWeight = inertiaWeight
                        - ((initialInertiaWeight - finalInertiaWeight) / getProblem().getChangeStep());
            } else {
                inertiaWeight = initialInertiaWeight;
            }
        }
    }

    /**
     * Updates the velocity of this particle.
     * 
     * @param currentParticle The particle that we want to update the velocity.
     * @param index The index position of the particle in the swarm.
     */
    protected void updateParticleVelocity(PSOParticle currentParticle, int index) {
        PSOParticle bestParticleNeighborhood;

        bestParticleNeighborhood = topology.getBestParticleNeighborhood(this, index);
        currentParticle.updateVelocity(inertiaWeight, bestParticleNeighborhood.getBestPosition(), c1, c2, getProblem());

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

        if (getProblem().isFitnessBetterThan(gBestFitness, pBestFitness)) {
            this.gBest = pBest.clone();
        }
    }

    protected double[] getInitialPosition() {
        double[] position = new double[this.dimensions];
        // Random random = new Random(System.nanoTime());
        MersenneTwister random = new MersenneTwister(System.nanoTime());

        for (int i = 0; i < this.dimensions; i++) {
            double value = random.nextDouble();

            position[i] = (getProblem().getUpperBound(i) - getProblem().getLowerBound(i)) * value
                    + getProblem().getLowerBound(i);

            if (position[i] > getProblem().getUpperBound(i)) {
                position[i] = getProblem().getUpperBound(i);
            } else if (position[i] < getProblem().getLowerBound(i)) {
                position[i] = getProblem().getLowerBound(i);
            }
        }
        return position;
    }

    protected double[] getInitialVelocity() {
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

    public PSOParticle[] getParticles() {
        return particles;
    }

    public void setParticles(PSOParticle[] particles) {
        this.particles = particles;
    }

    protected void setgBest(double[] gBest) {
        this.gBest = gBest;
    }

    public int getDimensions() {
        return dimensions;
    }

    protected void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public double getInertiaWeight() {
        return inertiaWeight;
    }

    public void setInertiaWeight(double inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    public void setTopology(ITopology topology) {
        this.topology = topology;
    }

    public ITopology getTopology() {
        return topology;
    }

    public double getInitialInertiaWeight() {
        return initialInertiaWeight;
    }

    public double getFinalInertiaWeight() {
        return finalInertiaWeight;
    }

    public boolean isLinearDecay() {
        return linearDecay;
    }

    public void setInitialInertiaWeight(double initialInertiaWeight) {
        this.initialInertiaWeight = initialInertiaWeight;
    }

    public void setFinalInertiaWeight(double finalInertiaWeight) {
        this.finalInertiaWeight = finalInertiaWeight;
    }

    public void setLinearDecay(boolean linearDecay) {
        this.linearDecay = linearDecay;
    }

    @Override
    public void setParticles(Particle[] particles) {
        this.particles = (PSOParticle[]) particles;
        super.setParticles(particles);
    }
}
