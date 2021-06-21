package com.skynet.skynet;

import java.util.Arrays;

/**
 * Process -- a process designed to run on certain types of machines
 * 
 * @implSpec Name?
 * @implSpec REQUIRES machine type spec -- what can run this proc? on [types]
 * @implSpec REQUIRES >=0 services from other processes; on [servReq]
 * @implSpec RETURNS services used in other processes; on [servGiv]
 * @implSpec REQUIRES > 0 resources from other machines; on [numRss]
 */
public class Proc {
  private int iD;
  private int[] types;
  private int[] servReq;
  private int[] servGiv;
  private boolean binded = false;
  private com.skynet.skynet.Machine boundedTo;

  private int numRss;

  public Proc(int iD, int[] types, int[] servReq, int[] servGiv, int numRss) {
    this.iD = iD;
    this.types = types;
    this.servReq = servReq;
    this.servGiv = servGiv;
    this.numRss = numRss;
  }

  public String toString() {
    String name = "PROCESS " + iD + "\n------------";
    return name + "\nValid Mach Types: " + Arrays.toString(types) + "\nNum Rss needed: " + numRss
        + "\nServices Required: " + Arrays.toString(servReq) + "\nServices Provided: " + Arrays.toString(servGiv)
        + "\nIs Binded: " + binded + "\n\n";
  }

  public void validateProc() throws BadProcException {
    try {
      for (int i : servGiv) {
        for (int j : servReq) {
          assert !(i == j);
        }
      }
    } catch (Exception e) {
      throw new BadProcException("BAD PROC; cant provide & require identical service");
    }
  }

  public boolean equals(Proc p) {
    return iD == p.getID();
  }

  public int getID() {
    return iD;
  }

  public int[] getTypes() {
    return types;
  }

  public int getNumRss() {
    return numRss;
  }

  public void setNumRss(int numRss) {
    this.numRss = numRss;
  }

  public int[] getServReq() {
    return servReq;
  }

  public int[] getServGiv() {
    return servGiv;
  }

  public boolean getBinded() {
    return this.binded;
  }

  public void setBinded(boolean binded) {
    this.binded = binded;
  }

  public Machine getBoundedTo() {
    return this.boundedTo;
  }

  public void setBoundedTo(Machine boundedTo) {
    this.boundedTo = boundedTo;
  }
}
