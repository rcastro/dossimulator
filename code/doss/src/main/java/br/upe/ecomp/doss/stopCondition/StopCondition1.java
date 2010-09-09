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
package br.upe.ecomp.doss.stopCondition;

import java.util.HashMap;
import java.util.Map;

import br.upe.ecomp.doss.algorithm.Algorithm;

public class StopCondition1 implements IStopCondition {

    private Map<String, Class<?>> parametersMap = new HashMap<String, Class<?>>();

    @Override
    public String getName() {
        return "Stop condition 1";
    }

    @Override
    public String getDescription() {
        return "Stop condition 1 description";
    }

    @Override
    public Map<String, Class<?>> getParametersMap() {
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
