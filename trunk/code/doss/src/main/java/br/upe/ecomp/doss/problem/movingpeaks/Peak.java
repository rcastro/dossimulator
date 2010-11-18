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

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 * Represents a peak of the Moving peaks benchmark.
 * 
 * @author Rodrigo Castro
 * 
 */
public class Peak {

    private static final double THRESHOLD = 0.7;

    private RandomData random;
    private double[] peakPosition;
    private double heigth;
    private double width;

    public void init(double heigth, double width, double... position) {
        random = new RandomDataImpl();

        this.heigth = heigth;
        this.width = width;
        this.peakPosition = position;
    }

    public void update(double[] positionIncrement, double[] lowerBound, double[] upperBound) {
        updateHeigth();
        updateWidth();
        updatePosition(positionIncrement, lowerBound, upperBound);
    }

    public double getFitness(double[] currentPosition) {
        double weightedDifference = 0;
        double difference;
        for (int i = 0; i < peakPosition.length; i++) {
            difference = currentPosition[i] - peakPosition[i];
            weightedDifference += width * Math.pow(difference, 2);
        }
        return heigth / (1 + weightedDifference);
    }

    private void updateHeigth() {
        heigth = heigth + 7 * random.nextGaussian(0, 1);
    }

    private void updateWidth() {
        width = width + 0.01 * random.nextGaussian(0, 1);
    }

    private void updatePosition(double[] positionIncrement, double[] lowerBound, double[] upperBound) {
        double newPosition;
        for (int i = 0; i < peakPosition.length; i++) {
            if (random.nextUniform(0, 1) >= THRESHOLD) {
                positionIncrement[i] *= -1;
            }
            newPosition = peakPosition[i] + positionIncrement[i];
            if (newPosition >= lowerBound[i] && newPosition <= upperBound[i]) {
                peakPosition[i] = newPosition;
            }
        }
    }
}
