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
package br.upe.ecomp.doss.runner;

/**
 * <p>
 * The listener interface for notifying about the end of the simulation.
 * </p>
 * <p>
 * The class that is interested in being notified about the end of the simulation implements this
 * interface, and the object created with that class is registered with {@link Runner}, using the
 * Runner's <code>addLitener</code> method. When the simulation ends, that object's
 * <code>onSimulationFinish</code> method is invoked.
 * </p>
 * 
 * @author Rodrigo Castro
 */
public interface RunnerListener {

    /**
     * Invoked when the simulation ends.
     */
    void onSimulationFinish();
}
