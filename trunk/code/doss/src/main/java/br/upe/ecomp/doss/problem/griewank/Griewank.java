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
package br.upe.ecomp.doss.problem.griewank;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

public class Griewank extends Problem {
   
    @Parameter(name = "Dimensions")
    private int dimensions;
    
    /**
     * Default constructor.
     */
    public Griewank(){
    }
    
    @Parameter(name = "Lower Bound")
    private double lowerBound;
    
    @Parameter(name = "Upper Bound")
    private double upperBound;
    
    @Override
    public void init() {
        // do nothing
    }

    @Override
    public int getDimensionsNumber() {
        return this.dimensions;
    }

    @Override
    public double getLowerBound(int dimension) {
        return this.lowerBound;
    }

    @Override
    public double getUpperBound(int dimension) {
        return this.upperBound;
    }

    @Override
    public boolean isFitnessBetterThan(double bestSolutionFitness, double currentSolutionFitness) {
        return currentSolutionFitness < bestSolutionFitness;
    }

    @Override
    public double getFitness(double... solution) {
        double fitness = 0;
        double auxSum = 0;
        double auxMul = 1;
        
        for (int i = 0; i < this.getDimensionsNumber(); i++) {
            auxSum += ((solution[i] * solution[i]) / 4000);
            auxMul *= Math.cos(solution[i] / Math.sqrt(i + 1));
        }
        
        fitness = 1 + auxSum - auxMul;
        
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
        return "Griewank";
    }

    @Override
    public String getDescription() {
        return "Problem Griewank to test the Bees Algorithms";
    }

}
