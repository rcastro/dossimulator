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
package br.upe.ecomp.doss.problem.schwefel;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

public class Schwefel12 extends Problem {

    @Parameter(name = "Dimesions")
    private int dimensions;
    
    /**
     * Default constructor.
     */
    public Schwefel12() {
        
    }
    
    @Override
    public void init() {
        // Do nothing
    }

    @Override
    public int getDimensionsNumber() {
        return this.dimensions;
    }

    @Override
    public double getLowerBound(int dimension) {
        return -100;
    }

    @Override
    public double getUpperBound(int dimension) {
        return 100;
    }

    @Override
    public boolean isFitnessBetterThan(double bestSolutionFitness, double currentSolutionFitness) {
        return bestSolutionFitness > currentSolutionFitness;
    }

    @Override
    public double getFitness(double... solution) {
        double fitness = 0;
        double auxFit = 0;
        
        for (int i = 0; i < this.getDimensionsNumber(); i++) {
            for (int j = 0; j <= i; j++) {
                auxFit += solution[j];
            }
            fitness += (auxFit * auxFit);
            auxFit = 0;
        }
        return fitness;
    }

    @Override
    public void update(Algorithm algorithm) {
        // Do nothing

    }

    @Override
    public int getChangeStep() {
        // Do nothing
        return 0;
    }

    @Override
    public String getName() {
        return "Schwefel 1.2";
    }

    @Override
    public String getDescription() {
        return "Problem Schwefel 1.2 to test the Bees Algorithms.";
    }

}
