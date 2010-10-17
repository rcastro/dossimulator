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
public class Measurement1 extends Measurement {

    private Map<String, Class<?>> parametersMap;
    private Double fitness;

    /**
     * Default constructor.
     */
    public Measurement1() {
        parametersMap = new HashMap<String, Class<?>>();
        fitness = 0D;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Measurement 1";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Description for measurement 1";
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
        fitness = algorithm.getBestSolutionValue();
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        return fitness;
    }

    @Override
    public String toString() {
        return getName();
    }
}
