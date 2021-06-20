package com.skynet.skynet;

public class Link {
  private int[] servicesNeeded;
  private int[] servicesProvided;
  private Proc proc;
  private Machine mach;

  public Link(Proc p, Machine m, boolean isTest) throws BadLinkingException {
    validateLink(p, m);
    if (!isTest) {
      p.setBinded(true);
      p.setBoundedTo(m);
      m.setRssAvail(Math.max(m.getRssAvail() - p.getNumRss(), 0));
      m.boundProcs.add(p);
      m.setNumBinds(m.getNumBinds() + 1);
      servicesNeeded = p.getServReq();
      servicesProvided = p.getServGiv();

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

  public int[] getServicesNeeded() {
    return this.servicesNeeded;
  }

  public void setServicesNeeded(int[] servicesNeeded) {
    this.servicesNeeded = servicesNeeded;
  }

  public int[] getServicesProvided() {
    return this.servicesProvided;
  }

  public void setServicesProvided(int[] servicesProvided) {
    this.servicesProvided = servicesProvided;
  }

  public String toString() {
    return "PROCESS " + proc.getID() + "-> MACHINE " + mach.getID() + "\n" + proc.toString() + mach.toString();
  }
}
