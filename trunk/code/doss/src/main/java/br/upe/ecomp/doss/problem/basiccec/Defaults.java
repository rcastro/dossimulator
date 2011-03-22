/*
 * Copyright (c) 2009 Thomas Weise for NICAL
 * http://www.it-weise.de/
 * tweise@gmx.de
 *
 * GNU LESSER GENERAL PUBLIC LICENSE (Version 2.1, February 1999)
 */

package br.upe.ecomp.doss.problem.basiccec;

/**
 * This internal class holds the default values for everything
 * 
 * This class has been imported from the CEC. (Sergio Ribeiro)
 * 
 * @author Thomas Weise
 */
public final class Defaults {

  /** the default dimension */
  static int default_dim = 1000;

  /** the default m */
  static int default_m = 50;

  

  /**
   * Obtain the randomizer
   * 
   * @param c
   *          the class to get the randomizer for
   * @return the randomizer
   */
  static final Randomizer getRandomizer(final Class/* <?> */c) {
    long l;

    try {
      l = Long.parseLong(c.getSimpleName().substring(1));
    } catch (Throwable tt) {
      throw new RuntimeException(tt);
    }

    return new Randomizer(l);
  }
  
  public static int getDefault_dim() {
      return default_dim;
  }

  public static void setDefault_dim(int default_dim) {
      Defaults.default_dim = default_dim;
  }
  
  public static int getDefault_m() {
      return default_m;
  }

  public static void setDefault_m(int default_m) {
      Defaults.default_m = default_m;
  }
  
}
