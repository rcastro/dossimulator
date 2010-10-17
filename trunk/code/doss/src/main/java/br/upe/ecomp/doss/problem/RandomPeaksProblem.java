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

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class RandomPeaksProblem extends Problem {

    private Map<String, Class<?>> parametersMap;

    /**
     * Default constructor.
     */
    public RandomPeaksProblem() {
        parametersMap = new HashMap<String, Class<?>>();
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Random Peaks";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Problem random peaks to test the PSO algorithm.";
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
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return 30;
    }

    /**
     * {@inheritDoc}
     */
    public boolean compareFitness(double pBestFitness, double currentPositionFitness) {
        return currentPositionFitness < pBestFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getFitness(double... dimension) {
        Double x = dimension[0];
        Double y = dimension[1];

        return +5 * Math.exp(-0.1 * ((x - 15) * (x - 15) + (y - 20) * (y - 20))) - 2
                * Math.exp(-0.08 * ((x - 20) * (x - 20) + (y - 15) * (y - 15))) + 3
                * Math.exp(-0.08 * ((x - 25) * (x - 25) + (y - 10) * (y - 10))) + 2
                * Math.exp(-0.1 * ((x - 10) * (x - 10) + (y - 10) * (y - 10))) - 2
                * Math.exp(-0.5 * ((x - 5) * (x - 5) + (y - 10) * (y - 10))) - 4
                * Math.exp(-0.1 * ((x - 15) * (x - 15) + (y - 5) * (y - 5))) - 2
                * Math.exp(-0.5 * ((x - 8) * (x - 8) + (y - 25) * (y - 25))) - 2
                * Math.exp(-0.5 * ((x - 21) * (x - 21) + (y - 25) * (y - 25))) + 2
                * Math.exp(-0.5 * ((x - 25) * (x - 25) + (y - 16) * (y - 16))) + 2
                * Math.exp(-0.5 * ((x - 5) * (x - 5) + (y - 14) * (y - 14)));
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
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    public Object getParameterByName(String name) {
        // Do nothing.
        return null;
    }
}
