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

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.problem.Problem;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class Fish extends Particle {

    private double weight;
    private double[] previousPosition;
    private double deltaFitness;

    /**
     * Default constructor.
     */
    public Fish(int dimensions) {
        super(dimensions);
    }

    public void feed(double maxAbsDeltaFitness, double wScale) {
        weight = weight + (deltaFitness / maxAbsDeltaFitness);
        if (weight > wScale) {
            weight = wScale;
        } else if (weight < 1) {
            weight = 1;
        }
    }

    public void performIndividualMovement(double[] stepInd, Problem problem) {
        RandomData random = new RandomDataImpl();
        int dimensions = getDimensions();
        double[] currentPosition = getCurrentPosition().clone();

        double[] nextPosition = new double[getDimensions()];
        for (int i = 0; i < dimensions; i++) {
            nextPosition[i] = currentPosition[i] + random.nextUniform(-1.0, 1.0) * stepInd[i];
            if (nextPosition[i] > problem.getUpperBound(i) || nextPosition[i] < problem.getLowerBound(i)) {
                nextPosition[i] = currentPosition[i];
            }
        }

        deltaFitness = problem.getFitness(nextPosition) - getCurrentFitness();
        if (problem.isFitnessBetterThan(getCurrentFitness(), problem.getFitness(nextPosition)) && deltaFitness < 0) {
            deltaFitness *= -1;
        }

        if (problem.isFitnessBetterThan(getCurrentFitness(), problem.getFitness(nextPosition))) {
            previousPosition = getCurrentPosition().clone();
            updateCurrentPosition(nextPosition, problem.getFitness(nextPosition));

            if (problem.isFitnessBetterThan(getBestFitness(), getCurrentFitness())) {
                updateBestPosition(getCurrentPosition(), getCurrentFitness());
            }
        } else {
            deltaFitness = 0;
        }
    }

    public void updatePosition(double[] resultantDirection, Problem problem) {
        int dimensions = getDimensions();
        double[] nextPosition = getCurrentPosition().clone();
        double position;
        for (int i = 0; i < dimensions; i++) {
            position = nextPosition[i] + resultantDirection[i];
            if (position <= problem.getUpperBound(i) && position >= problem.getLowerBound(i)) {
                nextPosition[i] = position;
            }
        }
        // if (problem.compareFitness(getCurrentFitness(), problem.getFitness(nextPosition))) {
        updateCurrentPosition(nextPosition, problem.getFitness(nextPosition));
        if (problem.isFitnessBetterThan(getBestFitness(), getCurrentFitness())) {
            updateBestPosition(getCurrentPosition(), getCurrentFitness());
        }
        // }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDeltaFitness() {
        return deltaFitness;
    }

    public void setDeltaFitness(double deltaFitness) {
        this.deltaFitness = deltaFitness;
    }

    public double[] getPreviousPosition() {
        return previousPosition;
    }
}
