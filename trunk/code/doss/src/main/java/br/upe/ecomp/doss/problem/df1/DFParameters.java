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

public class DFParameters {

    private double aH; // coeficiente da funcao logistica
    private double aR; // coeficiente da funcao logistica
    private double aX; // coeficiente da funcao logistica
    private int xBase;
    private int xRange;
    private double xScale;
    private int hBase;
    private int hRange;
    private double hScale;
    private int rBase;
    private int rRange;
    private double rScale;
    private int dimensions;
    private boolean dynamicH; // mudanca na altura do pico?
    private boolean dynamicR; // mudanca na inclinacao do pico?
    private boolean dynamicX; // mudanca na posicao do pico?

    /**
     * Construtor com valores padroes para teste.
     */
    public DFParameters() {
        this.aH = 3.2;
        this.aR = 1.2;
        this.aX = 3.2;
        this.dimensions = 2;
        this.dynamicH = false;
        this.dynamicR = false;
        this.dynamicX = true;
        this.hBase = 40;
        this.hRange = 20;
        this.hScale = 0.5;
        this.rBase = 1;
        this.rRange = 7;
        this.rScale = 0.5;
        this.xBase = -10;
        this.xRange = 20;
        this.xScale = 0.7;
    }

    public DFParameters(double ah, double ar, double ax, int dimensions, boolean dynamicH, boolean dynamicR,
            boolean dynamicX, int hBase, int hRange, double hScale, int rBase, int rRange, double rScale, int xBase,
            int xRange, double xScale) {
        this.aH = ah;
        this.aR = ar;
        this.aX = ax;
        this.dimensions = dimensions;
        this.dynamicH = dynamicH;
        this.dynamicR = dynamicR;
        this.dynamicX = dynamicX;
        this.hBase = hBase;
        this.hRange = hRange;
        this.hScale = hScale;
        this.rBase = rBase;
        this.rRange = rRange;
        this.rScale = rScale;
        this.xBase = xBase;
        this.xRange = xRange;
        this.xScale = xScale;
    }

    public double getAH() {
        return aH;
    }

    public void setAH(double ah) {
        aH = ah;
    }

    public double getAR() {
        return aR;
    }

    public void setAR(double ar) {
        aR = ar;
    }

    public double getAX() {
        return aX;
    }

    public void setAX(double ax) {
        aX = ax;
    }

    public int getXBase() {
        return xBase;
    }

    public void setXBase(int base) {
        this.xBase = base;
    }

    public int getXRange() {
        return xRange;
    }

    public void setXRange(int range) {
        xRange = range;
    }

    public double getXScale() {
        return xScale;
    }

    public void setXScale(double scale) {
        xScale = scale;
    }

    public int getHBase() {
        return hBase;
    }

    public void setHBase(int base) {
        hBase = base;
    }

    public int getHRange() {
        return hRange;
    }

    public void setHRange(int range) {
        hRange = range;
    }

    public double getHScale() {
        return hScale;
    }

    public void setHScale(double scale) {
        hScale = scale;
    }

    public int getRBase() {
        return rBase;
    }

    public void setRBase(int base) {
        rBase = base;
    }

    public int getRRange() {
        return rRange;
    }

    public void setRRange(int range) {
        rRange = range;
    }

    public double getRScale() {
        return rScale;
    }

    public void setRScale(double scale) {
        rScale = scale;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public boolean isDynamicH() {
        return dynamicH;
    }

    public void setDynamicH(boolean dynamicH) {
        this.dynamicH = dynamicH;
    }

    public boolean isDynamicR() {
        return dynamicR;
    }

    public void setDynamicR(boolean dynamicR) {
        this.dynamicR = dynamicR;
    }

    public boolean isDynamicX() {
        return dynamicX;
    }

    public void setDynamicX(boolean dynamicX) {
        this.dynamicX = dynamicX;
    }

}
