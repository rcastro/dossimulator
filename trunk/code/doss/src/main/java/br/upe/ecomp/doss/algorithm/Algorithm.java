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
package br.upe.ecomp.doss.algorithm;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.upe.ecomp.doss.core.Configurable;
import br.upe.ecomp.doss.measurement.Measurement;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.recorder.IRecorder;
import br.upe.ecomp.doss.stopCondition.StopCondition;

/**
 * Basic class that defines an algorithm.
 * 
 * @author Rodrigo Castro
 */
public abstract class Algorithm implements Runnable, Configurable {

    private Problem problem;
    private List<StopCondition> stopConditions;
    private List<Measurement> measurements;
    private Particle[] particles;
    private IRecorder recorder;
    private int iterations;
    private boolean showSimulation;

    /**
     * Makes the initial setup required for the begging of the algorithm
     * execution.
     */
    public abstract void init();

    /**
     * {@inheritDoc}
     */
    public void run() {
        init();
        recorder.init(this);
        iterations = 1;
        do {
            iterate();
            for (Measurement measurement : measurements) {
                measurement.update(this);
            }
            recorder.update(this);
            iterations += 1;
            if (showSimulation) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (!isStop());

        // We increment iterations before the iteration actually occur
        iterations = iterations - 1;
        recorder.finalise(this);
    }

    /**
     * Verifies if any of the stop conditions registered to the current
     * execution of this algorithm was reached.<br />
     * The stop conditions are checked in the order they were registered.
     * 
     * @return <code>true</code> if any stop condition was reached, otherwise
     *         returns <code>false</code>.
     */
    public boolean isStop() {
        boolean result = false;
        for (StopCondition stopCondition : stopConditions) {
            result = stopCondition.isStop(this);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * Executes one iteration of the algorithm. This is the main method of the
     * algorithm, here should go all the logic of its execution.
     */
    public abstract void iterate();

    /**
     * Returns the particles participating of the current execution.
     * 
     * @return The particles participating of the current execution.
     */
    public Particle[] getParticles() {
        return particles;
    }

    /**
     * Returns the best solution of the current iteration.
     * 
     * @return the best solution of the current iteration.
     */
    public abstract double[] getBestSolution();

    /**
     * Returns the value of best solution of the current iteration.
     * 
     * @return the value of best solution of the current iteration.
     */
    public abstract double getBestSolutionValue();

    /**
     * Sets the particles that will participate of the execution of this
     * algorithm.
     * 
     * @param particles The particles that will participate of the execution of
     *            this algorithm.
     */
    public void setParticles(Particle[] particles) {
        this.particles = particles;
    }

    /**
     * Returns the number of the current iteration.
     * 
     * @return The number of the current iteration.
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Sets the current iteration number.
     * 
     * @param iterations The current iteration number.
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Returns the current problem being solved.
     * 
     * @return The current problem being solved.
     */
    public Problem getProblem() {
        return problem;
    }

    /**
     * Sets the problem to be solved.
     * 
     * @param problem The problem to be solved.
     */
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    /**
     * Returns the list of stop conditions registered to the current execution
     * of this algorithm.
     * 
     * @return The list of stop conditions registered to the current execution
     *         of this algorithm.
     */
    public List<StopCondition> getStopConditions() {
        return stopConditions;
    }

    /**
     * Sets the list of stop conditions of this algorithm.
     * 
     * @param stopConditions The list of stop conditions of this algorithm.
     */
    public void setStopConditions(List<StopCondition> stopConditions) {
        this.stopConditions = stopConditions;
    }

    /**
     * Returns the list of measurements registered to the current execution of
     * this algorithm.
     * 
     * @return The list of measurements registered to the current execution of
     *         this algorithm.
     */
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    /**
     * Sets the list of measurement for this algorithm.
     * 
     * @param measurements The list of measurement for this algorithm.
     */
    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    /**
     * Returns the recorder instance registered to save the results of this
     * algorithm after each iteration.
     * 
     * @return The recorder instance registered to save the results of this
     *         algorithm after each iteration.
     */
    public IRecorder getRecorder() {
        return recorder;
    }

    /**
     * Sets the recorder instance that will be used to save the results of this
     * algorithm after each iteration.
     * 
     * @param recorder The recorder instance that will be used to save the
     *            results of this algorithm after each iteration.
     */
    public void setRecorder(IRecorder recorder) {
        this.recorder = recorder;
    }

    public boolean isShowSimulation() {
        return showSimulation;
    }

    public void setShowSimulation(boolean showSimulation) {
        this.showSimulation = showSimulation;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    public abstract String getName();

    /**
     * {@inheritDoc}
     */
    public abstract String getDescription();

    /**
     * {@inheritDoc}
     */
    public abstract Map<String, Class<?>> getParametersMap();

    /**
     * {@inheritDoc}
     */
    public abstract void setParameterByName(String name, Object value);

    /**
     * {@inheritDoc}
     */
    public abstract Object getParameterByName(String name);

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object == this) {
            equals = true;
        } else if (object instanceof Algorithm) {
            Algorithm instance = (Algorithm) object;
            equals = new EqualsBuilder().append(getName(), instance.getName()).isEquals();
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }
}