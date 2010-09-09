/**
 * Copyright (C) 2010
 * Rodrigo Castro
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

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.Configurable;

/**
 * Basic class that defines a Stop Condition.
 * 
 * @author Rodrigo Castro
 */
public interface IStopCondition extends Configurable {

    /**
     * Indicates if the stop conditions was reached.
     * 
     * @param algorithm The algorithm for which this stop condition is applied.
     * @return <code>true</code> if the stop condition was reached, otherwise returns
     *         <code>false</code>.
     */
    boolean isStop(Algorithm algorithm);
}
