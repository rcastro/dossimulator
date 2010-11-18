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
package br.upe.ecomp.doss.algorithm.fss;

import java.util.Random;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.util.MathUtils;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class FSS extends Algorithm {

    private int dimensions;
    private double previousFishSchoolWeight;
    private Fish[] fishSchool;
    private double[] stepIndInit;
    private double[] stepIndFinal;
    private double[] stepVolInit;
    private double[] stepVolFinal;
    private double[] stepInd;
    private double[] stepVol;
    private double[] bestFishFitness;

    @Parameter(name = "Fish-school size")
    private int schoolSize;

    @Parameter(name = "Weight scale")
    private double wScale;

    @Parameter(name = "Initial individual step percentage")
    private double stepIndInitPercentage;

    @Parameter(name = "Final individual step percentage")
    private double stepIndFinalPercentage;

    @Parameter(name = "Initial volitive step percentage")
    private double stepVolInitPercentage;

    @Parameter(name = "Final volitive step percentage")
    private double stepVolFinalPercentage;

    /**
     * {@inheritDoc}
     */
    public void init() {
        dimensions = getProblem().getDimensionsNumber();
        createFishSchool();
        previousFishSchoolWeight = getFishSchoolWeigth();
        initSteps();
    }

    /**
     * {@inheritDoc}
     */
    public void iterate() {
        for (Fish fish : fishSchool) {
            fish.performIndividualMovement(stepInd, getProblem());
            calculateGBest(fish);
        }
        feedFishSchool();
        performCollectiveInstinctiveMovement();
        performCollectiveVolitiveMovement();
        updateSteps();
    }

    private void createFishSchool() {
        fishSchool = new Fish[schoolSize];

        double[] position;
        for (int i = 0; i < schoolSize; i++) {
            position = getInitialPosition();

            Fish fish = new Fish(dimensions);
            fish.setWeight(wScale / 2);
            fish.updateCurrentPosition(position, getProblem().getFitness(position));
            fish.updateBestPosition(position.clone(), fish.getCurrentFitness());

            fishSchool[i] = fish;
        }
        setParticles(fishSchool);

        // Define the best fish of the first iteration
        bestFishFitness = fishSchool[0].getCurrentPosition().clone();
        for (Fish fish : fishSchool) {
            calculateGBest(fish);
        }
    }

    private void initSteps() {
        stepIndInit = new double[dimensions];
        stepIndFinal = new double[dimensions];
        stepVolInit = new double[dimensions];
        stepVolFinal = new double[dimensions];

        double boundRange;
        for (int i = 0; i < dimensions; i++) {
            boundRange = Math.abs(getProblem().getUpperBound(i) - getProblem().getLowerBound(i));
            stepIndInit[i] = (stepIndInitPercentage / 100) * boundRange;
            stepIndFinal[i] = (stepIndFinalPercentage / 100) * boundRange;
            stepVolInit[i] = (stepVolInitPercentage / 100) * boundRange;
            stepVolFinal[i] = (stepVolFinalPercentage / 100) * boundRange;
        }
        stepInd = stepIndInit.clone();
        stepVol = stepVolInit.clone();
    }

    private void updateSteps() {
        double factor = getIterations() / 30000;
        for (int i = 0; i < dimensions; i++) {
            stepInd[i] = stepInd[i] - (stepIndInit[i] - stepIndFinal[i]) * factor;
            stepVol[i] = stepVol[i] - (stepVolInit[i] - stepVolFinal[i]) * factor;
        }
    }

    private void feedFishSchool() {
        for (Fish fish : fishSchool) {
            double maxAbsDeltaFitness = getMaxAbsDeltaFitness();
            fish.feed(maxAbsDeltaFitness, wScale);
        }
    }

    private void performCollectiveInstinctiveMovement() {
        double[] resultantDirection = calculateResultantDirection();

        for (Fish fish : fishSchool) {
            fish.updatePosition(resultantDirection, getProblem());
        }
    }

    private double[] calculateResultantDirection() {
        double[] resultantDirection = new double[dimensions];
        double allDeltaFitnessSum = 0;

        for (Fish fish : fishSchool) {

            if (fish.getDeltaFitness() > 0) {
                allDeltaFitnessSum += fish.getDeltaFitness();
                for (int i = 0; i < dimensions; i++) {
                    resultantDirection[i] = resultantDirection[i]
                            + (fish.getCurrentPosition()[i] - fish.getPreviousPosition()[i]) * fish.getDeltaFitness();
                }
            }
        }
        if (allDeltaFitnessSum > 0) {
            for (int i = 0; i < dimensions; i++) {
                resultantDirection[i] = resultantDirection[i] / allDeltaFitnessSum;
            }
        } else {
            for (int i = 0; i < dimensions; i++) {
                resultantDirection[i] = 0;
            }
        }
        return resultantDirection;
    }

    private void performCollectiveVolitiveMovement() {
        RandomData random = new RandomDataImpl();
        double[] barycenter = calculateBarycenter();

        double actualFishSchoolWeight = getFishSchoolWeigth();

        double euclidianDistance;
        double[] position;
        double currentPosition;
        for (Fish fish : fishSchool) {

            euclidianDistance = MathUtils.distance(fish.getCurrentPosition(), barycenter);
            position = new double[dimensions];

            for (int i = 0; i < dimensions; i++) {
                currentPosition = fish.getCurrentPosition()[i];

                if (actualFishSchoolWeight > previousFishSchoolWeight) {

                    position[i] = currentPosition - stepVol[i] * random.nextUniform(0.0, 1.0)
                            * ((currentPosition - barycenter[i]) / euclidianDistance);
                    // * ((currentPosition - barycenter[i]) / euclidianDistance);
                } else {
                    position[i] = currentPosition + stepVol[i] * random.nextUniform(0.0, 1.0)
                            * ((currentPosition - barycenter[i]) / euclidianDistance);
                    // * ((currentPosition - barycenter[i]) / euclidianDistance);
                }
                // * ((currentPosition - barycenter[i]) / euclidianDistance);
                if (position[i] > getProblem().getUpperBound(i) || position[i] < getProblem().getLowerBound(i)) {
                    position[i] = currentPosition;
                }
            }

            // if (getProblem().compareFitness(fish.getCurrentFitness(),
            // getProblem().getFitness(position))) {
            fish.updateCurrentPosition(position, getProblem().getFitness(position));

            if (getProblem().isFitnessBetterThan(fish.getBestFitness(), fish.getCurrentFitness())) {
                fish.updateBestPosition(fish.getCurrentPosition(), fish.getCurrentFitness());
            }
            // }
        }

        previousFishSchoolWeight = actualFishSchoolWeight;
    }

    private double[] calculateBarycenter() {
        double[] barycenter = new double[dimensions];
        double allWeightSum = 0;

        for (Fish fish : fishSchool) {
            allWeightSum += fish.getWeight();

            for (int i = 0; i < dimensions; i++) {
                barycenter[i] = barycenter[i] + (fish.getCurrentPosition()[i] * fish.getWeight());
            }
        }
        for (int i = 0; i < dimensions; i++) {
            barycenter[i] = barycenter[i] / allWeightSum;
        }
        return barycenter;
    }

    private double getMaxAbsDeltaFitness() {
        double maxAbsDeltaFitness = 0;
        for (Fish fish : fishSchool) {
            if (Math.abs(fish.getDeltaFitness()) > maxAbsDeltaFitness) {
                maxAbsDeltaFitness = Math.abs(fish.getDeltaFitness());
            }
        }
        if (maxAbsDeltaFitness == 0) {
            maxAbsDeltaFitness = 1;
        }
        return maxAbsDeltaFitness;
    }

    private double getFishSchoolWeigth() {
        double allWeightSum = 0;
        for (Fish fish : fishSchool) {
            allWeightSum += fish.getWeight();
        }
        return allWeightSum;
    }

    /**
     * Updates the global best fish based on the fish informed.<br>
     * If the fish informed has a better position them the actual global
     * best fish, it becomes the new global best fish.
     * 
     * @param fish The fish that will have the best position evaluated.
     */
    protected void calculateGBest(Fish fish) {
        double[] currFishFitness = fish.getBestPosition();

        double pBestFitness = getProblem().getFitness(currFishFitness);
        double gBestFitness = getProblem().getFitness(bestFishFitness);

        if (getProblem().isFitnessBetterThan(gBestFitness, pBestFitness)) {
            bestFishFitness = currFishFitness.clone();
        }
    }

    private double[] getInitialPosition() {
        double[] position = new double[this.dimensions];
        Random random = new Random(System.nanoTime());

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

    /**
     * {@inheritDoc}
     */
    public double[] getBestSolution() {
        return bestFishFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getBestSolutionValue() {
        return getProblem().getFitness(bestFishFitness);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Fish School Search";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "An implementation of the FSS algorithm.";
    }
}
