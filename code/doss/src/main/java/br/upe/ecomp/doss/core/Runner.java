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
package br.upe.ecomp.doss.core;

import java.util.ArrayList;
import java.util.List;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.parser.AlgorithmXMLParser;
import br.upe.ecomp.doss.recorder.ChartRecorder;
import br.upe.ecomp.doss.recorder.FileRecorder;
import br.upe.ecomp.doss.view.ChartRunner;

/**
 * Class responsible for run the algorithms.
 * 
 * @author Rodrigo Castro
 */
public class Runner implements Runnable {

    private String filePath;
    private String fileName;
    private int numberSimulations;
    private boolean showSimulation;
    private List<RunnerListener> listeners;

    /**
     * Configures the Runner algorithm.
     * 
     * @param filePath The algorithm that we want to run.
     * @param fileName .
     * @param numberSimulations The number of simulations that will be executed.
     */
    public Runner(String filePath, String fileName, int numberSimulations, boolean showSimulation) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.numberSimulations = numberSimulations;
        this.showSimulation = showSimulation;

        this.listeners = new ArrayList<RunnerListener>();
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        Algorithm algorithm;
        for (int i = 0; i < numberSimulations; i++) {
            algorithm = AlgorithmXMLParser.read(filePath, fileName);

            if (!showSimulation) {
                algorithm.setRecorder(new FileRecorder());
                algorithm.setShowSimulation(false);
                algorithm.run();
            } else {
                algorithm.setRecorder(new ChartRecorder());
                algorithm.setShowSimulation(true);
                new ChartRunner().runChart(algorithm);
            }
            if (!showSimulation) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        for (RunnerListener listener : listeners) {
            listener.onSimulationFinish();
        }
    }

    public void addLitener(RunnerListener listener) {
        listeners.add(listener);
    }
}
