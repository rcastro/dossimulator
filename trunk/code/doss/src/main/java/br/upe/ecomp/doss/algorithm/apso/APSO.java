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
package br.upe.ecomp.doss.algorithm.apso;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.pso.AbstractPSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * An implementation of the APSO algorithm.
 * 
 * @author George Moraes
 */
public abstract class APSO extends AbstractPSO {

    // APSO necessary parameters
    private double inertiaWeightMin;
    private double inertiaWeightMax;
    private double cMin;
    private double cMax;
    private FuzzyMembershipFunction membershipFunction;
    private EnumEvolutionaryState currentEvolutionaryState;
    private double evolutionaryFactor;
    // Elitist learning rate bounds
    private double sigmaMin;
    private double sigmaMax;

    /**
     * Default constructor.
     */
    public APSO() {
        this.membershipFunction = new FuzzyMembershipFunction(0, 0, 0, 0);
        this.inertiaWeightMin = 0.4;
        this.inertiaWeightMax = 0.9;
        this.cMin = 1.5;
        this.cMax = 2.5;
        this.sigmaMin = 0.1;
        this.sigmaMax = 1;
        this.evolutionaryFactor = 0;
        setDecreaseC1increaseC2(false); // Because C1 and C2 and auto adaptive in APSO algorithm
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        setDimensions(getProblem().getDimensionsNumber());

        createParticles();

        // Define the gBest particle of the first iteration
        setgBest(getParticles()[0].getCurrentPosition().clone());
        APSOParticle[] particles = (APSOParticle[]) getParticles();
        setBestParticle(getParticles()[0]);
        calculatePbestAndGbest();

        // Updating each mean distance to others particles
        updateMeanDistanceToOtherPaticles(particles);
        // Defines the evolutionary state of the swarm and adapts parameters
        defineSwarmEvolutionaryState();
        // Executes the Elitist Learning Strategy
        doElitistLearningStrategy();
    }

    /**
     * Creates randomly the particles of swarm.
     */
    protected void createParticles() {
        APSOParticle[] particles = new APSOParticle[getSwarmSize()];
        for (int i = 0; i < getSwarmSize(); i++) {
            particles[i] = newParticle();
        }
        setParticles(particles);
    }

    public APSOParticle newParticle() {
        double[] position = getInitialPosition();
        APSOParticle particle = new APSOParticle(getDimensions());
        particle.updateCurrentPosition(position, getProblem().getFitness(position));
        particle.updateBestPosition(position.clone(), particle.getCurrentFitness());
        particle.setVelocity(getInitialVelocity());
        return particle;
    }

    /**
     * {@inheritDoc}
     */
    public void iterate() {
        APSOParticle[] particles = (APSOParticle[]) getParticles();

        // Updating the velocity and position of all particles
        updateVelocityAndPosition();
        // Calculating the pbest and gbest positions
        calculatePbestAndGbest();

        // Updating each mean distance to others particles
        updateMeanDistanceToOtherPaticles(particles);
        // Defines the evolutionary state of the swarm and adapts parameters
        defineSwarmEvolutionaryState();
        // Executes the Elitist Learning Strategy
        doElitistLearningStrategy();

        // Updating parameters
        updateParameters();

        // Reinitializing particles after environment change
        reInitParticles(getPercentRestartParticles(), particles);
    }

    private double calculateMeanDistanceToOtherPaticles(int index) {
        double euclideanDistance = 0;
        for (int i = 0; i < getParticles().length; i++) {
            if (index != i) {
                euclideanDistance += MathUtils.distance(getParticles()[index].getCurrentPosition(),
                        getParticles()[i].getCurrentPosition());
            }
        }
        return euclideanDistance / (getParticles().length - 1);
    }

    protected void updateMeanDistanceToOtherPaticles(APSOParticle[] particles) {
        for (int i = 0; i < particles.length; i++) {
            double meanDistance = calculateMeanDistanceToOtherPaticles(i);
            particles[i].setMeanDistanceToOthersParticles(meanDistance);
        }
    }

    private double calculateEvolutionaryFactor() {
        double evolFactor;
        APSOParticle[] particles = (APSOParticle[]) getParticles();
        List<APSOParticle> parciclesList = Arrays.asList(particles);
        APSOParticle particleAux = Collections.min(parciclesList, new ComparatorMeanDistance());
        double dmin = particleAux.getMeanDistanceToOthersParticles();
        particleAux = Collections.max(parciclesList, new ComparatorMeanDistance());
        double dmax = particleAux.getMeanDistanceToOthersParticles();
        particleAux = Collections.max(parciclesList, new ComparatorMaximumFitness());
        double dg = particleAux.getMeanDistanceToOthersParticles();

        evolFactor = (dg - dmin) / (dmax - dmin);

        return evolFactor;
    }

    protected void defineSwarmEvolutionaryState() {
        evolutionaryFactor = calculateEvolutionaryFactor();
        membershipFunction.fuzzification(evolutionaryFactor);
        currentEvolutionaryState = membershipFunction.defuzzification();
        adaptParameters(evolutionaryFactor);
    }

    private void adaptParameters(double evolFactor) {
        RandomData random = new RandomDataImpl();
        double accelerationRate = random.nextUniform(0.05, 0.1);
        double c1 = getC1();
        double c2 = getC2();
        if (currentEvolutionaryState.equals(EnumEvolutionaryState.CONVERGENCE)) {
            // C1 - increase slightly | C2 increase slightly
            c1 = c1 + (c1 * accelerationRate * 0.5);
            c2 = c2 + (c2 * accelerationRate * 0.5);
        } else if (currentEvolutionaryState.equals(EnumEvolutionaryState.EXPLOITATION)) {
            // C1 - increase | C2 decrease
            c1 = c1 + (c1 * accelerationRate);
            c2 = c2 - (c2 * accelerationRate);
        } else if (currentEvolutionaryState.equals(EnumEvolutionaryState.EXPLORATION)) {
            // C1 - increase slightly | C2 decrease slightly
            c1 = c1 + (c1 * accelerationRate * 0.5);
            c2 = c2 - (c2 * accelerationRate * 0.5);
        } else if (currentEvolutionaryState.equals(EnumEvolutionaryState.JUMPING_OUT)) {
            // C1 - decrease | C2 increase
            c1 = c1 - (c1 * accelerationRate);
            c2 = c2 + (c2 * accelerationRate);
        }
        // Normalization
        if ((c1 + c2) > (cMin + cMax)) {
            double c1Temp = c1;
            double c2Temp = c2;
            c1Temp = c1 * (cMin + cMax) / (c1 + c2);
            c2Temp = c2 * (cMin + cMax) / (c1 + c2);
            c1 = c1Temp;
            c2 = c2Temp;
        }

        // Verify and adjust C1 and C2 by bounds
        if (c1 < cMin) {
            c1 = cMin;
        }
        if (c2 < cMin) {
            c2 = cMin;
        }
        if (c1 > cMax) {
            c1 = cMax;
        }
        if (c2 > cMax) {
            c2 = cMax;
        }

        setC1(c1);
        setC2(c2);

        // Adapt inertia weight
        double inertiaWeight = 1 / (1 + 1.5 * Math.exp(-2.6 * evolFactor));
        // Verify and adjust inertia weight by bounds
        if (inertiaWeight < inertiaWeightMin) {
            inertiaWeight = inertiaWeightMin;
        }
        if (inertiaWeight > inertiaWeightMax) {
            inertiaWeight = inertiaWeightMax;
        }
        setInertiaWeight(inertiaWeight);
    }

    protected void doElitistLearningStrategy() {
        RandomData random = new RandomDataImpl();
        APSOParticle[] particles = (APSOParticle[]) getParticles();

        List<APSOParticle> apsoParticles = Arrays.asList(particles);
        APSOParticle bestParticle = Collections.max(apsoParticles, new ComparatorMaximumFitness());

        // Dimension that can be changed if the best particle fitness improves after the change
        int dimensionChange = random.nextInt(0, getProblem().getDimensionsNumber() - 1);
        double[] currentBestPosition = bestParticle.getBestPosition();

        // Standard Deviation of the Gaussian Distribution
        // TODO usar esse decaimento linear pode ser ruim para ambientes dinamicos
        double learningElitistRate = sigmaMax - (sigmaMax - sigmaMin) * (this.getIterations() / getMaxIterations());

        // Changes position in only one dimension
        // if(EnumEvolutionaryState.CONVERGENCE) TODO testar com e sem esta condicao
        double newDimensionPosition = currentBestPosition[dimensionChange]
                + (getProblem().getUpperBound(dimensionChange) - getProblem().getLowerBound(dimensionChange))
                * random.nextGaussian(0, learningElitistRate);

        // TODO verificar se nao extrapolou o limite do espaco de busca
        double[] newPosition = currentBestPosition.clone();
        newPosition[dimensionChange] = newDimensionPosition;

        double currentBestFitness = bestParticle.getBestFitness();
        double newFitness = getProblem().getFitness(newPosition);

        // If the change improves the fitness then update the best particle with the new position,
        // else update the worst particle with the new position.
        if (getProblem().isFitnessBetterThan(currentBestFitness, newFitness)) {
            bestParticle.updateBestPosition(newPosition.clone(), newFitness);
            bestParticle.updateCurrentPosition(newPosition.clone(), newFitness);
        } else {
            PSOParticle worstParticle = Collections.min(apsoParticles, new ComparatorMaximumFitness());
            worstParticle.updateCurrentPosition(newPosition.clone(), newFitness);
            worstParticle.updatePBest(getProblem());
        }
        calculateGBest(bestParticle);
    }

    // TODO retirar esse metodo,
    private int getMaxIterations() {
        for (StopCondition stop : getStopConditions()) {
            if (stop instanceof MaximumIterationsStopCondition) {
                return ((MaximumIterationsStopCondition) stop).getMaximumIterations();
            }
        }
        return 0;
    }

    public double getEvolutionaryFactor() {
        return evolutionaryFactor;
    }

}
