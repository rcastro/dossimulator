/**
 * Copyright (C) 2010
 * Rodrigo Castro
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

import java.util.HashMap;
import java.util.Map;

/**
 * Problem 1 to test the implementation of the PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public class Problem1 implements IProblem {

    private Map<String, Class<?>> parametersMap;

    /**
     * Default constructor.
     */
    public Problem1() {
        parametersMap = new HashMap<String, Class<?>>();
        parametersMap.put("Dimensions", Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Test for Problem 1";
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
        return 4;
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerLimit(int dimension) {
        return -5.12;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperLimit(int dimension) {
        return 5.12;
    }

    /**
     * {@inheritDoc}
     */
    public boolean compareFitness(double pBestFitness, double currentPositionFitness) {
        return currentPositionFitness > pBestFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getFitness(double... dimension) {
        double result = 0;
        for (int i = 0; i < dimension.length - 1; i++) {
            result += dimension[i] * dimension[i] * (i + 1);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Class<?>> getParametersMap() {
        return parametersMap;
    }

    /**
     * {@inheritDoc}
     */
    public void setParameterByName(String name, Object value) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public Object getParameterByName(String name) {
        return 3;
    }

    @Override
    public String toString() {
        return getName();
    }
}
