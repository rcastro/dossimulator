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
package br.upe.ecomp.doss.main;

import java.util.ArrayList;
import java.util.List;

import br.upe.ecomp.doss.problem.IProblem;
import br.upe.ecomp.doss.problem.Problem1;
import br.upe.ecomp.doss.recorder.FileRecorder;
import br.upe.ecomp.doss.recorder.IRecorder;
import br.upe.ecomp.doss.stopCondition.IStopCondition;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;

/**
 * Main class for tests purposes.
 * 
 * @author Rodrigo Castro
 */
public class Main {

    /**
     * Main method.
     * 
     * @param args Possible arguments for the main class.
     */
    public static void main(String[] args) {
        MaximumIterationsStopCondition stopCondition = new MaximumIterationsStopCondition();
        stopCondition.setMaximumIterations(20);
        List<IStopCondition> stopConditions = new ArrayList<IStopCondition>();
        stopConditions.add(stopCondition);

        IProblem problem = new Problem1();
        IRecorder recorder = new FileRecorder();

        // Algorithm algorithm = new GlobalBestPSO(100, 20, 0.01, 0.01);
        // algorithm.setProblem(problem);
        // algorithm.setRecorder(recorder);
        // algorithm.setStopConditions(stopConditions);
        // algorithm.setMeasurements(new ArrayList<IMeasurement>());
        //
        // new Runner().runAlgorithm(algorithm);
    }
}
