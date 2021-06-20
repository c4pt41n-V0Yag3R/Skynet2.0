package com.skynet.skynet;

import java.util.*;

/**
 * Machine -- machines designed to run processes
 * 
 * @implSpec REQUIRES unique ID; on [machID] in
 * @implSpec PROVIDES resources processes need; on [rssAvail]
 * @implSpec REQUIRES machine type; on [machType]
 * 
 */
public class Machine {
  private int iD;
  private int type;
  private int rssAvail;
  ArrayList<Proc> boundProcs;
  private int numBinds = 0;

  public Machine(int machID, int machType, int rssAvail) {
    this.iD = machID;
    this.type = machType;
    this.rssAvail = rssAvail;
    boundProcs = new ArrayList<Proc>();
  }

  public void validateMach() throws BadMachineException {
    try {
      assert boundProcs.size() == numBinds;
      assert rssAvail >= 0;
    } catch (Exception e) {
      throw new BadMachineException("links tampered with!");
    }
  }

  public int getID() {
    return iD;
  }

  public int getRssAvail() {
    return rssAvail;
  }

  public void setRssAvail(int rssAvail) {
    this.rssAvail = rssAvail;
  }

  public int getMachType() {
    return type;
  }

  public int getNumBinds() {
    return numBinds;
  }

  public void setNumBinds(int numBinds) {
    this.numBinds = numBinds;
  }

  public String toString() {
    return "MACHINE " + iD + "\n----------\nType: " + type + "\nRss Available: " + rssAvail + "\nNum Binds: " + numBinds
        + "\n\n";
  }
}
