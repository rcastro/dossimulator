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

import java.util.ArrayList;
import java.util.List;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.exception.InfraException;
import br.upe.ecomp.doss.core.parser.AlgorithmXMLParser;
import br.upe.ecomp.doss.recorder.FileChartRecorder;
import br.upe.ecomp.doss.recorder.FileRecorder;

/**
 * Class responsible for run the algorithms.
 * 
 * @author Rodrigo Castro
 */
public class Runner implements Runnable {

    private static final int SLEEP_TIME = 2000;

    private String filePath;
    private String fileName;
    private int simulationsNumber;
    private boolean showSimulation;
    private Algorithm algorithm;
    private List<RunnerListener> listeners;

    private Runner(int simulationsNumber, boolean showSimulation) {
        this.listeners = new ArrayList<RunnerListener>();
        this.simulationsNumber = simulationsNumber;
        this.showSimulation = showSimulation;
    }

    /**
     * Configures this class.
     * 
     * @param algorithm The {@link Algorithm} instance that will be executed.
     * @param showSimulation Indicates if the simulation execution will be showed at real time.
     */
    public Runner(Algorithm algorithm, boolean showSimulation) {
        this(1, showSimulation);
        this.algorithm = algorithm;
    }

    /**
     * Configures this class.
     * 
     * @param filePath The path of the XML file representing the algorithm.
     * @param fileName The name of the XML file representing the algorithm.
     * @param simulationsNumber The number of simulations that will be executed.
     * @param showSimulation Indicates if the simulation execution will be showed at real time.
     */
    public Runner(String filePath, String fileName, int simulationsNumber, boolean showSimulation) {
        this(simulationsNumber, showSimulation);

        this.filePath = filePath;
        this.fileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        for (int i = 0; i < simulationsNumber; i++) {
            if (filePath != null && fileName != null) {
                algorithm = AlgorithmXMLParser.read(filePath, fileName);
            } else if (algorithm == null) {
                throw new InfraException(
                        "You have to inform either the Algorithm or the xml file of the test scenario.");
            }
            algorithm.setShowSimulation(showSimulation);

            // Initializes the problem here because other entities may rely on him to be initialized
            // correctly.
            algorithm.getProblem().init();

            if (!showSimulation) {
                algorithm.setRecorder(new FileRecorder());
                algorithm.run();
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                algorithm.setRecorder(new FileChartRecorder());
                new ChartRunner().runChart(algorithm);
            }
        }

        for (RunnerListener listener : listeners) {
            listener.onSimulationFinish();
        }
    }

    /**
     * Adds a listener that will be advised at the end of the simulations.
     * 
     * @param listener An instance of {@link RunnerListener}.
     */
    public void addLitener(RunnerListener listener) {
        listeners.add(listener);
    }
}
