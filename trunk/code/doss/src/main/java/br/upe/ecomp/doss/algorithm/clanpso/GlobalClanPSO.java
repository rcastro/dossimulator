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

import java.util.ArrayList;
import java.util.List;

import br.upe.ecomp.doss.algorithm.pso.GlobalBestPSO;
import br.upe.ecomp.doss.algorithm.pso.PSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;
import br.upe.ecomp.doss.algorithm.pso.topology.GlobalBestTopology;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Sergio Ribeiro
 */
public class GlobalClanPSO<T extends PSO, U extends PSOParticle> extends PSO implements ClanTypes<T, U> {

    private U[] leaders;
    private List<T> clans;

    private int clansNumber;

    @Parameter(name = "Particles per clan")
    private int particlesPerClan;

    /**
     * Creates a new instance of this class.
     */
    public GlobalClanPSO() {
        super();
        setTopology(new GlobalBestTopology());
    }

    @Override
    public String getName() {
        return "Global Clan PSO";
    }

    @Override
    public String getDescription() {
        return "An implementation of the Clan PSO algorithm.";
    }

    @Override
    public void init() {
        super.init();
        setParticles(new PSOParticle[getSwarmSize()]);
        clansNumber = getSwarmSize() / particlesPerClan;
        clans = new ArrayList<T>();
        for (int i = 0; i < clansNumber; i++) {
            T clan = psoTypePerClan();
            // Setting clan parameters
            clan.setProblem(getProblem());
            clan.setMeasurements(getMeasurements());
            clan.setStopConditions(getStopConditions());
            clan.setSwarmSize(particlesPerClan);
            clan.setInitialInertiaWeight(getInitialInertiaWeight());
            clan.setFinalInertiaWeight(getFinalInertiaWeight());
            clan.setLinearDecay(isLinearDecay());
            
            clan.setInitialC1(getInitialC1());
            clan.setFinalC1(getFinalC1());
            clan.setInitialC2(getInitialC2());
            clan.setFinalC2(getFinalC2());
            clan.setDecreaseC1increaseC2(isDecreaseC1increaseC2());

            clan.init();
            clans.add((T) clan);
        }

        int i = 0;
        for (T clan : clans) {
            for (PSOParticle particle : clan.getParticles()) {
                getParticles()[i] = particle;
                i++;
            }
        }

    }

    @Override
    public void iterate() {

        for (T clan : clans) {
            clan.setIterations(getIterations());
            clan.iterate();
        }
        delegateLeaders();
        performLeadersConference();
    }

    private void delegateLeaders() {
        leaders = particleType();
        for (int i = 0; i < clansNumber; i++) {
            leaders[i] = (U) clans.get(i).getBestParticle();
        }
    }

    private void performLeadersConference() {

        // Calculating the pbest and gbest positions
        // for (PSOParticle particle : leaders) {
        // particle.updatePBest(getProblem());
        //
        // // Stores the best particle's position.
        // calculateGBest(particle);
        // }

        // Executes PSO with leaders
        PSOParticle[] particlesTemp = getParticles();
        setParticles(leaders);
        setSwarmSize(leaders.length);

        super.iterate();

        // Retrieves the entire swarm
        setParticles(particlesTemp);
        setSwarmSize(particlesTemp.length);

    }

    public U[] particleType() {
        return (U[]) new PSOParticle[clansNumber];
    }

    public T psoTypePerClan() {
        return (T) new GlobalBestPSO();
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

    public U[] getLeaders() {
        return leaders;
    }

    public List<T> getClans() {
        return clans;
    }

}
