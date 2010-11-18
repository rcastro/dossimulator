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
package br.upe.ecomp.doss.problem.movingpeaks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

/**
 * Implementation of the Moving Peaks benchmark.
 * 
 * @author Rodrigo Castro
 */
public class MovingPeaks extends Problem {

    private RandomData random;
    private Peak[] peaks;

    private double[] peak1Parameters;
    private double[] peak2Parameters;
    private double[] peak3Parameters;
    private double[] peak4Parameters;
    private double[] peak5Parameters;

    private double[] lowerBound;
    private double[] upperBound;

    @Parameter(name = "Moving length")
    private double movingLength;

    @Parameter(name = "Change step")
    private int changeStep;

    /**
     * Default constructor.
     */
    public MovingPeaks() {

    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        random = new RandomDataImpl();

        setPeak1Parameters(new double[] { 0.1, 50, 8, 64, 67, 55, 4 });
        setPeak2Parameters(new double[] { 0.1, 50, 50, 13, 76, 15, 7 });
        setPeak3Parameters(new double[] { 0.1, 50, 9, 19, 27, 67, 24 });
        setPeak4Parameters(new double[] { 0.1, 50, 66, 87, 65, 19, 43 });
        setPeak5Parameters(new double[] { 0.1, 50, 76, 32, 43, 54, 65 });

        defineLimits();

        peaks = new Peak[5];
        for (int i = 0; i < 5; i++) {
            peaks[i] = new Peak();
        }

        peaks[0].init(peak1Parameters[0], peak1Parameters[1], Arrays.copyOfRange(peak1Parameters, 2, 7));
        peaks[1].init(peak2Parameters[0], peak2Parameters[1], Arrays.copyOfRange(peak2Parameters, 2, 7));
        peaks[2].init(peak3Parameters[0], peak3Parameters[1], Arrays.copyOfRange(peak3Parameters, 2, 7));
        peaks[3].init(peak4Parameters[0], peak4Parameters[1], Arrays.copyOfRange(peak4Parameters, 2, 7));
        peaks[4].init(peak5Parameters[0], peak5Parameters[1], Arrays.copyOfRange(peak5Parameters, 2, 7));

    }

    private void defineLimits() {
        lowerBound = new double[5];
        upperBound = new double[5];

        List<Double> positions = new ArrayList<Double>();
        for (int i = 2; i < 7; i++) {
            positions.add(peak1Parameters[i]);
            positions.add(peak2Parameters[i]);
            positions.add(peak3Parameters[i]);
            positions.add(peak4Parameters[i]);
            positions.add(peak5Parameters[i]);

            lowerBound[i - 2] = Collections.min(positions);
            upperBound[i - 2] = Collections.max(positions);

            positions.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Moving Peaks";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Moving peaks problem.";
    }

    /**
     * {@inheritDoc}
     */
    public int getDimensionsNumber() {
        return 5;
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerBound(int dimension) {
        return lowerBound[dimension];
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return upperBound[dimension];
    }

    /**
     * This is a minimization problem. {@inheritDoc}
     */
    public boolean isFitnessBetterThan(double pBestFitness, double currentPositionFitness) {
        return currentPositionFitness > pBestFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getFitness(double... position) {
        List<Double> fitness = new ArrayList<Double>();
        for (Peak peak : peaks) {
            fitness.add(peak.getFitness(position));
        }
        return Collections.max(fitness);
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        if (algorithm.getIterations() > 0 && algorithm.getIterations() % changeStep == 0) {
            for (Peak peak : peaks) {
                peak.update(getMovingVector(), lowerBound, upperBound);
            }
        }
    }

    private double[] getMovingVector() {
        double[] vector = new double[5];
        List<Integer> indexes = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4));
        double movingLengthFraction = movingLength / 5.0;

        int index;
        while (indexes.size() > 1) {
            index = indexes.remove(random.nextInt(0, indexes.size() - 1));
            vector[index] = random.nextUniform(0, movingLengthFraction);
        }
        vector[indexes.remove(0)] = movingLength - getLength(vector);

        return vector;
    }

    private double getLength(double[] vector) {
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        return sum;
    }

    public void setPeak1Parameters(double[] peak1Parameters) {
        this.peak1Parameters = peak1Parameters;
    }

    public void setPeak2Parameters(double[] peak2Parameters) {
        this.peak2Parameters = peak2Parameters;
    }

    public void setPeak3Parameters(double[] peak3Parameters) {
        this.peak3Parameters = peak3Parameters;
    }

    public void setPeak4Parameters(double[] peak4Parameters) {
        this.peak4Parameters = peak4Parameters;
    }

    public void setPeak5Parameters(double[] peak5Parameters) {
        this.peak5Parameters = peak5Parameters;
    }

    public void setChangeStep(int changeStep) {
        this.changeStep = changeStep;
    }

    public void setMovingLength(double movingLength) {
        this.movingLength = movingLength;
    }
}
