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
package br.upe.ecomp.doss.algorithm.clanpso;

import br.upe.ecomp.doss.algorithm.pso.PSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.algorithm.pso.topology.ClanTopology;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class ClanPSO extends PSO {

    private boolean leadersConference;
    private PSOParticle[] leaders;

    private int clansNumber;

    @Parameter(name = "Particles per clan")
    private int particlesPerClan;

    /**
     * Creates a new instance of this class.
     */
    public ClanPSO() {
        super();
        setTopology(new ClanTopology());
    }

    @Override
    public String getName() {
        return "Clan PSO";
    }

    @Override
    public String getDescription() {
        return "An implementation of the Clan PSO algorithm.";
    }

    @Override
    public void iterate() {
        clansNumber = getSwarmSize() / particlesPerClan;
        super.iterate();
        delegateLeaders();
        performLeadersConference();
    }

    private void delegateLeaders() {
        leaders = new PSOParticle[clansNumber];
        int currentParticle = 0;
        for (int i = 0; i < clansNumber; i += 1) {
            leaders[i] = getTopology().getBestParticleNeighborhood(this, currentParticle);
            currentParticle += particlesPerClan;
        }
    }

    private void performLeadersConference() {
        leadersConference = true;

        // Calculating the pbest and gbest positions
        for (PSOParticle particle : leaders) {
            particle.updatePBest(getProblem());

            // Stores the best particle's position.
            calculateGBest(particle);
        }

        // Updating the velocity and position of all leaders
        for (int i = 0; i < leaders.length; i++) {
            PSOParticle particle = leaders[i];

            updateParticleVelocity(particle, i);
            particle.updateCurrentPosition(getProblem());
        }

        leadersConference = false;
    }

    public int getClansNumber() {
        return clansNumber;
    }

    public void setClansNumber(int clansNumber) {
        this.clansNumber = clansNumber;
    }

    public int getParticlesPerClan() {
        return particlesPerClan;
    }

    public boolean isLeadersConference() {
        return leadersConference;
    }

    public PSOParticle[] getLeaders() {
        return leaders;
    }
}
