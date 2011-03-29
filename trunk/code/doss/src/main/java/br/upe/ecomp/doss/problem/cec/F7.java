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
package br.upe.ecomp.doss.problem.cec;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.problem.basiccec.Defaults;

/**
 * .
 * 
 * @author Sergio Ribeiro
 */
public class F7 extends Problem {

    @Parameter (name = "Dimensions")
    private int dimensions;
    
    private br.upe.ecomp.doss.problem.basiccec.F7 function;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        Defaults.setDefault_dim(dimensions);
        function = new br.upe.ecomp.doss.problem.basiccec.F7();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimensionsNumber() {
        return function.getDimension();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerBound(int dimension) {
        return function.getMin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperBound(int dimension) {
        return function.getMax();
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
        return function.compute(solution);
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
        return "F7";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Single-group Shifted m-dimensional Schwefel’s Problem 1.2";
    }

}
