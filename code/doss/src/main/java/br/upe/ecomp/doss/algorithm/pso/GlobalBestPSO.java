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
package br.upe.ecomp.doss.algorithm.pso;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Global Best PSO algorithm.
 * 
 * @author Rodrigo Castro
 */
public class GlobalBestPSO extends PSO {

	public static final String SWARM_SIZE = "Swarm size";

	// The cognitive component
	public static final String C1 = "C1";
	// The social component
	public static final String C2 = "C2";

	private Map<String, Class<?>> parametersMap;

	/**
	 * Creates a new instance of this class.
	 */
	public GlobalBestPSO() {
		super();
		initParametersMap();
	}

	/**
	 * initializes the parameters map.
	 */
	private void initParametersMap() {
		parametersMap = new HashMap<String, Class<?>>();
		parametersMap.put(SWARM_SIZE, Integer.class);
		parametersMap.put(C1, Double.class);
		parametersMap.put(C2, Double.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "Global Best PSO";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return "An implementation of the global best PSO algorithm.";
	}

	/**
	 * {@inheritDoc}
	 */
	protected PSOParticle getBestParticleNeighborhood(int index) {
		int indexBestParticle = index;
		double bestParticleFitness = getProblem().getFitness(
				((PSOParticle) getParticles()[index]).getBestPosition());
		double currentParticleFitness;

		for (int i = 0; i < getSwarmSize(); i++) {
			currentParticleFitness = getProblem().getFitness(
					((PSOParticle) getParticles()[i]).getBestPosition());

			if (getProblem().compareFitness(bestParticleFitness, currentParticleFitness)) {
				indexBestParticle = i;
				bestParticleFitness = currentParticleFitness;
			}
		}

		return (PSOParticle) getParticles()[indexBestParticle];
	}

	@Override
	public void setParameterByName(String name, Object value) {
		if (name.equals(SWARM_SIZE)) {
			setSwarmSize((Integer) value);
		}
		if (name.equals(C1)) {
			setC1((Double) value);
		}
		if (name.equals(C2)) {
			setC2((Double) value);
		}
	}

	@Override
	public Object getParameterByName(String name) {
		if (name.equals(SWARM_SIZE)) {
			return getSwarmSize();
		}
		if (name.equals(C1)) {
			return getC1();
		}
		if (name.equals(C2)) {
			return getC2();
		}
		return null;
	}

}
