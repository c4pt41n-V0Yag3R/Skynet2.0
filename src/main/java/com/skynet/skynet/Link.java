package com.skynet.skynet;

import java.util.*;

public class Link {
  private int[] servicesNeeded;
  // connMap has keys as proc ids, vals as the services that key provides
  private Map<Integer, Integer> connMap = new HashMap<Integer, Integer>();
  private int[] servicesProvided;
  private transient boolean[] allTrue;
  public int procID;
  public int machID;
  private transient String toStr;

  public Link(Proc p, Machine m, boolean isTest) throws BadLinkingException {
    validateLink(p, m);
    if (!isTest) {
      p.setBinded(true);
      p.setBoundedTo(m);
      for (String rssType : p.getRssTypes()) {
        m.setRssTypeAvail(rssType, Math.max(m.getRssTypeAvail(rssType) - p.getNumRssType(rssType), 0));
      }
      m.boundProcs.add(p.getID());
      m.setNumBinds(m.getNumBinds() + 1);
      servicesNeeded = p.getServReq();
      allTrue = new boolean[p.getServReq().length];
      servicesProvided = p.getServGiv();

      procID = p.getID();
      machID = m.getID();

      toStr = "PROC " + procID + " -> MACH " + machID + "\n" + p.toString() + m.toString();

      System.out.println("Link Established between Process " + procID + " and Machine " + machID);
    }
  }

  public void validateLink(Proc p, Machine m) throws BadLinkingException {
    boolean contains = false;
    for (int i : p.getTypes()) {
      if (m.getMachType() == i) {
        contains = true;
        break;
      }
    }
    if (!contains)
      throw new BadLinkingException(
          "LINK BETWEEN: Process " + p.getID() + " AND Machine " + m.getID() + " IS IMPOSSIBLE; bad typing");
    if (p.getBinded()) {
      throw new BadLinkingException(
          "LINK BETWEEN: Process " + p.getID() + " AND Machine " + m.getID() + " IS IMPOSSIBLE; proc already bound");

    }
    for (String rssType : p.getRssTypes()) {
      if (!(m.getRssTypes().contains(rssType)) || m.getRssTypeAvail(rssType) < p.getNumRssType(rssType)) {
        throw new BadLinkingException(
            "LINK BETWEEN: Process " + p.getID() + " AND Machine " + m.getID() + " IS IMPOSSIBLE; bad rss use");
      }
    }
  }

  public String canHazServs(ArrayList<Link> link_list, boolean selfServicing) {
    boolean allSame = true;
    getservs: for (Link link : link_list) {
      for (int serv : this.getServicesNeeded()) {
        for (int serv2 : link.getServicesProvided()) {
          if (serv == serv2) {
            connMap.put(link.procID, serv);
            for (int i = 0; i < this.getServicesNeeded().length; i++) {
              if (this.getServicesNeeded()[i] == serv) {
                allTrue[i] = true;
              }
            }
            for (boolean b : allTrue) {
              if (!b)
                allSame = false;
            }
            if (allSame) {
              break getservs;
            } else {
              continue;
            }
          }
        }
      }
    }
    String res = "";
    for (int p : connMap.keySet()) {
      if (!selfServicing && (procID == p)) {// hacky af
        continue;
      }
      res += "PROC " + procID + "(bound to MACH " + machID + ") can get service " + connMap.get(p) + " from PROC " + p
          + "\n";
    }
    return res;
  }

  // private boolean equals(Link link) {
  // /**
  // * Link A = Link B if they connect the same proc to the same mach
  // */
  // return (proc.equals(link.proc) && mach.equals(link.mach));
  // }

  public int[] getServicesNeeded() {
    return servicesNeeded;
  }

  // public void setServicesNeeded(int[] servicesNeeded) {
  // this.servicesNeeded = servicesNeeded;
  // }

  public int[] getServicesProvided() {
    return servicesProvided;
  }

  public boolean[] getAllTrue() {
    return this.allTrue;
  }

  // public void setServicesProvided(int[] servicesProvided) {
  // this.servicesProvided = servicesProvided;
  // }
  @Override
  public String toString() {
    return toStr;
  }
}
