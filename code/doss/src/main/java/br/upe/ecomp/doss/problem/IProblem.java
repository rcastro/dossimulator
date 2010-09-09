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

import br.upe.ecomp.doss.core.Configurable;

/**
 * Problem interface.
 * 
 * @author Rodrigo Castro
 */
public interface IProblem extends Configurable {

    /**
     * Returns the name of the problem.
     * 
     * @return The name of the problem.
     */
    String getName();

    /**
     * Returns the number of dimensions of this problem.
     * 
     * @return The number of dimensions of this problem.
     */
    int getDimensionsNumber();

    /**
     * Returns the lower limit of the search space for this problem in the given dimension.
     * 
     * @param dimension The dimension for which we want to know the lower limit.
     * @return The lower limit of the search space for this problem in the given dimension.
     */
    double getLowerLimit(int dimension);

    /**
     * Returns the upper limit of the search space for this problem in the given dimension.
     * 
     * @param dimension The dimension for which we want to know the upper limit.
     * @return The upper limit of the search space for this problem in the given dimension.
     */
    double getUpperLimit(int dimension);

    /**
     * Compares the bestSolutionFitness with the currentSolutionFitness and returns
     * <code>true</code> if currentSolutionFitness has a better fitness than bestSolutionFitness,
     * otherwise it returns <code>false</code>.
     * 
     * @param bestSolutionFitness The fitness of the best solution.
     * @param currentSolutionFitness The fitness of the current solution.
     * @return <code>true</code> if currentSolutionFitness has a better fitness than
     *         bestSolutionFitness, otherwise it returns <code>false</code>.
     */
    boolean compareFitness(double bestSolutionFitness, double currentSolutionFitness);

    /**
     * Calculates the fitness of the given solution.
     * 
     * @param solution The values of the solution in each dimension of the problem.
     * @return The fitness of the given solution.
     */
    double getFitness(double... solution);
}
