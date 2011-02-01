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
package br.upe.ecomp.doss.algorithm.volitiveapso;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.apso.APSO;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * An implementation of the WeightedPSO algorithm.
 * 
 * @author George Moraes
 */
public abstract class VolitiveAPSO extends APSO {

    private double previousSwarmWeight;

    private double[] stepVolInit;
    private double[] stepVolFinal;
    private double[] stepVol;

    @Parameter(name = "Weight scale")
    private double wScale;

    @Parameter(name = "Initial volitive step percentage")
    private double stepVolInitPercentage;

    @Parameter(name = "Final volitive step percentage")
    private double stepVolFinalPercentage;

    private WeightedAPSOParticle sentry;

    /**
     * Default constructor.
     */
    public VolitiveAPSO() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        setDimensions(getProblem().getDimensionsNumber());

        createParticles();
        previousSwarmWeight = getSwarmWeigth();

        // Define the gBest particle of the first iteration
        setgBest(getParticles()[0].getCurrentPosition().clone());
        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        for (WeightedAPSOParticle particle : particles) {
            calculateGBest(particle);
        }

        // Updating each mean distance to others particles
        updateMeanDistanceToOtherPaticles(particles);
        // Defines the evolutionary state of the swarm and adapts parameters
        defineSwarmEvolutionaryState();
        // Executes the Elitist Learning Strategy
        doElitistLearningStrategy();

        initSteps();
    }

    /**
     * {@inheritDoc}
     */
    public void iterate() {

        if (isEnvironmentChanged()) {
            // System.out.println("The environment has changed!");
            initSteps();
        }

        // Updating the velocity and position of all particles
        for (int i = 0; i < getSwarmSize(); i++) {
            WeightedAPSOParticle particle = (WeightedAPSOParticle) getParticles()[i];

            updateParticleVelocity(particle, i);
            particle.updateCurrentPosition(getProblem());
        }

        feedFishSchool();
        performCollectiveVolitiveMovement();

        // Calculating the pbest and gbest positions
        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        for (WeightedAPSOParticle particle : particles) {
            particle.updatePBest(getProblem());

            // Stores the best particle's position.
            calculateGBest(particle);
        }

        // Updating each mean distance to others particles
        updateMeanDistanceToOtherPaticles(particles);
        // Defines the evolutionary state of the swarm and adapts parameters
        defineSwarmEvolutionaryState();
        // Executes the Elitist Learning Strategy
        doElitistLearningStrategy();

        updateSteps();

    }

    /**
     * Creates randomly the particles of swarm.
     */
    protected void createParticles() {
        WeightedAPSOParticle[] particles = new WeightedAPSOParticle[getSwarmSize()];

        double[] position;
        for (int i = 0; i < getSwarmSize(); i++) {
            position = getInitialPosition();

            WeightedAPSOParticle particle = new WeightedAPSOParticle(getDimensions());
            particle.setWeight(wScale / 2);
            particle.updateCurrentPosition(position, getProblem().getFitness(position));
            particle.updateBestPosition(position.clone(), particle.getCurrentFitness());
            particle.setVelocity(getInitialVelocity());

            particles[i] = particle;
        }

        this.sentry = particles[0];

        setParticles(particles);
    }

    private void initSteps() {
        if (stepVolInit == null) {
            stepVolInit = new double[getDimensions()];
        }
        if (stepVolFinal == null) {
            stepVolFinal = new double[getDimensions()];
        }

        double boundRange;
        for (int i = 0; i < getDimensions(); i++) {
            boundRange = Math.abs(getProblem().getUpperBound(i) - getProblem().getLowerBound(i));
            stepVolInit[i] = (stepVolInitPercentage / 100) * boundRange;
            stepVolFinal[i] = (stepVolFinalPercentage / 100) * boundRange;
        }
        stepVol = stepVolInit.clone();
    }

    private void updateSteps() {
        double factor = 0.1;
        for (int i = 0; i < getDimensions(); i++) {
            stepVol[i] = (1 - factor) * stepVol[i];
        }
    }

    private boolean isEnvironmentChanged() {
        if (getProblem().getFitness(sentry.getCurrentPosition()) != sentry.getCurrentFitness()) {
            return true;
        }
        return false;
    }

    private void performCollectiveVolitiveMovement() {
        RandomData random = new RandomDataImpl();
        double[] barycenter = calculateBarycenter();

        double actualSwarmWeight = getSwarmWeigth();

        double euclidianDistance;
        double[] position;
        double currentPosition;

        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        for (WeightedAPSOParticle particle : particles) {

            euclidianDistance = MathUtils.distance(particle.getCurrentPosition(), barycenter);
            position = new double[getDimensions()];

            for (int i = 0; i < getDimensions(); i++) {
                currentPosition = particle.getCurrentPosition()[i];

                if (actualSwarmWeight > previousSwarmWeight) {

                    position[i] = currentPosition - stepVol[i] * random.nextUniform(0.0, 1.0)
                            * ((currentPosition - barycenter[i]) / euclidianDistance);
                } else {
                    position[i] = currentPosition + stepVol[i] * random.nextUniform(0.0, 1.0)
                            * ((currentPosition - barycenter[i]) / euclidianDistance);
                }
                if (position[i] > getProblem().getUpperBound(i) || position[i] < getProblem().getLowerBound(i)) {
                    position[i] = currentPosition;
                }
            }

            // if (getProblem().compareFitness(fish.getCurrentFitness(),
            // getProblem().getFitness(position))) {
            particle.updateCurrentPosition(position, getProblem().getFitness(position));

            if (getProblem().isFitnessBetterThan(particle.getBestFitness(), particle.getCurrentFitness())) {
                particle.updateBestPosition(particle.getCurrentPosition(), particle.getCurrentFitness());
            }
            // }
        }

        previousSwarmWeight = actualSwarmWeight;
    }

    private double[] calculateBarycenter() {
        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        double[] barycenter = new double[getDimensions()];
        double allWeightSum = 0;

        for (WeightedAPSOParticle particle : particles) {
            allWeightSum += particle.getWeight();

            for (int i = 0; i < getDimensions(); i++) {
                barycenter[i] = barycenter[i] + (particle.getCurrentPosition()[i] * particle.getWeight());
            }
        }
        for (int i = 0; i < getDimensions(); i++) {
            barycenter[i] = barycenter[i] / allWeightSum;
        }
        return barycenter;
    }

    private void feedFishSchool() {
        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        for (WeightedAPSOParticle particle : particles) {
            double maxAbsDeltaFitness = getMaxAbsDeltaFitness();
            particle.feed(maxAbsDeltaFitness, wScale);
        }
    }

    private double getMaxAbsDeltaFitness() {
        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        double maxAbsDeltaFitness = 0;
        for (WeightedAPSOParticle particle : particles) {
            if (Math.abs(particle.getDeltaFitness()) > maxAbsDeltaFitness) {
                maxAbsDeltaFitness = Math.abs(particle.getDeltaFitness());
            }
        }
        if (maxAbsDeltaFitness == 0) {
            maxAbsDeltaFitness = 1;
        }
        return maxAbsDeltaFitness;
    }

    private double getSwarmWeigth() {
        WeightedAPSOParticle[] particles = (WeightedAPSOParticle[]) getParticles();
        double allWeightSum = 0;
        for (WeightedAPSOParticle particle : particles) {
            allWeightSum += particle.getWeight();
        }
        return allWeightSum;
    }

}
