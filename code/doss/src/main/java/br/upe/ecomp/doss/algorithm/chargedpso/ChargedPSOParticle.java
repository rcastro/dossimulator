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
package br.upe.ecomp.doss.algorithm.chargedpso;

import java.util.Random;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.problem.Problem;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class ChargedPSOParticle extends PSOParticle {

    private double charge;

    /**
     * Creates a new instance of this class.
     * 
     * @param dimensions The number of dimensions of the problem we are trying to solve.
     */
    public ChargedPSOParticle(int dimensions) {
        super(dimensions);
    }

    /**
     * Updates the current velocity of the particle.
     * 
     * @param inertialWeight The inertia weight
     * @param bestParticleNeighborhood The best particle in the neighborhood
     * @param c1 The cognitive component
     * @param c2 The social component
     */
    public void updateVelocity(double inertialWeight, double[] bestParticleNeighborhood, double c1, double c2,
            double[] acceleration) {

        Random random = new Random();
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        double[] velocity = getVelocity();

        double[] pBest = getBestPosition();
        for (int i = 0; i < getDimensions(); i++) {
            velocity[i] = inertialWeight * velocity[i] + c1 * r1 * (pBest[i] - getCurrentPosition()[i]) + c2 * r2
                    * (bestParticleNeighborhood[i] - getCurrentPosition()[i]) + acceleration[i];
        }
        setVelocity(velocity);
    }

    /**
     * Updates the current position of the particle.
     * 
     * @param problem The problem that we are trying to solve.
     */
    @Override
    public void updateCurrentPosition(Problem problem) {
        RandomData random = new RandomDataImpl();
        double[] position = getCurrentPosition();
        double[] velocity = getVelocity();
        double newPosition;
        for (int i = 0; i < getDimensions(); i++) {
            double p = position[i];
            newPosition = position[i] + velocity[i];
            double p1 = newPosition;
            if (charge != 0) {
                double step;

                if (newPosition > problem.getUpperBound(i)) {
                    step = Math.abs(random.nextUniform(0, problem.getUpperBound(i) - problem.getLowerBound(i)));
                    newPosition = problem.getUpperBound(i) - step;
                } else if (newPosition < problem.getLowerBound(i)) {
                    step = Math.abs(random.nextUniform(0, problem.getUpperBound(i) - problem.getLowerBound(i)));
                    newPosition = problem.getLowerBound(i) + step;
                }

                if (newPosition > problem.getUpperBound(i)) {
                    System.out.println("parou");
                } else if (newPosition < problem.getLowerBound(i)) {
                    System.out.println("parou 1");
                }

            } else if (newPosition > problem.getUpperBound(i)) {
                newPosition = problem.getUpperBound(i);
            } else if (newPosition < problem.getLowerBound(i)) {
                newPosition = problem.getLowerBound(i);
            }
            if (newPosition > problem.getUpperBound(i)) {
                System.out.println("ops");
            }
            position[i] = newPosition;
        }
        updateCurrentPosition(position, problem.getFitness(position));

    }

    private double getRandomInRange(double value1, double value2) {
        RandomData random = new RandomDataImpl();
        double lowerBound;
        double upperBound;
        if (value1 < value2) {
            lowerBound = value1;
            upperBound = value2;
        } else {
            lowerBound = value2;
            upperBound = value1;
        }
        return random.nextUniform(lowerBound, upperBound);
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }
}
