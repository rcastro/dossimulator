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
package br.upe.ecomp.doss.recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.measurement.Measurement;

/**
 * A file based implementation of {@link IRecorder}.
 * 
 * @author Rodrigo Castro
 */
public class FileRecorder implements IRecorder {

    private FileWriter file;
    private String problemDirectoryName;
    private String executionDirectoryName;
    private String fileName;
    private String filePath;
    private String algorithmName;

    /**
     * {@inheritDoc}
     */
    public void init(Algorithm algorithm) {
        this.algorithmName = algorithm.getName();
        createAlgorithmDirectory();

        filePath = problemDirectoryName + File.separatorChar + File.separatorChar;
        createFile();
        printFileHeader(algorithm);
    }

    /**
     * {@inheritDoc}
     */
    public void update(Algorithm algorithm) {
        double[] position;

        printIterationHeader(algorithm.getIterations());

        Particle[] particles = algorithm.getParticles();
        for (Particle particle : particles) {
            position = particle.getCurrentPosition();
            printPosition(position);
        }

        printMeasurementHeader(algorithm.getIterations());

        for (Measurement measurement : algorithm.getMeasurements()) {
            printMeasurement(measurement);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void finalise(Algorithm algorithm) {
        try {
            file.write("Total iterations: " + algorithm.getIterations());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printFileHeader(Algorithm algorithm) {
        try {
            file.write("Problem: " + algorithmName + "\n");
            file.write("Measurements: " + getMeasurementsList(algorithm.getMeasurements()) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMeasurementsList(List<Measurement> measurements) {
        StringBuilder sbMeasurements = new StringBuilder();
        for (Iterator<Measurement> iterator = measurements.iterator(); iterator.hasNext();) {
            sbMeasurements.append(iterator.next().getName());
            if (iterator.hasNext()) {
                sbMeasurements.append(", ");
            }
        }
        return sbMeasurements.toString();
    }

    private void printMeasurementHeader(int iteration) {
        try {
            file.write("\nMeasurement(s) for iteration " + iteration + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printIterationHeader(int iteration) {
        try {
            file.write("\nIteration " + iteration + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPosition(double[] position) {
        StringBuilder line = new StringBuilder(String.valueOf(position[0]));

        int length = position.length;
        for (int i = 1; i < length; i++) {
            line.append(" ");
            line.append(String.valueOf(position[i]));
        }
        line.append("\n");

        try {
            file.write(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printMeasurement(Measurement measurement) {
        StringBuilder line = new StringBuilder(measurement.getName());
        line.append(": ");
        line.append(measurement.getValue());
        line.append("\n");

        try {
            file.write(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAlgorithmDirectory() {
        problemDirectoryName = algorithmName.replace(" ", "_");

        File directory = new File(problemDirectoryName);
        if (!directory.isDirectory()) {
            directory.mkdir();
        }
    }

    private void createExecutionDirectory() {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy");

        executionDirectoryName = s.format(new Date());
        File directory = new File(problemDirectoryName + File.separatorChar + executionDirectoryName);
        if (!directory.isDirectory()) {
            directory.mkdir();
        }
    }

    private void createFile() {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy_hhmmss");
        fileName = s.format(new Date());

        try {
            file = new FileWriter(new File(filePath, fileName + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
