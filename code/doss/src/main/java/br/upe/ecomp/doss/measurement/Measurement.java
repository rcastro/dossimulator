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
package br.upe.ecomp.doss.measurement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.Configurable;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * Basic class the defines a Measurement.
 * 
 * @author Rodrigo Castro
 * 
 */
public abstract class Measurement implements Configurable {

    /**
     * Updates this measure based on the current state of the algorithm.
     * 
     * @param algorithm The algorithm for which this measure is applied.
     */
    public abstract void update(Algorithm algorithm);

    /**
     * Returns the current value of this measure.
     * 
     * @return The current value of this measure.
     */
    public abstract double getValue();

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object == this) {
            equals = true;
        } else if (object instanceof StopCondition) {
            StopCondition instance = (StopCondition) object;
            equals = new EqualsBuilder().append(getName(), instance.getName()).isEquals();
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
