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
package br.upe.ecomp.doss.algorithm.apso;

import java.util.Comparator;

import br.upe.ecomp.doss.algorithm.Particle;

/**
 * Compare particles by the current fitness value in ascending order.
 * 
 * @author George Moraes
 */
public class ComparatorMaximumFitness implements Comparator<Particle> {

    @Override
    public int compare(Particle particle1, Particle particle2) {
        if (particle1.getBestFitness() < particle2.getBestFitness()) {
            return -1;
        } else if (particle1.getBestFitness() > particle2.getBestFitness()) {
            return 1;
        } else {
            return 0;
        }
    }

}
