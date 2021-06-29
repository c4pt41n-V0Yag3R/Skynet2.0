package com.skynet.skynet;

import java.util.*;

public class Link {
  // private int[] servicesNeeded;
  // private int[] servicesProvided;
  private boolean[] allTrue;
  private Proc proc;
  private Machine mach;
  // private int procID = proc.getID();
  // private int machID = mach.getID();

  public Link(Proc p, Machine m, boolean isTest) throws BadLinkingException {
    validateLink(p, m);
    if (!isTest) {
      p.setBinded(true);
      p.setBoundedTo(m);
      m.setRssAvail(Math.max(m.getRssAvail() - p.getNumRss(), 0));
      m.boundProcs.add(p);
      m.setNumBinds(m.getNumBinds() + 1);
      // servicesNeeded = p.getServReq();
      allTrue = new boolean[p.getServReq().length];
      // servicesProvided = p.getServGiv();

      proc = p;
      mach = m;

      System.out.println("Link Established between Process " + p.getID() + " and Machine " + m.getID());
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
    if (p.getNumRss() > m.getRssAvail() || p.getBinded())
      throw new BadLinkingException(
          "LINK BETWEEN: Process " + p.getID() + " AND Machine " + m.getID() + " IS IMPOSSIBLE; bad rss use");
  }

  public String canHazServs(ArrayList<Link> link_list, boolean selfServicing) {
    Map<Proc, Integer> connMap = (Map<Proc, Integer>) new HashMap<Proc, Integer>();
    boolean allSame = true;
    getservs: for (Link link : link_list) {
      for (int serv : this.getServicesNeeded()) {
        for (int serv2 : link.getServicesProvided()) {
          if (serv == serv2) {
            connMap.put(link.proc, serv);
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
    for (Proc p : connMap.keySet()) {
      if (!selfServicing && proc.equals(p)) {// hacky af
        continue;
      }
      res += "PROC " + proc.getID() + "(bound to MACH " + mach.getID() + ") can get service " + connMap.get(p)
          + " from PROC " + p.getID() + "\n";
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
    return proc.getServReq();
  }

  // public void setServicesNeeded(int[] servicesNeeded) {
  // this.servicesNeeded = servicesNeeded;
  // }

  public int[] getServicesProvided() {
    return proc.getServGiv();
  }

  // public void setServicesProvided(int[] servicesProvided) {
  // this.servicesProvided = servicesProvided;
  // }

  public String toString() {
    return "PROC " + proc.getID() + " -> MACH " + mach.getID() + "\n" + proc.toString() + mach.toString();
  }
}
