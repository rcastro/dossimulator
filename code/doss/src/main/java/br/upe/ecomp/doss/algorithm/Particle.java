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
package br.upe.ecomp.doss.algorithm;

/**
 * Basic class that defines a Particle. This class should be extended for more specialized
 * algorithms. <br>
 * For example, for the PSO algorithm, we would want our particle to have a velocity property.
 * 
 * @author Rodrigo Castro
 */
public class Particle {

    private double[] bestPosition;
    private double bestFitness;
    private double currentFitness;
    private double[] currentPosition;
    private int dimensions;

    /**
     * Creates a new Particle.
     * 
     * @param dimensions The number of dimensions of the problem that we are trying to solve.
     */
    public Particle(int dimensions) {
        this.dimensions = dimensions;
        currentPosition = new double[dimensions];
    }

    /**
     * Returns the best position found by the particle.
     * 
     * @return The best position found by the particle.
     */
    public double[] getBestPosition() {
        return bestPosition;
    }

    /**
     * Updates the best position of this particle.
     * 
     * @param bestPosition The new best position of the particle.
     * @param bestPositionFitness The fitness of the new best position.
     */
    public void updateBestPosition(double[] bestPosition, double bestPositionFitness) {
        this.bestPosition = bestPosition;
        this.bestFitness = bestPositionFitness;
    }

    /**
     * Returns the fitness of the best position found by the particle.
     * 
     * @return The fitness of the best position found by the particle.
     */
    public double getBestFitness() {
        return bestFitness;
    }

    /**
     * Returns the fitness of the current position of this particle.
     * 
     * @return The fitness of the current position of this particle.
     */
    public double getCurrentFitness() {
        return this.currentFitness;
    }

    /**
     * Returns the current position of the particle.
     * 
     * @return The current position of the particle.
     */
    public double[] getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Updates the current position of this particle.
     * 
     * @param position The new position of the particle.
     * @param positionFitness The fitness of the new position.
     */
    public void updateCurrentPosition(double[] position, double positionFitness) {
        this.currentPosition = position;
        this.currentFitness = positionFitness;
    }

    /**
     * Returns the number of dimensions of the problem being solved.
     * 
     * @return The number of dimensions of the problem being solved.
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * Sets the number of dimensions of the problem that we are trying to solve.
     * 
     * @param dimensions The number of dimensions of the problem that we are trying to solve.
     */
    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }
}
