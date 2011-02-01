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
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.random.MersenneTwister;
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
    private List<Peak> peaks;

    @Parameter(name = "Moving length")
    private double movingLength;

    @Parameter(name = "Change step")
    private int changeStep;

    @Parameter(name = "Correlation coeficient")
    private double correlationCoeficient;

    @Parameter(name = "Heigth severity")
    private double heigthSeverity;

    @Parameter(name = "Width severity")
    private double widthSeverity;

    @Parameter(name = "Dimensions number")
    private int dimensions;

    /**
     * Default constructor.
     */
    public MovingPeaks() {

    }

    private void initPeak(Peak peak) {
        int heigth = random.nextInt(50, 60); // 50;
        double width = 0.001;
        double[] position = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            position[i] = random.nextUniform(0, 100);
        }
        peak.init(heigthSeverity, widthSeverity, heigth, width, position.clone());
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        random = new RandomDataImpl();

        Peak peak;
        peaks = new ArrayList<Peak>();
        for (int i = 0; i < 10; i++) {
            peak = new Peak(random);
            initPeak(peak);
            peaks.add(peak);
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
        return dimensions;
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerBound(int dimension) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return 100;
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
                peak.update(getMovingVector(), 0, 100);
            }
        }
    }

    private double[] getMovingVector() {
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double[] vector = new double[dimensions];
        double movingLengthFraction = movingLength / dimensions;
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < dimensions; i++) {
            indexes.add(i);
        }

        int index;
        while (indexes.size() > 1) {
            index = indexes.remove(random.nextInt(indexes.size() - 1));
            vector[index] = movingLengthFraction * random.nextDouble();
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

    @Override
    public int getChangeStep() {
        return this.changeStep;
    }

    public void setChangeStep(int changeStep) {
        this.changeStep = changeStep;
    }

    public void setMovingLength(double movingLength) {
        this.movingLength = movingLength;
    }

}
