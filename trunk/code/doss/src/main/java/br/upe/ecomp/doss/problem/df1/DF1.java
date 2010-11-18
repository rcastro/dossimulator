package br.upe.ecomp.doss.problem.df1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.core.annotation.Parameter;
import br.upe.ecomp.doss.problem.Problem;

public class DF1 extends Problem {

    private static final String AH = "aH"; // coeficiente da funcao logistica
    private static final String AR = "aR"; // coeficiente da funcao logistica
    private static final String AX = "aX"; // coeficiente da funcao logistica
    private static final String XBASE = "xBase";
    private static final String XRANGE = "xRange";
    private static final String XSCALE = "xScale";
    private static final String HBASE = "hBase";
    private static final String HRANGE = "hRange";
    private static final String HSCALE = "hScale";
    private static final String RBASE = "rBase";
    private static final String RRANGE = "rRange";
    private static final String RSCALE = "rScale";
    private static final String IS_DYNAMIC_H = "dynamicH"; // mudanca na altura
                                                           // do pico?
    private static final String IS_DYNAMIC_R = "dynamicR"; // mudanca na
                                                           // inclinacao do
                                                           // pico?
    private static final String IS_DYNAMIC_X = "dynamicX"; // mudanca na posicao
                                                           // do pico?
    private static final String NUMPEAKS = "numPeaks";
    private static final String DIMENSIONS = "dimensions";

    private List<Peak> peakList;
    private DFParameters dfParameters;

    @Parameter(name = "Number of peaks")
    private int numPeaks;

    @Parameter(name = "Change step")
    private int changeStep;

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

    public void setChangeStep(int changeStep) {
        this.changeStep = changeStep;
    }
}
