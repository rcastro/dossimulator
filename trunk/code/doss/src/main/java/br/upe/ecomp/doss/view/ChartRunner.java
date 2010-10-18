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
package br.upe.ecomp.doss.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.ColorAxis;
import ChartDirector.ContourLayer;
import ChartDirector.XYChart;
import br.upe.ecomp.doss.algorithm.Algorithm;
import br.upe.ecomp.doss.problem.Problem;
import br.upe.ecomp.doss.recorder.FileChartRecorder;

/**
 * Runs the algorithm and shows its execution at real time.
 * 
 * @author Rodrigo Castro
 * @author George Moraes
 */
public class ChartRunner implements Runnable {

    private FileChartRecorder recorder;
    private ChartViewer viewer;
    private boolean running;
    private JFrame frame;
    private double[] dataX;
    private double[] dataY;
    private Problem problem;
    private Algorithm algorithm;

    /**
     * The name of the chart.
     * 
     * @return The name of the chart.
     */
    public String toString() {
        return algorithm.getName();
    }

    /**
     * Creates the chart image.
     * 
     * @param recorder An instance of {@link FileChartRecorder}
     * @return The chart image.
     */
    public Image createChartImage(FileChartRecorder recorder) {

        double[] dataZ = new double[(dataX.length) * (dataY.length)];
        for (int yIndex = 0; yIndex < dataY.length; ++yIndex) {
            double y = dataY[yIndex];
            for (int xIndex = 0; xIndex < dataX.length; ++xIndex) {
                double x = dataX[xIndex];

                dataZ[yIndex * (dataX.length) + xIndex] = problem.getFitness(new double[] { x, y });
            }
        }

        // Create a XYChart object of size 600 x 500 pixels
        XYChart c = new XYChart(600, 500);

        // Add a title to the chart using 18 points Times New Roman Bold Italic
        // font
        c.addTitle(problem.getName(), "Times New Roman Bold Italic", 18);

        // Set the plot area at (75, 40) and of size 400 x 400 pixels. Use
        // semi-transparent black (80000000) dotted lines for both horizontal
        // and vertical grid lines
        c.setPlotArea(75, 40, 400, 400, -1, -1, -1, c.dashLineColor(0x80000000, Chart.DotLine), -1);

        // Set x-axis and y-axis title using 12 points Arial Bold Italic font
        c.xAxis().setTitle("x", "Arial Bold Italic", 12);
        c.yAxis().setTitle("y", "Arial Bold Italic", 12);

        // Set x-axis and y-axis labels to use Arial Bold font
        c.xAxis().setLabelStyle("Arial Bold");
        c.yAxis().setLabelStyle("Arial Bold");

        // When auto-scaling, use tick spacing of 40 pixels as a guideline
        c.yAxis().setTickDensity(40);
        c.xAxis().setTickDensity(40);

        c.addScatterLayer(recorder.getXAxis(), recorder.getYAxis(), "", Chart.Cross2Shape(0.2), 7, 0x000000);

        // Add a contour layer using the given data
        ContourLayer layer = c.addContourLayer(dataX, dataY, dataZ);

        // Set the contour color to transparent
        layer.setContourColor(Chart.Transparent);

        // Move the grid lines in front of the contour layer
        c.getPlotArea().moveGridBefore(layer);

        // Add a color axis (the legend) in which the left center point is anchored
        // at (495, 240). Set the length to 370 pixels and the labels on the right side.
        ColorAxis cAxis = layer.setColorAxis(495, 240, Chart.Left, 370, Chart.Right);

        // Add a bounding box to the color axis using light grey (eeeeee) as the
        // background and dark grey (444444) as the border.
        cAxis.setBoundingBox(0xeeeeee, 0x444444);

        // Add a title to the color axis using 12 points Arial Bold Italic font
        cAxis.setTitle("Height", "Arial Bold Italic", 12);

        // Set color axis labels to use Arial Bold font
        cAxis.setLabelStyle("Arial Bold");

        // Use smooth gradient coloring
        cAxis.setColorGradient(true);
        return c.makeImage();
    }

    /**
     * Creates the chart images.
     * 
     * @param viewer An instance of {@link ChartViewer}
     * @param recorder An instance of {@link FileChartRecorder}
     */
    public void createChart(ChartViewer viewer, FileChartRecorder recorder) {

        // Output the chart
        Image image = createChartImage(recorder);
        viewer.setImage(image);
    }

    @Override
    public void run() {
        while (running) {
            createChart(viewer, recorder);
            frame.repaint();

            try {
                Thread.sleep(250);
            } catch (InterruptedException cause) {
                // Log!
                cause.printStackTrace();
            }
        }
    }

    /**
     * Runs the algorithm and shows its execution at real time.
     * 
     * @param algorithm The instance of {@link Algorithm} that will be executed.
     */
    public void runChart(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.recorder = (FileChartRecorder) algorithm.getRecorder();
        this.problem = algorithm.getProblem();

        // Create and set up the main window
        frame = new JFrame(algorithm.getName());
        frame.getContentPane().setBackground(Color.white);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // The x and y coordinates of the chart grid
        dataX = new double[31];
        dataY = new double[31];
        for (int i = 0; i < 31; i++) {
            dataX[i] = i;
            dataY[i] = i;
        }

        // Create the chart and put it in the content pane
        this.viewer = new ChartViewer();
        createChart(viewer, recorder);
        frame.getContentPane().add(viewer);

        // Display the window
        frame.pack();
        frame.setVisible(true);

        // Runs the algorithm
        running = true;
        new Thread(this).start();
        algorithm.run();
        running = false;
    }
}
