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

/**
 * Represents the fuzzy membership function of APSO algorithm.
 * 
 * @author George Moraes
 */
public class FuzzyMembershipFunction {

    private double convergence = 0;
    private double exploitation = 0;
    private double exploration = 0;
    private double jumpingOut = 0;

    public FuzzyMembershipFunction(double convergence, double exploitation, double exploration, double jumpingOut) {
        this.convergence = convergence;
        this.exploitation = exploitation;
        this.exploration = exploration;
        this.jumpingOut = jumpingOut;
    }

    public void fuzzification(double evolFactor) {
        if (evolFactor < 0 || evolFactor > 1 || evolFactor == Double.NaN) {
            System.out.println("Evolutionary Factor out of bounds: " + evolFactor);
        }

        // Convergence
        if (0.0 <= evolFactor && evolFactor <= 0.1)
            convergence = 1;
        if (0.1 < evolFactor && evolFactor <= 0.3)
            convergence = -5 * evolFactor + 1.5;
        if (0.3 < evolFactor && evolFactor <= 1.0)
            convergence = 0;

        // Exploitation
        if (0.0 <= evolFactor && evolFactor <= 0.2)
            exploration = 0;
        if (0.2 < evolFactor && evolFactor <= 0.3)
            exploration = 10 * evolFactor - 2;
        if (0.3 < evolFactor && evolFactor <= 0.4)
            exploration = 1;
        if (0.4 < evolFactor && evolFactor <= 0.6)
            exploration = -5 * evolFactor + 3;
        if (0.6 < evolFactor && evolFactor <= 1.0)
            exploration = 0;

        // Exploration
        if (0.0 <= evolFactor && evolFactor <= 0.4)
            exploration = 0;
        if (0.4 < evolFactor && evolFactor <= 0.6)
            exploration = 5 * evolFactor - 2;
        if (0.6 < evolFactor && evolFactor <= 0.7)
            exploration = 1;
        if (0.7 < evolFactor && evolFactor <= 0.8)
            exploration = -10 * evolFactor + 8;
        if (0.8 < evolFactor && evolFactor <= 1.0)
            exploration = 0;

        // Jumping out
        if (0.0 <= evolFactor && evolFactor <= 0.7)
            jumpingOut = 0;
        if (0.7 < evolFactor && evolFactor <= 0.9)
            jumpingOut = 5 * evolFactor - 3.5;
        if (0.9 < evolFactor && evolFactor <= 1.0)
            jumpingOut = 1;
    }

    public EnumEvolutionaryState defuzzification() {
        if (convergence > exploitation && convergence > exploration && convergence > jumpingOut) {
            return EnumEvolutionaryState.CONVERGENCE;
        } else if (exploitation > exploration && exploitation > convergence && exploitation > jumpingOut) {
            return EnumEvolutionaryState.EXPLOITATION;
        } else if (jumpingOut > exploitation && jumpingOut > convergence && jumpingOut > exploration) {
            return EnumEvolutionaryState.JUMPING_OUT;
        } else {
            return EnumEvolutionaryState.EXPLORATION;
        }

    }
}
