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

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.pso.PSO;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * An implementation of the WeightedPSO algorithm.
 * 
 * @author George Moraes
 */
public abstract class VolitivePSO extends PSO {

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

    @Parameter(name = "Volitive step decay percentage")
    private double stepVolDecayPercentage;

    private WeightedPSOParticle sentry;

    /**
     * Default constructor.
     */
    public VolitivePSO() {
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
        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        for (WeightedPSOParticle particle : particles) {
            calculateGBest(particle);
        }

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
            WeightedPSOParticle particle = (WeightedPSOParticle) getParticles()[i];

            updateParticleVelocity(particle, i);
            particle.updateCurrentPosition(getProblem());
        }

        feedFishSchool();
        performCollectiveVolitiveMovement();

        // Calculating the pbest and gbest positions
        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        for (WeightedPSOParticle particle : particles) {
            particle.updatePBest(getProblem());

            // Stores the best particle's position.
            calculateGBest(particle);
        }

        updateSteps();

    }

    private boolean isEnvironmentChanged() {
        if (getProblem().getFitness(sentry.getCurrentPosition()) != sentry.getCurrentFitness()) {
            return true;
        }
        return false;
    }

    /**
     * Creates randomly the particles of swarm.
     */
    protected void createParticles() {
        WeightedPSOParticle[] particles = new WeightedPSOParticle[getSwarmSize()];

        double[] position;
        for (int i = 0; i < getSwarmSize(); i++) {
            position = getInitialPosition();

            WeightedPSOParticle particle = new WeightedPSOParticle(getDimensions());
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
        for (int i = 0; i < getDimensions(); i++) {
            stepVol[i] = ((100 - stepVolDecayPercentage) / 100) * stepVol[i];
        }

        if (isLinearDecay()) {
            if (getIterations() % getProblem().getChangeStep() != 0) {
                double inertia = getInertiaWeight();
                inertia = inertia - ((getInertiaWeight() - getFinalInertiaWeight()) / getProblem().getChangeStep());
                setInertiaWeight(inertia);
            } else {
                setInertiaWeight(getInitialInertiaWeight());
            }
        }
    }

    private void performCollectiveVolitiveMovement() {

        RandomData random = new RandomDataImpl();
        double[] barycenter = calculateBarycenter();

        double actualSwarmWeight = getSwarmWeigth();

        double euclidianDistance;
        double[] position;
        double currentPosition;

        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        for (WeightedPSOParticle particle : particles) {

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
        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        double[] barycenter = new double[getDimensions()];
        double allWeightSum = 0;

        for (WeightedPSOParticle particle : particles) {
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
        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        for (WeightedPSOParticle particle : particles) {
            double maxAbsDeltaFitness = getMaxAbsDeltaFitness();
            particle.feed(maxAbsDeltaFitness, wScale);
        }
    }

    private double getMaxAbsDeltaFitness() {
        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        double maxAbsDeltaFitness = 0;
        for (WeightedPSOParticle particle : particles) {
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
        WeightedPSOParticle[] particles = (WeightedPSOParticle[]) getParticles();
        double allWeightSum = 0;
        for (WeightedPSOParticle particle : particles) {
            allWeightSum += particle.getWeight();
        }
        return allWeightSum;
    }

    public double[] getStepVolFinal() {
        return stepVolFinal;
    }

    public void setStepVolFinal(double[] stepVolFinal) {
        this.stepVolFinal = stepVolFinal;
    }

    public double getwScale() {
        return wScale;
    }

    public void setwScale(double wScale) {
        this.wScale = wScale;
    }

    public double getStepVolInitPercentage() {
        return stepVolInitPercentage;
    }

    public void setStepVolInitPercentage(double stepVolInitPercentage) {
        this.stepVolInitPercentage = stepVolInitPercentage;
    }

    public double getStepVolFinalPercentage() {
        return stepVolFinalPercentage;
    }

    public void setStepVolFinalPercentage(double stepVolFinalPercentage) {
        this.stepVolFinalPercentage = stepVolFinalPercentage;
    }

    public double getStepVolDecayPercentage() {
        return stepVolDecayPercentage;
    }

    public void setStepVolDecayPercentage(double stepVolDecayPercentage) {
        this.stepVolDecayPercentage = stepVolDecayPercentage;
    }

}
