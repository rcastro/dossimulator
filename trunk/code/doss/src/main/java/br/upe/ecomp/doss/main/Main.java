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

import java.io.File;
import java.util.Arrays;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.chargedpso.ChargedGlobalBestPSO;
import br.upe.ecomp.doss.measurement.MeanFitness;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.RandomPeaks;
import br.upe.ecomp.doss.problem.df1.DF1;
import br.upe.ecomp.doss.problem.movingpeaks.MovingPeaks;
import br.upe.ecomp.doss.runner.Runner;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;
import br.upe.ecomp.doss.stopCondition.StopCondition;
import br.upe.ecomp.doss.util.CollectiveFitnessMeasure;

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
        int option = 2;

        switch (option) {
            case 0:
                run();
                break;
            case 1:
                runFromTestScenarioFile();
                break;
            case 2:
                printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes_variacao_carga/tipo_I/10_porcento",
                        "Charged PSO 10");
                printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes_variacao_carga/tipo_I/50_porcento",
                        "Charged PSO 50");
                printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes_variacao_carga/tipo_I/80_porcento",
                        "Charged PSO 80");
                printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes_variacao_carga/tipo_I/100_porcento",
                        "Charged PSO 100");

                // printCollectiveFitness(
                // "/Users/rodrigo/Documents/UPE/Cadeiras/2010.2/PFC/workspace/dosa/Charged_Global_Best_PSO",
                // "Charged PSO");
                // printCollectiveFitness("/Users/rodrigo/Documents/UPE/Cadeiras/2010.2/PFC/workspace/dosa/Clan_PSO",
                // "Clan PSO");
                // printCollectiveFitness(
                // "/Users/rodrigo/Documents/UPE/Cadeiras/2010.2/PFC/workspace/dosa/Global_Best_PSO",
                // "Global Best PSO");
                // printCollectiveFitness(
                // "/Users/rodrigo/Documents/UPE/Cadeiras/2010.2/PFC/workspace/dosa/Fish_School_Search - DF1",
                // "Fish School Search");
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private static void run() {
        Runner runner = new Runner(getAlgorithm(), false);
        runner.run();
    }

    private static void runFromTestScenarioFile() {
        Runner runner = new Runner("/Users/rodrigo/Desktop/Cenarios_de_Teste", "randompeaks_fss.xml", 1, true);
        runner.run();
    }

    private static void printCollectiveFitness(String path, String name) {
        File directory = new File(path);
        String measurement = "Mean Fitness";
        System.out.println(name + ": " + CollectiveFitnessMeasure.getCollectiveFitness(directory, measurement));
    }

    private static Algorithm getAlgorithm() {
        MovingPeaks problem = new MovingPeaks();
        problem.setChangeStep(50);
        problem.setMovingLength(0.9);

        RandomPeaks problem1 = new RandomPeaks();

        DF1 problem2 = new DF1();
        problem2.setChangeStep(50);
        problem2.setNumPeaks(2);

        StopCondition stopCondition = new MaximumIterationsStopCondition();
        ((MaximumIterationsStopCondition) stopCondition).setMaximumIterations(5000);

        Measurement measurement = new MeanFitness();

        ChargedGlobalBestPSO algorithm = new ChargedGlobalBestPSO();
        algorithm.setC1(2.0);
        algorithm.setC2(2.0);
        algorithm.setParticleCharge(16.0);
        algorithm.setSwarmSize(50);

        algorithm.setProblem(problem);
        algorithm.setStopConditions(Arrays.asList(stopCondition));
        algorithm.setMeasurements(Arrays.asList(measurement));

        return algorithm;
    }
}
