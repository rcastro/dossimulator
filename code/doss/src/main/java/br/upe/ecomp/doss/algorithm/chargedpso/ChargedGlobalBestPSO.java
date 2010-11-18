package br.upe.ecomp.doss.algorithm.chargedpso;

import br.upe.ecomp.doss.algorithm.pso.topology.GlobalBestTopology;

public class ChargedGlobalBestPSO extends ChargedPSO {

    /**
     * Creates a new instance of this class.
     */
    public ChargedGlobalBestPSO() {
        super();
        setTopology(new GlobalBestTopology());
    }

    @Override
    public String getName() {
        return "Charged Global Best PSO";
    }

    @Override
    public String getDescription() {
        return "An implementation of the Charged PSO algorithm.";
    }
}
