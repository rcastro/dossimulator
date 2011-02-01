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
import br.upe.ecomp.doss.algorithm.volitivepso.LocalVolitivePSO;
import br.upe.ecomp.doss.measurement.MeanFitness;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.df1.DF1;
import br.upe.ecomp.doss.runner.Runner;
import br.upe.ecomp.doss.stopCondition.MaximumIterationsStopCondition;
import br.upe.ecomp.doss.stopCondition.StopCondition;
import br.upe.ecomp.doss.util.CollectiveFitnessMeasure;

/**
 * Main class for experiments purposes.
 * 
 * @author George Moraes
 */
public class MainExperiments {

    /**
     * Main method.
     * 
     * @param args Possible arguments for the main class.
     */
    public static void main(String[] args) {
        int option = 0;

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

    }

    private static void run() {

        long initTime = System.currentTimeMillis();
        int simulationsNumber = 30;

        LocalVolitivePSO algorithm = (LocalVolitivePSO) getAlgorithm();
        double stepVolDecayPercentageInit = 0;
        double stepVolDecayPercentageFinal = 90;
        double stepVolInitPercentageInit = 30;
        double stepVolInitPercentageFinal = 100;

        for (double stepVolDecayPercent = stepVolDecayPercentageInit; stepVolDecayPercent <= stepVolDecayPercentageFinal; stepVolDecayPercent += 10) {
            for (double stepVolInitPercent = stepVolInitPercentageInit; stepVolInitPercent <= stepVolInitPercentageFinal; stepVolInitPercent += 10) {
                System.out.println("Decay: " + stepVolDecayPercent + "%");
                System.out.println("InitVol: " + stepVolInitPercent + "%");
                configureVolitivePSO(algorithm, stepVolDecayPercent, stepVolInitPercent);
                Runner runner = new Runner((LocalVolitivePSO) getAlgorithm(), simulationsNumber, false);
                runner.run();
                long finalTime = System.currentTimeMillis();
                double simulationTime = (finalTime - initTime) / 1000;
                System.out.println("Time: " + simulationTime + " sec.");
            }
        }
        long finalTime = System.currentTimeMillis();
        double totalTime = (finalTime - initTime) / 1000;
        System.out.println("Time: " + totalTime + " sec.");
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

        DF1 problem2 = getProblem();

        StopCondition stopCondition = getStopCondition();

        Measurement measurement = getMeasurement();

        LocalVolitivePSO algorithm = getAlgorithmLocalVolitivePSO();

        algorithm.setProblem(problem2);
        algorithm.setStopConditions(Arrays.asList(stopCondition));
        algorithm.setMeasurements(Arrays.asList(measurement));

        return algorithm;
    }

    private static LocalVolitivePSO getAlgorithmLocalVolitivePSO() {
        LocalVolitivePSO algorithm = new LocalVolitivePSO();

        // fixed parameters
        double c1 = 1.494;
        double c2 = 1.494;
        int swarmSize = 50;
        boolean linearDecay = true;
        double wScale = 500;
        double initialInertiaWeight = 0.9;
        double finalInertiaWeight = 0.4;

        // variable parameters
        double stepVolDecayPercentage = 90;
        double stepVolInitPercentage = 70;
        double stepVolFinalPercentage = 0.01;

        algorithm.setC1(c1);
        algorithm.setC2(c2);
        algorithm.setSwarmSize(swarmSize);
        algorithm.setInitialInertiaWeight(initialInertiaWeight);
        algorithm.setFinalInertiaWeight(finalInertiaWeight);
        algorithm.setLinearDecay(linearDecay);
        algorithm.setStepVolFinalPercentage(stepVolFinalPercentage);
        algorithm.setwScale(wScale);

        configureVolitivePSO(algorithm, stepVolDecayPercentage, stepVolInitPercentage);
        return algorithm;
    }

    private static void configureVolitivePSO(LocalVolitivePSO algorithm, double stepVolDecayPercentage,
            double stepVolInitPercentage) {
        algorithm.setStepVolDecayPercentage(stepVolDecayPercentage);
        algorithm.setStepVolInitPercentage(stepVolInitPercentage);
    }

    private static Measurement getMeasurement() {
        Measurement measurement = new MeanFitness();
        return measurement;
    }

    private static StopCondition getStopCondition() {
        int maximumIterations = 10001;

        StopCondition stopCondition = new MaximumIterationsStopCondition();
        ((MaximumIterationsStopCondition) stopCondition).setMaximumIterations(maximumIterations);
        return stopCondition;
    }

    private static DF1 getProblem() {
        int changeStep = 100;
        int numPeaks = 30;
        boolean dynamicPosition = true;
        boolean dynamicSlope = false;
        boolean dynamicHeight = false;
        int dimensions = 30;

        DF1 problem2 = new DF1();
        problem2.setChangeStep(changeStep);
        problem2.setNumPeaks(numPeaks);
        problem2.setDynamicPosition(dynamicPosition);
        problem2.setDynamicHeight(dynamicHeight);
        problem2.setDynamicSlope(dynamicSlope);
        problem2.setDimensions(dimensions);
        return problem2;
    }
}
