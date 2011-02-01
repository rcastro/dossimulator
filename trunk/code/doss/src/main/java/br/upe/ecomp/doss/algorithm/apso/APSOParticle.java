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
package br.upe.ecomp.doss.algorithm.apso;

import br.upe.ecomp.doss.algorithm.pso.PSOParticle;

/**
 * An specialization of the {@link PSOParticle} class for the APSO algorithm.
 * 
 * @author George Moraes
 */
public class APSOParticle extends PSOParticle {

    private double meanDistanceToOthersParticles;

    /**
     * Creates a new instance of this class.
     * 
     * @param dimensions The number of dimensions of the problem we are trying to solve.
     */
    public APSOParticle(int dimensions) {
        super(dimensions);
    }

    /**
     * Return the current mean distance to all others particles.
     * 
     * @return The current mean distance to all others particles.
     */
    public double getMeanDistanceToOthersParticles() {
        return meanDistanceToOthersParticles;
    }

    /**
     * Sets the current mean distance to all others particles.
     * 
     * @param meanDistanceToOthersParticles The current mean distance to all others particles.
     */
    public void setMeanDistanceToOthersParticles(double meanDistanceToOthersParticles) {
        this.meanDistanceToOthersParticles = meanDistanceToOthersParticles;
    }

}
