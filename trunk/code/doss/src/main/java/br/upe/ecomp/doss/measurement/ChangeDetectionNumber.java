package br.upe.ecomp.doss.measurement;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.changedetection.EnvironmentChangeDetection;

public class ChangeDetectionNumber extends Measurement {

    private int changesNumber;

    @Override
    public void init() {
        changesNumber = 0;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        if (algorithm instanceof EnvironmentChangeDetection) {
            changesNumber = ((EnvironmentChangeDetection) algorithm).getChangesNumber();
        } else {
            changesNumber = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        return changesNumber;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Changes detection number";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "This is the changes detection number.";
    }

}
