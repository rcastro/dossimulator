package br.upe.ecomp.doss.algorithm.pso.topology;

import br.upe.ecomp.doss.algorithm.clanpso.ClanPSO;
import br.upe.ecomp.doss.algorithm.pso.PSO;
import br.upe.ecomp.doss.algorithm.pso.PSOParticle;

public class ClanTopology implements ITopology {

    private ClanPSO clanPSO;

    /**
     * {@inheritDoc}
     */
    public PSOParticle getBestParticleNeighborhood(PSO pso, int index) {

        clanPSO = (ClanPSO) pso;

        PSOParticle particle = null;
        if (clanPSO.isLeadersConference()) {
            particle = getBestParticleFromLeaders(index);
        } else {
            particle = getBestParticleClan(index);
        }
        return particle;
    }

    private PSOParticle getBestParticleClan(int index) {
        int indexBestParticle = index;
        double bestParticleFitness = clanPSO.getParticles()[index].getBestFitness();
        double currentParticleFitness;

        int begginIndex = (index / clanPSO.getClansNumber()) * clanPSO.getClansNumber();
        int lastIndex = begginIndex + clanPSO.getClansNumber();
        for (int i = begginIndex; i < lastIndex; i++) {
            currentParticleFitness = clanPSO.getParticles()[i].getBestFitness();

            if (clanPSO.getProblem().isFitnessBetterThan(bestParticleFitness, currentParticleFitness)) {
                indexBestParticle = i;
                bestParticleFitness = currentParticleFitness;
            }
        }

        return (PSOParticle) clanPSO.getParticles()[indexBestParticle];
    }

    // Topology: star
    private PSOParticle getBestParticleFromLeaders(int index) {
        int indexBestParticle = index;
        double bestParticleFitness = clanPSO.getLeaders()[index].getBestFitness();
        double currentParticleFitness;

        for (int i = 0; i < clanPSO.getLeaders().length; i++) {
            currentParticleFitness = clanPSO.getLeaders()[i].getBestFitness();

            if (clanPSO.getProblem().isFitnessBetterThan(bestParticleFitness, currentParticleFitness)) {
                indexBestParticle = i;
                bestParticleFitness = currentParticleFitness;
            }
        }

        return clanPSO.getLeaders()[indexBestParticle];
    }
}
