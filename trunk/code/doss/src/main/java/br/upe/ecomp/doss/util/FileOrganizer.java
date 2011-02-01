package br.upe.ecomp.doss.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import br.upe.ecomp.doss.core.exception.InfraException;

/**
 * 
 * This class is used to organize file in folders.
 * 
 * @author George Moraes
 * 
 */
public class FileOrganizer {

    public static final String RESULT_TOKEN = ":";

    /**
     * Separate all files, from a given source directory, in folders based on parameters values.
     * 
     * @param srcDir The directory containing files that will be separated
     * @param params The array containing the parameters' names
     */
    public static void moveAllFiles(File srcDir, String[] params) {
        File[] srcFiles = srcDir.listFiles();
        for (File file : srcFiles) {
            if (file.isFile()) {
                organizeFileByParamValue(file, params);
            }
        }
    }

    private static boolean organizeFileByParamValue(File file, String[] params) {
        BufferedReader reader = null;
        String paramValue = "";
        String folderName = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            for (String param : params) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(param)) {
                        paramValue = line.split(RESULT_TOKEN)[1].trim();
                        folderName += param.substring(0, 10) + paramValue;
                        break;
                    }
                }
            }
            File directory = new File(file.getParent() + File.separator + folderName);
            directory.mkdir();
            return file.renameTo(new File(directory, file.getName()));
        } catch (FileNotFoundException cause) {
            throw new InfraException("Unable to find the file: " + file.getName(), cause);
        } catch (IOException cause) {
            throw new InfraException("Error while reading the file: " + file.getName(), cause);
        } finally {
            try {
                reader.close();
            } catch (IOException cause) {
                throw new InfraException("Error while trying close the reader: " + file.getName(), cause);
            }
        }
    }

    public static void main(String[] args) {
        String[] params = { "stepVolDecayPercentage", "stepVolInitPercentage" };
        String filePath = "Local_Best_Volitive_PSO";
        File fileNew = new File(filePath);
        moveAllFiles(fileNew, params);
    }
}
