/**
 * Copyright (C) 2010
 * Rodrigo Castro
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

import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.algorithm.Particle;
import br.upe.ecomp.doss.measurement.IMeasurement;

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
    private String problemName;

    /**
     * {@inheritDoc}
     */
    public void init(Algorithm algorithm) {
        this.problemName = algorithm.getProblem().getName();
        createProblemDirectory();
        createExecutionDirectory();

        filePath = problemDirectoryName + File.separatorChar + executionDirectoryName + File.separatorChar;
        createFile();
        printFileHeader();
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

        for (IMeasurement measurement : algorithm.getMeasurements()) {
            printMeasurement(measurement);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void finalise() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printFileHeader() {
        try {
            file.write("Problem: " + problemName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void printMeasurement(IMeasurement measurement) {
        StringBuilder line = new StringBuilder(measurement.getName());
        line.append(":");
        line.append(measurement.getValue());
        line.append("\n");

        try {
            file.write(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createProblemDirectory() {
        problemDirectoryName = problemName.replace(" ", "_");

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
        SimpleDateFormat s = new SimpleDateFormat("hhmmss");
        fileName = s.format(new Date());

        try {
            file = new FileWriter(new File(filePath, fileName + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
