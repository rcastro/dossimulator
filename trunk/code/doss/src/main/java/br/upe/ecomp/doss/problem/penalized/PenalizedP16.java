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
package br.upe.ecomp.doss.problem.penalized;

public class PenalizedP16 extends Penalized {

    /**
     * Default constructor.
     */
    public PenalizedP16() {
        super();
        setPenalized(this);
    }
    
    public double getFitness(double... solution) {
        double fitness = 0;
        double auxFit = 0;
        double auxSumY = 0;
        double auxSumU = 0;
        
        for (int i = 0; i < this.getDimensionsNumber() - 1; i++) {
            double p = 0;
            double s = 0;
            p = Math.pow((solution[i] - 1), 2);
            s = 1 + Math.pow(Math.sin(3 * Math.PI * solution[i+1]), 2);
            auxSumY += p * s;
        }
        
        double auxSin = Math.pow(Math.sin(3*Math.PI*solution[0]), 2);
        
        double auxP = Math.pow(solution[this.getDimensionsNumber()-1] - 1, 2)
        * (1 + Math.pow(Math.sin(2*Math.PI*solution[this.getDimensionsNumber()-1]), 2)); 
        
        auxFit = 0.1 * (auxSin + auxSumY + auxP);
        
        for (int i = 0; i < this.getDimensionsNumber(); i++) {
            auxSumU += getFunctionU(solution[i], 5, 100, 4);
        }
        
        fitness = auxFit + auxSumU;
        
        return fitness;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Penalized P16";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Penalized P16 problem.";
    }

}
