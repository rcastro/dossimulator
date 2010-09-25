package br.upe.ecomp.doss.util;

import java.text.NumberFormat;

public class NumberUtils {

	public static double digitsFraction(double number, int digits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(digits);
		return Double.valueOf(nf.format(number).replace(',', '.'));
	}

	public static double twoDigitsFraction(double number) {
		NumberFormat nf = NumberFormat.getInstance();
		Double num = new Double(number);
		nf.setMaximumFractionDigits(2);
		return Double.valueOf(nf.format(num).replace(',', '.'));
	}

	/*
	public static int digitsInteger(int number, int digits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(digits);
		return Integer.valueOf(nf.format(number).replace(',', '.'));
	}*/
	
	public static int getPercentToQuantity(int intPercent, int intTotal) {
		double percent = ( (double)intPercent / 100); 
		int nBests = (int) Math.round(intTotal * percent);
		return nBests;
	}
	
}
