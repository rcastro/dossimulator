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
package br.upe.ecomp.doss.core.exception;

/**
 * Represents a infrastructure exception.
 * 
 * @author Rodrigo Castro
 */
public class InfraException extends RuntimeException {

    /**
     * Constructs a new infrastructure exception with the specified error message.
     * 
     * @param message The error message.
     */
    public InfraException(String message) {
        this(message, null);
    }

    /**
     * Constructs a new infrastructure exception with the specified cause.
     * 
     * @param cause The cause.
     */
    public InfraException(Throwable cause) {
        this(null, cause);
    }

    /**
     * Constructs a new infrastructure exception with the specified error message and cause.
     * 
     * @param message The error message.
     * @param cause The cause.
     */
    public InfraException(String message, Throwable cause) {
        super(message, cause);
    }
}
