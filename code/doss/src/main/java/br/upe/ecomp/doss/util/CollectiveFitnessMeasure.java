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
package br.upe.ecomp.doss.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import br.upe.ecomp.doss.core.exception.InfraException;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public class CollectiveFitnessMeasure {

    protected static final String RESULT_TOKEN = ":";

    /**
     * Calculates the collective fitness measure.
     * 
     * @return The collective fitness measure.
     */
    public static double getCollectiveFitness(File directory, String measurement) {
        // We want to guarantee that the last iteration is always computed.
        // if (lastIteration % step != 0) {
        // iterations += 1;
        // }

        List<File> files = getFilesOnDirectory(directory);

        double[] means = new double[files.size()];
        int currentFile = 0;
        for (File file : files) {
            means[currentFile] = getMean(file, measurement);
            currentFile++;
        }

        double collectiveMean = 0;
        for (int i = 0; i < means.length; i++) {
            collectiveMean += means[i];
        }

        return collectiveMean / means.length;
    }

    // private static double getMean(File file, String measurement) {
    // BufferedReader reader = null;
    // String measurementValue = null;
    // String line = null;
    // String measurementLine = measurement + RESULT_TOKEN;
    // int iterations = 0;
    // double mean = 0;
    // try {
    // reader = new BufferedReader(new FileReader(file));
    // while ((line = reader.readLine()) != null) {
    // if (line.startsWith(measurementLine)) {
    // measurementValue = line.split(RESULT_TOKEN)[1].trim();
    // mean += Double.parseDouble(measurementValue);
    // iterations++;
    // }
    // }
    // } catch (FileNotFoundException cause) {
    // throw new InfraException("Unable to find the file: " + file.getName(), cause);
    // } catch (IOException cause) {
    // throw new InfraException("Error while reading the file: " + file.getName(), cause);
    // } finally {
    // if (reader != null) {
    // try {
    // reader.close();
    // } catch (IOException e) {
    // // TODO Log!
    // e.printStackTrace();
    // }
    // }
    // }
    // return mean / iterations;
    // }

    private static double getMean(File file, String measurement) {
        double mean = 0;
        String line = null;
        String measurementLine = measurement + RESULT_TOKEN;

        long position;
        RandomAccessFile fileReader = null;
        try {
            fileReader = new RandomAccessFile(file, "r");
            position = fileReader.length();
            do {
                fileReader.seek(position--);
                line = fileReader.readLine();
            } while (position >= 0 && (line == null || !line.startsWith(measurementLine)));
        } catch (FileNotFoundException cause) {
            throw new InfraException("Unable to find the file: " + file.getName(), cause);
        } catch (IOException cause) {
            throw new InfraException("Error while reading the file: " + file.getName(), cause);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException cause) {
                    // TODO Log!
                    cause.printStackTrace();
                }
            }
        }

        if (line != null) {
            String meanRead = line.split(RESULT_TOKEN)[1].trim();
            mean = Double.parseDouble(meanRead);
        }

        return mean;
    }

    private static List<File> getFilesOnDirectory(File directory) {
        List<File> files = new ArrayList<File>();
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".txt")) {
                files.add(file);
            }
        }
        return files;
    }
}
