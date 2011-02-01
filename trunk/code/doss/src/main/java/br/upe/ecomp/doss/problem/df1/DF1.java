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
package br.upe.ecomp.doss.problem.df1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

public class DF1 extends Problem {

    private List<Peak> peakList;
    private DFParameters dfParameters;

    @Parameter(name = "Number of peaks")
    private int numPeaks;

    @Parameter(name = "Change step")
    private int changeStep;

    @Parameter(name = "Dimensions")
    private int dimensions;

    @Parameter(name = "Dynamic Position")
    private boolean dynamicPosition;

    @Parameter(name = "Dynamic Height")
    private boolean dynamicHeight;

    @Parameter(name = "Dynamic Slope")
    private boolean dynamicSlope;

    /**
     * Default constructor.
     */
    public DF1() {
    }

    /**
     * {@inheritDoc} This method needs to be called just after all required
     * parameters were configured.
     */
    public void init() {
        this.peakList = new ArrayList<Peak>(numPeaks);
        this.dfParameters = new DFParameters();
        this.dfParameters.setDimensions(dimensions);
        this.dfParameters.setDynamicX(dynamicPosition);
        this.dfParameters.setDynamicH(dynamicHeight);
        this.dfParameters.setDynamicR(dynamicSlope);
        createFunctions(dfParameters);
    }

    private void createFunctions(DFParameters dfParameters) {
        for (int i = 0; i < this.numPeaks; i++) {
            this.peakList.add(new Peak(dfParameters));
        }
    }

    public List<Peak> getPeakList() {
        return peakList;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "DF1. A dynamic benchmark problem.";
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "DF1";
    }

    /**
     * {@inheritDoc}
     */
    public int getDimensionsNumber() {
        return dfParameters.getDimensions();
    }

    /**
     * {@inheritDoc}
     */
    public double getLowerBound(int dimension) {
        return dfParameters.getXBase();
    }

    /**
     * {@inheritDoc}
     */
    public double getUpperBound(int dimension) {
        return dfParameters.getXBase() + dfParameters.getXRange();
    }

    /**
     * This is a maximization problem. {@inheritDoc}
     */
    public boolean isFitnessBetterThan(double bestSolutionFitness, double currentSolutionFitness) {
        return currentSolutionFitness > bestSolutionFitness;
    }

    /**
     * {@inheritDoc}
     */
    public double getFitness(double... solution) {
        List<Double> funcValues = new ArrayList<Double>();
        for (Peak df : this.peakList) {
            funcValues.add(df.getFunctionValue(solution));
        }
        return Collections.max(funcValues);
    }

    @Override
    public void update(Algorithm algorithm) {
        if (algorithm.getIterations() > 0 && algorithm.getIterations() % changeStep == 0) {
            for (Peak peak : peakList) {
                peak.changePeak(algorithm.getIterations());
            }
        }
    }

    public int getNumPeaks() {
        return numPeaks;
    }

    public void setNumPeaks(int numPeaks) {
        this.numPeaks = numPeaks;
    }

    public int getChangeStep() {
        return changeStep;
    }

    public void setChangeStep(int changeStep) {
        this.changeStep = changeStep;
    }

    public void setDynamicPosition(boolean dynamicPosition) {
        this.dynamicPosition = dynamicPosition;
    }

    public void setDynamicHeight(boolean dynamicHeight) {
        this.dynamicHeight = dynamicHeight;
    }

    public void setDynamicSlope(boolean dynamicSlope) {
        this.dynamicSlope = dynamicSlope;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

}
