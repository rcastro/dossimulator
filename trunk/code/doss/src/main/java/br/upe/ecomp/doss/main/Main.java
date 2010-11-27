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
        int option = 3;

        switch (option) {
            case 0:
                run();
                break;
            case 1:
                runFromTestScenarioFile();
                break;
            case 2:
                // printCollectiveFitnessExpirement1();
                printCollectiveFitnessExpirement2();
                break;
            case 3:
                printStandardDeviationExperiment1();
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private static void printStandardDeviationExperiment1() {
        System.out.println("Função DF1:");

        System.out.println("Tipo I:");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_I/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_I/Clan_PSO", "Clan_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_I/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo II:");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_II/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_II/Clan_PSO", "Clan_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_II/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo III:");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_III/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_III/Clan_PSO", "Clan_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_III/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("Função Moving Peaks:");

        System.out.println("\nTipo I:");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_I/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_I/Clan_PSO",
                "Clan_PSO");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_I/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo II:");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_II/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_II/Clan_PSO",
                "Clan_PSO");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_II/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo III:");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_III/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printStandardDeviation("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_III/Clan_PSO",
                "Clan_PSO");
        printStandardDeviation(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_III/Fish_School_Search",
                "Fish_School_Search");

    }

    private static void printStandardDeviation(String path, String name) {
        File directory = new File(path);
        String measurement = "Mean Fitness";
        System.out.println(name + ": " + CollectiveFitnessMeasure.calculateStandardDeviation(directory, measurement));
    }

    private static void printCollectiveFitnessExpirement1() {
        System.out.println("Função DF1:");

        System.out.println("Tipo I:");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_I/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_I/Clan_PSO", "Clan_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_I/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo II:");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_II/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_II/Clan_PSO", "Clan_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_II/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo III:");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_III/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_III/Clan_PSO", "Clan_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/DF1/tipo_III/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("Função Moving Peaks:");

        System.out.println("\nTipo I:");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_I/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_I/Clan_PSO",
                "Clan_PSO");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_I/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo II:");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_II/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_II/Clan_PSO",
                "Clan_PSO");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_II/Fish_School_Search",
                "Fish_School_Search");

        System.out.println("\nTipo III:");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_III/Charged_Local_Best_PSO",
                "Charged_Local_Best_PSO");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_III/Clan_PSO",
                "Clan_PSO");
        printCollectiveFitness(
                "/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_1/Moving Peaks/tipo_III/Fish_School_Search",
                "Fish_School_Search");
    }

    private static void printCollectiveFitnessExpirement2() {
        System.out.println("Função DF1");

        System.out.println("\nTipo I:");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_I/10_porcento",
                "10_porcento");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_I/50_porcento",
                "50_porcento");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_I/100_porcento",
                "100_porcento");

        System.out.println("\nTipo II:");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_II/10_porcento",
                "10_porcento");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_II/50_porcento",
                "50_porcento");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_II/100_porcento",
                "100_porcento");

        System.out.println("\nTipo III:");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_III/10_porcento",
                "10_porcento");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_III/50_porcento",
                "50_porcento");
        printCollectiveFitness("/Users/rodrigo/Desktop/simulacoes/estudo_de_caso_2/DF1/tipo_III/100_porcento",
                "100_porcento");
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
