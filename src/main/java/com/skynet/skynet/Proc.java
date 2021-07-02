package com.skynet.skynet;

import java.util.*;

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
  private Machine boundedTo;

  private HashMap<String, Integer> numRss;

  public Proc(int iD, int[] types, int[] servReq, int[] servGiv, HashMap<String, Integer> numRss) {
    this.iD = iD;
    this.types = types;
    this.servReq = servReq;
    this.servGiv = servGiv;
    this.numRss = numRss;
  }

  @Override
  public String toString() {
    String name = "PROCESS " + iD + "\n------------";
    return name + "\nValid Mach Types: " + Arrays.toString(types) + "\nNum Rss needed: " + numRss
        + "\nServices Required: " + Arrays.toString(servReq) + "\nServices Provided: " + Arrays.toString(servGiv)
        + "\nIs Binded: " + binded + "\n\n";
  }

  public void validateProc(ArrayList<Proc> p_list) throws BadProcException {
    try {
      for (Proc proc : p_list) {
        if (equals(proc)) {
          continue;
        }
        assert iD != proc.getID();
      }
    } catch (Exception e) {
      throw new BadProcException("PROC " + iD + " HAS CONFLICTS");
    }
  }

  public void validateProc() throws BadProcException {
    try {
      // must have valid types to use
      assert !(types == null) && (types.length >= 1);

      // must have valid # rss
      for (int i : numRss.values()) {
        assert i >= 0;
      }
      // servGiv and servReq must be disjoint
      for (int i : servGiv) {
        for (int j : servReq) {
          assert !(i == j);
        }
      }
      if (boundedTo == null) {
        boundedTo = new Machine();
      }
    } catch (Exception e) {
      throw new BadProcException("BAD PROC " + iD);
    }
  }

  public boolean equals(Proc p) {
    Arrays.sort(types);
    Arrays.sort(servReq);
    Arrays.sort(servGiv);
    Arrays.sort(p.getTypes());
    Arrays.sort(p.getServReq());
    Arrays.sort(p.getServGiv());
    return (iD == p.getID()) && Arrays.equals(types, p.getTypes()) && Arrays.equals(servReq, p.getServReq())
        && Arrays.equals(servGiv, p.getServGiv()) && (binded == p.getBinded()) && (boundedTo.equals(p.getBoundedTo()));
  }

  public int getID() {
    return iD;
  }

  public int[] getTypes() {
    return types;
  }

  public Collection<String> getRssTypes() {
    return numRss.keySet();
  }

  public int getNumRssType(String rssType) {
    return numRss.get(rssType);
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
