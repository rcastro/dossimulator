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
package br.upe.ecomp.doss.problem.ackley;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

/**
 * @author Sergio Ribeiro
 */
public class Ackley extends Problem {

    @Parameter(name = "Dimensions")
    private int dimensions;
    
    /**
     * Default constructor.
     */
    public Ackley() {
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
    public int getDimensionsNumber() {
        return this.dimensions;
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerBound(int dimension) {
        return -32;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return 32;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFitnessBetterThan(double bestSolutionFitness, double currentSolutionFitness) {
        return currentSolutionFitness < bestSolutionFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getFitness(double... solution) {
        double fitness = 0;
        double auxExp = 0;
        double auxCos = 0;
        
        for (int i = 0; i < this.getDimensionsNumber(); i++) {
            auxExp += solution[i] * solution[i];
            auxCos += Math.cos(2 * Math.PI * solution[i]);
        }
        
        auxExp = auxExp / this.getDimensionsNumber();
        auxExp = Math.sqrt(auxExp);
        auxExp = auxExp * (-0.2);
        
        auxCos = auxCos / this.getDimensionsNumber();
        
        fitness = (-1)*20*Math.exp(auxExp) - 1*Math.exp(auxCos) + 20 + Math.E;
        
        return fitness;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    public int getChangeStep() {
        // Do nothing
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Ackley";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Problem Ackley to test the Bees Algorithms";
    }

}
