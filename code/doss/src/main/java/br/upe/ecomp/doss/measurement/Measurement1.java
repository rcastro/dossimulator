package br.upe.ecomp.doss.measurement;

import java.util.HashMap;
import java.util.Map;

import br.upe.ecomp.doss.algorithm.Algorithm;

/**
 * 
 * @author Rodrigo Castro
 */
public class Measurement1 implements IMeasurement {

    private Map<String, Class<?>> parametersMap;

    /**
     * Default constructor.
     */
    public Measurement1() {
        parametersMap = new HashMap<String, Class<?>>();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Measurement 1";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Description for measurement 1";
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Class<?>> getParametersMap() {
        return parametersMap;
    }

    /**
     * {@inheritDoc}
     */
    public void setParameterByName(String name, Object value) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public Object getParameterByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public double getValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}
