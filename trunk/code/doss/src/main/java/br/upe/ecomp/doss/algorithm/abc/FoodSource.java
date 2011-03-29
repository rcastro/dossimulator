/**
 * Copyright (C) 2011
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
package br.upe.ecomp.doss.algorithm.abc;

import br.upe.ecomp.doss.algorithm.Particle;

/**
 * .
 * 
 * @author Sergio Ribeiro
 */
public class FoodSource extends Particle {

    public FoodSource(int dimensions) {
        super(dimensions);
    }

    private int countStagnation;
    
    private double probabilitySelection;

    public int getCountStagnation() {
        return countStagnation;
    }

    public void setCountStagnation(int countStagnation) {
        this.countStagnation = countStagnation;
    }

    public double getProbabilitySelection() {
        return probabilitySelection;
    }

    public void setProbabilitySelection(double probabilitySelection) {
        this.probabilitySelection = probabilitySelection;
    }
}
