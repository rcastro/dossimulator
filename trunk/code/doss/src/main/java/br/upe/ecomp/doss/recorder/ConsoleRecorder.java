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
package br.upe.ecomp.doss.recorder;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.measurement.IMeasurement;

/**
 * A console based implementation of {@link IRecorder}.
 * 
 * @author George Moraes
 */
public class ConsoleRecorder implements IRecorder {

	private String problemName;

	/**
	 * {@inheritDoc}
	 */
	public void init(Algorithm algorithm) {
		this.problemName = algorithm.getProblem().getName();
		printFileHeader();
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(Algorithm algorithm) {
		double[] position;

		printIterationHeader(algorithm.getIterations());

		Particle[] particles = algorithm.getParticles();
		for (Particle particle : particles) {
			position = particle.getCurrentPosition();
			printPosition(position);
		}

		printCurrentBestSolution(algorithm);
		printMeasurementHeader(algorithm.getIterations());

		for (IMeasurement measurement : algorithm.getMeasurements()) {
			printMeasurement(measurement);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void finalise() {
		System.out.println("End of output!");
	}

	private void printFileHeader() {
		System.out.println("Problem: " + problemName);
	}

	private void printMeasurementHeader(int iteration) {
		System.out.println("\nMeasurement(s) for iteration " + iteration);
	}

	private void printIterationHeader(int iteration) {
		System.out.println("\nIteration " + iteration);
	}

	private void printPosition(double[] position) {
		StringBuilder line = new StringBuilder(String.valueOf(position[0]));

		int length = position.length;
		for (int i = 1; i < length; i++) {
			line.append(" ");
			line.append(String.valueOf(position[i]));
		}

		System.out.println(line.toString());
	}

	private void printCurrentBestSolution(Algorithm algorithm) {
		System.out.print("Best solution: ");
		printPosition(algorithm.getBestSolution());
		System.out.println("Value: " + algorithm.getBestSolutionValue());
	}

	private void printMeasurement(IMeasurement measurement) {
		StringBuilder line = new StringBuilder(measurement.getName());
		line.append(":");
		line.append(measurement.getValue());
		line.append("\n");

		System.out.println(line.toString());
	}

}
