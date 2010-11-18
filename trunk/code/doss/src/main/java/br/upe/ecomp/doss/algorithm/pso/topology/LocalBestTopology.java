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
package br.upe.ecomp.doss.algorithm.pso.topology;

import br.upe.ecomp.doss.algorithm.pso.PSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;

/**
 * Implementation of the Local Best PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public class LocalBestTopology implements ITopology {

    /**
     * {@inheritDoc}
     */
    public PSOParticle getBestParticleNeighborhood(PSO pso, int index) {
        int indexBestParticle = index;
        int indexLeftNeighbor = (index > 0) ? index - 1 : pso.getSwarmSize() - 1;
        int indexRightNeighbor = (index < pso.getSwarmSize() - 1) ? index + 1 : 0;
        double best = 0.0;

        double currentParticlePBestFitness = pso.getProblem().getFitness(pso.getParticles()[index].getBestPosition());
        double leftNeighborParticlePBestFitness = pso.getProblem().getFitness(
                pso.getParticles()[indexLeftNeighbor].getBestPosition());
        double rightNeighborParticlePBestFitness = pso.getProblem().getFitness(
                pso.getParticles()[indexRightNeighbor].getBestPosition());

        best = currentParticlePBestFitness;

        if (pso.getProblem().isFitnessBetterThan(best, leftNeighborParticlePBestFitness)) {
            indexBestParticle = indexLeftNeighbor;
            best = leftNeighborParticlePBestFitness;
        }

        if (pso.getProblem().isFitnessBetterThan(best, rightNeighborParticlePBestFitness)) {
            indexBestParticle = indexRightNeighbor;
        }

        return (PSOParticle) pso.getParticles()[indexBestParticle];
    }
}