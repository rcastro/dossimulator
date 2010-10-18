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

/**
 * Basic class that defines a Recorder.<br />
 * <br />
 * A recorder is the entity responsible for record the evolution of the algorithm execution. It will
 * be called at the end of each iteration of the algorithm. In its basic form, it can save the
 * position of each particle at the end of each iteration and the value of each measurement
 * registered to the algorithm.
 * 
 * @author Rodrigo Castro
 * 
 */
public interface IRecorder {

    /**
     * Initializes the record.
     * 
     * @param algorithm The algorithm for which this record is applied.
     */
    void init(Algorithm algorithm);

    /**
     * Updates the record based on the current state of the algorithm.
     * 
     * @param algorithm The algorithm for which this record is applied.
     */
    void update(Algorithm algorithm);

    /**
     * Ends the record.
     * 
     * @param algorithm The algorithm for which this record is applied.
     */
    void finalise(Algorithm algorithm);
}
