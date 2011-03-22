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
package br.upe.ecomp.doss.problem.penalized;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

public abstract class Penalized extends Problem {

    @Parameter(name = "Dimensions")
    private int dimensions;
    
    /**
     * Set penalized for P8 or P16.
     */
    private Penalized penalized;

    /**
     * Default constructor.
     */
    public Penalized() {
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
        return -50;
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return 50;
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
        
        if (penalized instanceof PenalizedP8) {
            fitness = ((PenalizedP8)penalized).getFitness(solution);
        } else if (penalized instanceof PenalizedP16) {
            fitness = ((PenalizedP16)penalized).getFitness(solution);
        }
        
        return fitness;
    }

    protected double getFunctionU(double x, double a, int k, int m) {
        double u = 0;
        
        if (x > a) {
            u = k * Math.pow(x - a, m);
        } else if (x < -a) {
            u = k * Math.pow(x * (-1) - a, m);
        }
        
        return u;
    }
    
    protected double getFunctionY(double x) {
        double y = 0;
        
        y = 1 + ((x+1)/4);
        
        return y;
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

    public Penalized getPenalized() {
        return penalized;
    }

    public void setPenalized(Penalized penalized) {
        this.penalized = penalized;
    }
}
