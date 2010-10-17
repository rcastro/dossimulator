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
package br.upe.ecomp.doss.measurement;

import java.util.HashMap;
import java.util.Map;

import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * 
 * @author Rodrigo Castro
 */
public class MeanFitness extends Measurement {

    private Map<String, Class<?>> parametersMap;
    private double sumBestFitness;
    private double mean;

    /**
     * Default constructor.
     */
    public MeanFitness() {
        sumBestFitness = 0;
        mean = 0;
        parametersMap = new HashMap<String, Class<?>>();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Mean Fitness";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Mean Fitness is the average over all previous fitness values. "
                + "This measurement is a representative performance measurement in "
                + "a dynamic environment, becouse it reflects algorithm performance "
                + "across the entire range of landscape dynamics.";
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        sumBestFitness += algorithm.getBestSolutionValue();
        mean = sumBestFitness / algorithm.getIterations();
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        return mean;
    }

    @Override
    public String toString() {
        return getName();
    }
}
