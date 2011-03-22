/**
 * Copyright (C) 2011
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

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Sergio Ribeiro
 */
public class Sphere extends Problem {

    @Parameter (name = "Dimensions")
    private int dimensions;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // Do nothing

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimensionsNumber() {
        return dimensions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerBound(int dimension) {
        return -100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperBound(int dimension) {
        return 100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFitnessBetterThan(double bestSolutionFitness, double currentSolutionFitness) {
        return currentSolutionFitness < bestSolutionFitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getFitness(double... solution) {
        
        double fitness = 0;
        
        for (int i = 0; i < dimensions; i++) {
            
            fitness += solution[i] * solution[i];
        }
        
        return fitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Algorithm algorithm) {
        // Do nothing

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChangeStep() {
        // Do nothing
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Sphere";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Sphere Function";
    }

}
