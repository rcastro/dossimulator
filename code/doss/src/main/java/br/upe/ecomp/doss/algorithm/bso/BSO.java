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
package br.upe.ecomp.doss.algorithm.bso;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.math.random.MersenneTwister;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
/**
 * 
 * @author Luma
 *
 */
public class BSO extends Algorithm{

    @Parameter(name = "Population Size")
    private int populationSize;
    
    @Parameter(name = "Max Trial")
    private int maxTrial;
    
    @Parameter(name = "Radius of search")
    private int radius;
    
    @Parameter(name = "Scout bees percentage")
    private double scoutPercentage;
    
    private int dimensions;
    private int numberScout = 0;
    private int numberOnlooker = 0;
    private int numberForager = 0;
    private double[] solution;
    private List<Bee> bees;
    
    
    @Override
    public void init() {
        bees = new ArrayList<Bee>(populationSize);
        dimensions = getProblem().getDimensionsNumber();
        numberScout =  (int) (populationSize*scoutPercentage);
        numberOnlooker = (populationSize - numberScout) / 2;
        numberForager = (populationSize - numberScout) / 2;
        System.out.println("Number of Scout: " + numberScout);
        System.out.println("Number of Onlookers: " + numberOnlooker);
        System.out.println("Number of Forager: " + numberForager);
        System.out.println("Bees of Size: " + bees.size());
        
        initializePopulation();
        
    }

    private void initializePopulation() 
    {
        for (int i = 0; i < populationSize; i++) {
            initializationPosition(i);
        }
        sortBees();
        for (int i = 0; i < bees.size(); i++) {
            System.out.println("Fitness of Bee(" + i + "):" + bees.get(i).getCurrentFitness());
        }
        setTypeBees();
        for (int i = 0; i < bees.size(); i++) {
            System.out.println("Type of Bee(" + i + "):" + bees.get(i).getType().getValue());
        }
        
    }

    private void setTypeBees() {
        for (int i = 0; i < numberScout; i++) {
            bees.get(i).setType(TypeBee.SCOUT);
        }
        if(numberOnlooker == populationSize - numberForager - numberScout)
        {
            for (int i = numberScout; i < populationSize - numberOnlooker; i++) {
                bees.get(i).setType(TypeBee.ONLOOKER);
            }
        }
        for (int i = numberScout + numberOnlooker; i < populationSize; i++) {
            bees.get(i).setType(TypeBee.FORAGER);
        }
        
    }

    private void sortBees() {
        Collections.sort(bees, new Comparator() {

            public int compare(Object o1, Object o2) {
                Bee a = (Bee) o1;
                Bee b = (Bee) o2;
                if(a.getCurrentFitness() < b.getCurrentFitness())
                    return -1;
                if(a.getCurrentFitness() > b.getCurrentFitness())
                    return 1;
                return 0;
            }
        });
        
    }

    private void initializationPosition(int bee) {
        double[] position = new double[dimensions];
        MersenneTwister random = new MersenneTwister(System.nanoTime());
        double fitness;
        
        for (int i = 0; i < dimensions; i++) {
            double value = random.nextDouble();
            position[i] = (getProblem().getUpperBound(i) - getProblem().getLowerBound(i)) * value
            + getProblem().getLowerBound(i);
            
            if (position[i] > getProblem().getUpperBound(i)) {
                position[i] = getProblem().getUpperBound(i);
            } else if (position[i] < getProblem().getLowerBound(i)) {
                position[i] = getProblem().getLowerBound(i);
            }
        }
        fitness = getProblem().getFitness(position);
        System.out.println("Current fitness in position: " + fitness);
        Bee beeAux = new Bee(dimensions);
        bees.add(bee, beeAux);
        bees.get(bee).updateCurrentPosition(position, fitness);
        bees.get(bee).updateBestPosition(position, fitness);
      
    }

    @Override
    public void iterate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double[] getBestSolution() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getBestSolutionValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        return "Bee Swarm Optimization";
    }

    @Override
    public String getDescription() {
        return "An implementation of the BSO algorithm.";
    }

}
