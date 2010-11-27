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

/**
 * Represents a peak of the Moving peaks benchmark.
 * 
 * @author Rodrigo Castro
 * 
 */
public class Peak {

    private static final double THRESHOLD = 0.6;

    private RandomData random;
    private double[] peakPosition;
    private double heigth;
    private double width;
    private double heigthSeverity;
    private double widthSeverity;

    public Peak(RandomData random) {
        this.random = random;
    }

    public void init(double heigthSeverity, double widthSeverity, double heigth, double width, double... position) {
        // random = new RandomDataImpl();

        this.heigthSeverity = heigthSeverity;
        this.widthSeverity = widthSeverity;
        this.heigth = heigth;
        this.width = width;
        this.peakPosition = position;
    }

    public void update(double[] positionIncrement, double lowerBound, double upperBound) {
        updateHeigth();
        updateWidth();
        updatePosition(positionIncrement, lowerBound, upperBound);
    }

    public double getFitness(double[] currentPosition) {
        double difference = 0;
        for (int i = 0; i < peakPosition.length; i++) {
            difference += Math.pow(currentPosition[i] - peakPosition[i], 2);
        }
        return heigth / (1 + width * difference);
    }

    private void updateHeigth() {
        heigth = Math.abs(heigth + heigthSeverity * random.nextGaussian(0, 1));
    }

    private void updateWidth() {
        width = Math.abs(width + widthSeverity * random.nextGaussian(0, 1));
    }

    private void updatePosition(double[] positionIncrement, double lowerBound, double upperBound) {
        double newPosition;
        for (int i = 0; i < peakPosition.length; i++) {
            if (random.nextUniform(0, 1) >= THRESHOLD) {
                positionIncrement[i] *= -1;
            }
            newPosition = peakPosition[i] + positionIncrement[i];
            // if (newPosition >= lowerBound && newPosition <= upperBound) {
            // peakPosition[i] = newPosition;
            // }
            peakPosition[i] = newPosition;
        }

        // for (int i = 0; i < peakPosition.length; i++) {
        // // position[i] = RandomUtils.generateRandom(100);
        // peakPosition[i] = random.nextUniform(0, 100);
        // }
    }
}
