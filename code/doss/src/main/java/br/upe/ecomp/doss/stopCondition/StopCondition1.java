package br.upe.ecomp.doss.stopCondition;

import java.util.HashMap;
import java.util.Map;

import br.upe.ecomp.doss.algorithm.Algorithm;

public class StopCondition1 implements IStopCondition {

    private Map<String, Class> parametersMap = new HashMap<String, Class>();

    @Override
    public String getName() {
        return "Stop condition 1";
    }

    @Override
    public String getDescription() {
        return "Stop condition 1 description";
    }

    @Override
    public Map<String, Class> getParametersMap() {
        return parametersMap;
    }

    @Override
    public void setParameterByName(String name, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getParameterByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isStop(Algorithm algorithm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
