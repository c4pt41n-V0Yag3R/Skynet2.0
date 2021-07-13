package com.skynet.skynet;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class LinkSystem {
  public int sysID;
  public Map<Integer, Proc> proc_map = new HashMap<Integer, Proc>();
  public Map<Integer, Machine> mach_map = new HashMap<Integer, Machine>();
  public ArrayList<Link> link_list = new ArrayList<Link>(); // haha
  private static transient Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private static transient Reader proc_reader;
  private static transient Reader mach_reader;

  public LinkSystem(String procFile, String machFile, String linkFile)
      throws IOException, BadMachineException, BadLinkingException, BadProcException {
    getProcsFromFile(procFile);
    getMachsFromFile(machFile);
  }

  public LinkSystem(String procFile, String machFile)
      throws IOException, BadMachineException, BadLinkingException, BadProcException {
    getProcsFromFile(procFile);
    getMachsFromFile(machFile);
  }

  private void getMachsFromFile(String machFile) throws IOException, BadMachineException {
    mach_reader = Files.newBufferedReader(Paths.get(machFile));
    Type machMapType = new TypeToken<Map<Integer, Machine>>() {
    }.getType();
    Type machAListType = new TypeToken<ArrayList<Machine>>() {
    }.getType();
    try {
      mach_map = gson.fromJson(mach_reader, machMapType);
    } catch (Exception e) {
      ArrayList<Machine> machs = gson.fromJson(mach_reader, machAListType);
      for (Machine machine : machs) {
        mach_map.put(machine.getID(), machine);
      }
    }
    for (Machine machine : mach_map.values()) {
      machine.validateMach();
    }
  }

  private void getProcsFromFile(String procFile) throws IOException, BadProcException {
    proc_reader = Files.newBufferedReader(Paths.get(procFile));
    Type procMapType = new TypeToken<Map<Integer, Proc>>() {
    }.getType();
    Type procAListType = new TypeToken<ArrayList<Proc>>() {
    }.getType();
    try {
      proc_map = gson.fromJson(proc_reader, procMapType);
    } catch (Exception e) {
      ArrayList<Proc> procs = gson.fromJson(proc_reader, procAListType);
      for (Proc proc : procs) {
        proc_map.put(proc.getID(), proc);
      }
    }
    for (Proc proc : proc_map.values()) {
      proc.validateProc();
    }
  }

  public void create_new() throws BadLinkingException, IOException {
    createLinks(proc_map, mach_map);

    // Pair links up with e.o. to share services if needed
    for (Link link : link_list) {
      System.out.println(link.canHazServs(link_list, false));
    }

    // write valid links to file
    Writer writer = Files.newBufferedWriter(Paths.get("linklayout.json"));
    System.out.println("ALL LINK SERVICES FULFILLED: " + validateSystem());
    gson.toJson(link_list, writer);
    writer.flush();
    writer.close();
  }

  // checks if for all links all service requirements are fulfilled
  public boolean validateSystem() {
    for (Link link : link_list) {
      for (boolean b : link.getAllTrue()) {
        if (!b) {
          return false;
        }
      }
    }
    return true;
  }

  public void addProc(Proc proc) {
    proc_map.put(proc.getID(), proc);
  }

  public void addMach(Machine mach) {
    mach_map.put(mach.getID(), mach);
  }

  public void removeProc(Proc proc) {
    for (Link link : link_list) {
      if (link.procID == proc.getID()) {
        removeLink(link);
      }
    }
    proc_map.remove(proc.getID());
  }

  public void removeMach(Machine mach) {
    for (Link link : link_list) {
      if (link.machID == mach.getID()) {
        removeLink(link);
      }
    }
    mach_map.remove(mach.getID());
  }

  public void removeLink(Link link) {
    Proc proc = proc_map.get(link.procID);
    Machine mach = mach_map.get(link.machID);
    proc.setBinded(false);
    proc.setBoundedTo(new Machine());
    for (String rssType : proc.getRssTypes()) {
      mach.setRssTypeAvail(rssType, mach.getRssTypeAvail(rssType) + proc.getNumRssType(rssType));
    }
    mach.boundProcs.remove(proc);
    mach.setNumBinds(mach.getNumBinds() - 1);
    link_list.remove(link);
  }

  public Collection<Proc> getProcs() {
    return proc_map.values();
  }

  public Collection<Machine> getMachs() {
    return mach_map.values();
  }

  public Collection<Link> getLinks() {
    return link_list;
  }

  private void createLinks(Map<Integer, Proc> proc_map, Map<Integer, Machine> mach_map) throws BadLinkingException {
    for (Proc proc : proc_map.values()) {// O(p*m*t); p=#procs, m=#mach, t=#types
      if (proc.getBinded())
        continue;
      ArrayList<Machine> vmachList = new ArrayList<Machine>();
      for (Machine mach : mach_map.values()) {
        // 1. find all machines that can run proc and store in vmachList
        try {
          new Link(proc, mach, true);
        } catch (Exception e) {
          continue;
        }
        vmachList.add(mach);
      }
      // 2. find mach with minimum binds
      if (vmachList.size() == 0)
        continue;
      Machine minMach = vmachList.get(0);
      for (int i = 0; i < vmachList.size(); i++) {
        minMach = vmachList.get(i).getNumBinds() < minMach.getNumBinds() ? vmachList.get(i) : minMach;
      }
      link_list.add(new Link(proc, minMach, false));
    }
  }

  @Override
  public String toString() {
    String res = "";
    for (Link link : link_list) {
      res += link.toString();
    }
    res += "\nPROCESSES:\n\n";
    for (Proc proc : proc_map.values()) {
      res += proc.toString();
    }
    res += "\nMACHINES\n\n";
    for (Machine mach : mach_map.values()) {
      res += mach.toString();
    }
    return res;
  }
}
