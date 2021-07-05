package com.skynet.skynet;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class LinkSystem {
  Map<Integer, Proc> proc_map = new HashMap<Integer, Proc>();
  Map<Integer, Machine> mach_map = new HashMap<Integer, Machine>();
  ArrayList<Link> link_list = new ArrayList<Link>(); // haha
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  Reader proc_reader;
  Reader mach_reader;

  public LinkSystem(String procFile, String machFile, String linkFile)
      throws IOException, BadMachineException, BadLinkingException {
    // get procs from json file
    proc_reader = Files.newBufferedReader(Paths.get(procFile));
    Type procMapType = new TypeToken<Map<Integer, Proc>>() {
    }.getType();
    proc_map = gson.fromJson(proc_reader, procMapType);
    for (Proc proc : proc_map.values()) {
      try {
        proc.validateProc();
      } catch (Exception e) {
        System.out.println("PROC " + proc.getID() + " INVALID!");
      }
    }

    // get machs from json file
    mach_reader = Files.newBufferedReader(Paths.get("machlayout.json"));
    Type machMachType = new TypeToken<Map<Integer, Machine>>() {
    }.getType();
    mach_map = gson.fromJson(mach_reader, machMachType);
    for (Machine machine : mach_map.values()) {
      machine.validateMach();
    }
  }

  public void create_new() throws BadLinkingException, IOException {
    createLinks(proc_map, mach_map, link_list);

    // Pair links up with e.o. to share services if needed
    for (Link link : link_list) {
      System.out.println(link.canHazServs(link_list, false));
    }

    // write valid links to file
    Writer writer = Files.newBufferedWriter(Paths.get("linklayout.json"));
    System.out.println("ALL LINK SERVICES FULFILLED: " + validateSystem(link_list));
    gson.toJson(link_list, writer);
    writer.flush();
    writer.close();
  }

  public static boolean validateSystem(ArrayList<Link> link_list) {
    for (Link link : link_list) {
      for (boolean b : link.getAllTrue()) {
        if (!b) {
          return false;
        }
      }
    }
    return true;
  }

  public void destroyLink(Link link, Map<Integer, Proc> proc_map, Map<Integer, Machine> mach_map,
      ArrayList<Link> link_list) {
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

  private static void createLinks(Map<Integer, Proc> proc_map, Map<Integer, Machine> mach_map,
      ArrayList<Link> link_list) throws BadLinkingException {
    for (Proc proc : proc_map.values()) {// O(p*m*t); p=#procs, m=#mach, t=#types
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
}
