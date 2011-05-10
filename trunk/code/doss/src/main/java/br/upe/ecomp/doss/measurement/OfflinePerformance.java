package br.upe.ecomp.doss.measurement;

import br.upe.ecomp.doss.algorithm.Algorithm;

public class OfflinePerformance extends Measurement {

    private double offlinePerformance;
    private double ft;
    private double ht;

    @Override
    public void init() {
        offlinePerformance = 0;
    }

    @Override
    public void update(Algorithm algorithm) {
        if (algorithm.getIterations() > 0 && algorithm.getIterations() % algorithm.getProblem().getChangeStep() == 0) {

        }
    }

    @Override
    public double getValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        return "Offline Performance";
    }

    @Override
    public String getDescription() {
        return "";
    }

}
