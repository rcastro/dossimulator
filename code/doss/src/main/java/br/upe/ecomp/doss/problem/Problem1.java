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
package br.upe.ecomp.doss.problem;

import static java.lang.Math.pow;
import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * Problem 1 to test the implementation of the PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public class Problem1 extends Problem {

    private final Integer dimensions;

    /**
     * Default constructor.
     */
    public Problem1() {
        dimensions = 3;
    }

    /**
     * {@inheritDoc}
     */
    public void init() {

    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Problem 1";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Problem number 1 to test the PSO algorithm.";
    }

    /**
     * {@inheritDoc}
     */
    public int getDimensionsNumber() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerBound(int dimension) {
        return -5.12;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return 5.12;
    }

    /**
     * This is a minimization problem. {@inheritDoc}
     */
    public boolean compareFitness(double pBestFitness, double currentPositionFitness) {
        return currentPositionFitness < pBestFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getFitness(double... dimension) {
        double sphere = 0.0;
        for (int i = 0; i < dimension.length; i++) {
            sphere += pow(dimension[i], 2);
        }
        return sphere;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        // TODO Auto-generated method stub
    }
}
