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
  private Map<String, Integer> rssAvail;
  ArrayList<Integer> boundProcs;
  private int numBinds = 0;
  private transient final int specID = Integer.MAX_VALUE;

  public Machine() {
    iD = specID;
    type = 0;
    rssAvail = new HashMap<String, Integer>();
  }

  public Machine(int machID, int machType, Map<String, Integer> rssAvail) {
    this.iD = machID;
    this.type = machType;
    this.rssAvail = rssAvail;
    boundProcs = new ArrayList<Integer>();
  }

  public void validateMach() throws BadMachineException {
    try {
      assert boundProcs.size() == numBinds;
      assert rssAvail.size() >= 0;
      // if (boundProcs.size() >= 1) {
      // for (Proc proc : boundProcs) {
      // assert proc.getBinded();
      // }
      // }
    } catch (Exception e) {
      throw new BadMachineException("BAD MACHINE " + iD);
    }
  }

  public boolean equals(Machine m) {
    return ((iD == m.getID()) && (type == m.getMachType())
        && (Arrays.equals(boundProcs.toArray(), m.boundProcs.toArray())));
  }

  public int getID() {
    return iD;
  }

  public int getRssTypeAvail(String rssType) {
    return rssAvail.get(rssType);
  }

  public Collection<String> getRssTypes() {
    return rssAvail.keySet();
  }

  public void setRssTypeAvail(String rssType, int val) {
    rssAvail.put(rssType, val);
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

  @Override
  public String toString() {
    return "MACHINE " + iD + "\n----------\nType: " + type + "\nRss Available: " + rssAvail + "\nNum Binds: " + numBinds
        + "\n\n";
  }
}
