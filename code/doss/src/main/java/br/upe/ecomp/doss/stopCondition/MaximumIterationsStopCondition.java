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
package br.upe.ecomp.doss.stopCondition;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * Stops the execution of the algorithm after a predefined number of iterations.
 * 
 * @author Rodrigo Castro
 */
public class MaximumIterationsStopCondition extends StopCondition {

    @Parameter(name = "Maximum iterations")
    private int maximumIterations;

    /**
     * Creates a new instance of this class.
     */
    public MaximumIterationsStopCondition() {
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStop(Algorithm algorithm) {
        return algorithm.getIterations() > maximumIterations;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Maximum number of iterarions";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Stops the execution of the algorithm after the given number of iterations.";
    }

    /**
     * Returns the maximum number of iterations of the algorithm.
     * 
     * @return The maximum number of iterations of the algorithm.
     */
    public int getMaximumIterations() {
        return maximumIterations;
    }

    /**
     * Sets the maximum number of iterations of the algorithm.
     * 
     * @param maximumIterations The maximum number of iterations of the
     *            algorithm.
     */
    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }
}
