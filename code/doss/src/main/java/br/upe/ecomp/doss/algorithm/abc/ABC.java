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
package br.upe.ecomp.doss.algorithm.abc;

import org.apache.commons.math.random.MersenneTwister;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Sergio Ribeiro
 */
public class ABC extends Algorithm {

    @Parameter(name = "Population Size")
    private int populationSize;
    
    @Parameter(name = "Max Trial")
    private int maxTrial;
    
    private int foodCount;
    private int dimensions;
    
    private double[] solution;
    
    private FoodSource[] foodSources;
    
    /**
     * {@inheritDoc}
     */
    public void init() {
        
        dimensions = getProblem().getDimensionsNumber();
        foodCount = populationSize / 2;
        foodSources = new FoodSource[foodCount];
        
        initializePopulation();
    }

    /**
     * All food sources are initialized.
     */
    private void initializePopulation() {
        
        for (int i = 0; i < foodCount; i++) {
            initializeFoodSource(i);
        }
        solution = foodSources[0].getCurrentPosition();
    }
    
    /**
     * Each food source are initialized.
     */
    private void initializeFoodSource(int index) {
        
        MersenneTwister mt = new MersenneTwister(System.nanoTime());
        double randNumber;
        double fitness;
        double[] position = new double[dimensions];
        
        for (int i = 0; i < dimensions; i++)
        {
            randNumber = mt.nextDouble()*32767 / ((double)32767+(double)1);
            position[i] = randNumber * (getProblem().getUpperBound(dimensions) -
                    getProblem().getLowerBound(dimensions)) + getProblem().getLowerBound(dimensions);
        }
        
        fitness = getProblem().getFitness(position);
        foodSources[index] = new FoodSource(dimensions);
        foodSources[index].updateCurrentPosition(position, fitness);
        foodSources[index].updateBestPosition(position, fitness);
        foodSources[index].setCountStagnation(0);
    }
    
    /**
     * {@inheritDoc}
     */
    public void iterate() {
        sendEmployedBees();
        evaluateEmployedBees();
        sendOnlookerBees();
        sendScoutBees();
    }

    private void sendEmployedBees() {
        
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double[] tempFood;
        double tempFitness;

        for (int i = 0; i < foodCount; i++)
        {
            double rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
            int param2change = (int)(rand * dimensions);

            rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
            int neighbour = (int)(rand * foodCount);

            tempFood = new double[dimensions];
            for (int j = 0; j < dimensions; j++)
            {
                tempFood[j] = foodSources[i].getCurrentPosition()[j];
            }

            rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
            tempFood[param2change] = foodSources[i].getCurrentPosition()[param2change] +
                (foodSources[i].getCurrentPosition()[param2change] -
                    foodSources[neighbour].getCurrentPosition()[param2change]) * (rand - 0.5) * 2;

            if (tempFood[param2change] < getProblem().getLowerBound(dimensions))
                tempFood[param2change] = getProblem().getLowerBound(dimensions);
            if (tempFood[param2change] > getProblem().getUpperBound(dimensions))
                tempFood[param2change] = getProblem().getUpperBound(dimensions);

            tempFitness = getProblem().getFitness(tempFood);

            if (getProblem().isFitnessBetterThan(foodSources[i].getCurrentFitness(), tempFitness))
            {
                foodSources[i].updateCurrentPosition(tempFood, tempFitness);
                foodSources[i].updateBestPosition(tempFood, tempFitness);
                foodSources[i].setCountStagnation(0);
                
                double bestSolutionFitness = getProblem().getFitness(solution);
                if (getProblem().isFitnessBetterThan(bestSolutionFitness, tempFitness)) {
                    solution = tempFood;
                }
            }
            else
            {
                foodSources[i].setCountStagnation(foodSources[i].getCountStagnation() + 1);
            }
        }
    }
    
    private void evaluateEmployedBees() {
        
        double sumFit = 0;

        for (int i = 0; i < foodCount; i++)
            sumFit += foodSources[i].getCurrentFitness();

        for (int i = 0; i < foodCount; i++)
            foodSources[i].setProbabilitySelection(foodSources[i].getCurrentFitness() / sumFit);
    }
    
    private void sendOnlookerBees() {
        
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double[] tempFood;
        double tempFitness;
        
        int count = 0;
        int i = 0;
        
        while(count < foodCount) {
    
            double rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
            
            if (rand < foodSources[i].getProbabilitySelection()) {
                count++;
                
                rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
                int param2change = (int)(rand * dimensions);
                
                rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
                int neighbour = (int)(rand * foodCount);
                
                while (neighbour == i) {
                    rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
                    neighbour = (int)(rand * foodCount);
                }
                
                tempFood = new double[dimensions];
                for (int j = 0; j < dimensions; j++)
                {
                    tempFood[j] = foodSources[i].getCurrentPosition()[j];
                }
                
                rand = random.nextDouble() * 32767 / ((double)(32767) + (double)(1));
                tempFood[param2change] = foodSources[i].getCurrentPosition()[param2change] +
                    (foodSources[i].getCurrentPosition()[param2change] -
                        foodSources[neighbour].getCurrentPosition()[param2change]) * (rand - 0.5) * 2;

                if (tempFood[param2change] < getProblem().getLowerBound(dimensions))
                    tempFood[param2change] = getProblem().getLowerBound(dimensions);
                if (tempFood[param2change] > getProblem().getUpperBound(dimensions))
                    tempFood[param2change] = getProblem().getUpperBound(dimensions);

                tempFitness = getProblem().getFitness(tempFood);
    
                if (getProblem().isFitnessBetterThan(foodSources[i].getCurrentFitness(), tempFitness))
                {
                    foodSources[i].updateCurrentPosition(tempFood, tempFitness);
                    foodSources[i].updateBestPosition(tempFood, tempFitness);
                    foodSources[i].setCountStagnation(0);
                    
                    double bestSolutionFitness = getProblem().getFitness(solution);
                    if (getProblem().isFitnessBetterThan(bestSolutionFitness, tempFitness)) {
                        solution = tempFood;
                    }
                }
                else
                {
                    foodSources[i].setCountStagnation(foodSources[i].getCountStagnation() + 1);
                }
            }
            i++;
            if (i == foodCount)
                i = 0;
        }
    }
    
    private void sendScoutBees() {
        
        int maxtrialindex, i;
        maxtrialindex = 0;
        for (i = 1; i < foodCount; i++)
        {
            if (foodSources[i].getCountStagnation() > foodSources[maxtrialindex].getCountStagnation())
                maxtrialindex = i;
        }
        if (foodSources[maxtrialindex].getCountStagnation() >= maxTrial)
        {
            initializeFoodSource(maxtrialindex);
        }
    }
    
    public FoodSource[] getParticles() {
        return foodSources;
    }
    
    /**
     * {@inheritDoc}
     */
    public double[] getBestSolution() {
        return solution;
    }

    /**
     * {@inheritDoc}
     */
    public double getBestSolutionValue() {
        return getProblem().getFitness(solution);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "Artifcial Bee Colony";
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "An implementation of the Artificial Bee Colony.";
    }

}
