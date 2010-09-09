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
package br.upe.ecomp.doss.core;

import java.util.Map;

/**
 * Represents entities that are configurable without one knowing their parameters before runtime.
 * 
 * @author Rodrigo Castro
 */
public interface Configurable {

    /**
     * Returns the name of this entity.
     * 
     * @return The name of this entity.
     */
    String getName();

    /**
     * Returns the description of this entity.
     * 
     * @return The description of this entity.
     */
    String getDescription();

    /**
     * Returns a map containing the parameters name of this entity, if any, and their class type
     * respectively. <br>
     * <br>
     * For example, if we have a parameter for the name of the entity, we would have the pair:
     * ("name", String.class).
     * 
     * @return The parameters name and their class type.
     */
    Map<String, Class> getParametersMap();

    /**
     * Sets a parameter value by its name.
     * 
     * @param name The name of the parameter we want to change.
     * @param value The value we want to set.
     */
    void setParameterByName(String name, Object value);

    /**
     * Returns the value of the parameter that has the name specified.
     * 
     * @param name The name of the parameter.
     * @return The value of the parameter that has the name specified.
     */
    Object getParameterByName(String name);
}
