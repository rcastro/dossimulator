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
 * @author George Moraes
 */
public abstract class PSO extends Algorithm {

    @Parameter(name = "Initial C1")
    private double initialC1;

    @Parameter(name = "Final C1")
    private double finalC1;

    @Parameter(name = "Initial C2")
    private double initialC2;

    @Parameter(name = "Final C2")
    private double finalC2;

    @Parameter(name = "Decrease C1 and Increase C2")
    private boolean decreaseC1increaseC2;

    @Parameter(name = "Swarm size")
    private int swarmSize;

    @Parameter(name = "Initial inertia weight")
    private double initialInertiaWeight;

    @Parameter(name = "Final inertia weight")
    private double finalInertiaWeight;

    @Parameter(name = "Inertia linear decay according environment changes")
    private boolean linearDecay;

    @Parameter(name = "Reinitializate particle percent")
    private double percentRestartParticles;

    private double inertiaWeight;
    private double c1;
    private double c2;
    private int dimensions;
    private double[] gBest;
    private PSOParticle bestParticle;

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
        this.c1 = this.initialC1;
        this.c2 = this.initialC2;

        this.particles = new PSOParticle[swarmSize];
        this.dimensions = getProblem().getDimensionsNumber();

        createParticles();

        // Define the gBest particle of the first iteration
        this.gBest = particles[0].getCurrentPosition().clone();
        this.bestParticle = particles[0];
        calculatePbestAndGbest();
    }

    /**
     * Create all particles of the swarm.
     */
    protected void createParticles() {
        for (int i = 0; i < swarmSize; i++) {
            particles[i] = newParticle();
        }
        setParticles(particles);
    }

    public PSOParticle newParticle() {
        double[] position = getInitialPosition();
        PSOParticle particle = new PSOParticle(dimensions);
        particle.updateCurrentPosition(position, getProblem().getFitness(position));
        particle.updateBestPosition(position.clone(), particle.getCurrentFitness());
        particle.setVelocity(getInitialVelocity());
        return particle;
    }

    public void reInitParticles(double percent, PSOParticle[] particles) {
        if (getProblem().getChangeStep() != 0) {
            if (getIterations() > 0 && getIterations() % getProblem().getChangeStep() == 0) {
                int newPerticlesNum = (int) Math.floor(getSwarmSize() * (percent / 100));
                for (int i = 0; i < newPerticlesNum; i++) {
                    particles[i] = newParticle();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void iterate() {
        // Updating the velocity and position of all particles
        updateVelocityAndPosition();

        // Calculating the pbest and gbest positions
        calculatePbestAndGbest();

        // Updating parameters
        updateParameters();

        // Reinitializing particles after environment change
        reInitParticles(percentRestartParticles, particles);
    }

    protected void updateVelocityAndPosition() {
        for (int i = 0; i < swarmSize; i++) {
            PSOParticle particle = particles[i];
            updateParticleVelocity(particle, i);
            particle.updateCurrentPosition(getProblem());
        }
    }

    protected void calculatePbestAndGbest() {
        for (PSOParticle particle : particles) {
            particle.updatePBest(getProblem());
            // Stores the best particle's position.
            calculateGBest(particle);
        }
    }

    protected void updateParameters() {
        if (linearDecay) {
            if (getIterations() % getProblem().getChangeStep() != 0) {
                inertiaWeight = inertiaWeight
                        - ((initialInertiaWeight - finalInertiaWeight) / getProblem().getChangeStep());
            } else {
                inertiaWeight = initialInertiaWeight;
            }
        }

        if (decreaseC1increaseC2) {
            if (getIterations() % getProblem().getChangeStep() != 0) {
                // decrease linearly
                c1 = c1 - ((initialC1 - finalC1) / getProblem().getChangeStep());
                // increase linearly
                c2 = c2 - ((initialC2 - finalC2) / getProblem().getChangeStep());
            } else {
                c1 = initialC1;
                c2 = initialC2;
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
            bestParticle = particle;
        }
    }

    protected double[] getInitialPosition() {
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double[] position = new double[this.dimensions];

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
        Random random = new Random(System.nanoTime());
        double[] velocity = new double[this.dimensions];
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

    public PSOParticle getBestParticle() {
        return bestParticle;
    }

    public void setBestParticle(PSOParticle bestParticle) {
        this.bestParticle = bestParticle;
    }

    public double getPercentRestartParticles() {
        return percentRestartParticles;
    }

    public void setPercentRestartParticles(double percentRestartParticles) {
        this.percentRestartParticles = percentRestartParticles;
    }

    public double getInitialC1() {
        return initialC1;
    }

    public double getInitialC2() {
        return initialC2;
    }

    public void setInitialC1(double initialC1) {
        this.initialC1 = initialC1;
    }

    public void setFinalC1(double finalC1) {
        this.finalC1 = finalC1;
    }

    public void setInitialC2(double initialC2) {
        this.initialC2 = initialC2;
    }

    public void setFinalC2(double finalC2) {
        this.finalC2 = finalC2;
    }

    public void setDecreaseC1increaseC2(boolean decreaseC1increaseC2) {
        this.decreaseC1increaseC2 = decreaseC1increaseC2;
    }

    @Override
    public void setParticles(Particle[] particles) {
        this.particles = (PSOParticle[]) particles;
        super.setParticles(particles);
    }
}
