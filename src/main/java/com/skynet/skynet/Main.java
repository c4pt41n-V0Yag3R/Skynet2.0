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

public class Main {
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

  public static void main(String[] args)
      throws BadProcException, BadMachineException, BadLinkingException, IOException {
    // setup
    Map<Integer, Proc> proc_map = new HashMap<Integer, Proc>();
    Map<Integer, Machine> mach_map = new HashMap<Integer, Machine>();
    ArrayList<Link> link_list = new ArrayList<Link>(); // haha
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // get procs from json file
    Reader reader = Files.newBufferedReader(Paths.get("proclayout.json"));
    Type procMapType = new TypeToken<Map<Integer, Proc>>() {
    }.getType();
    proc_map = gson.fromJson(reader, procMapType);
    for (Proc proc : proc_map.values()) {
      try {
        proc.validateProc();
      } catch (Exception e) {
        System.out.println("PROC " + proc.getID() + " INVALID!");
      }
    }

    // get machs from json file
    Reader reader1 = Files.newBufferedReader(Paths.get("machlayout.json"));
    Type machMachType = new TypeToken<Map<Integer, Machine>>() {
    }.getType();
    mach_map = gson.fromJson(reader1, machMachType);
    for (Machine machine : mach_map.values()) {
      machine.validateMach();
    }

    createLinks(proc_map, mach_map, link_list);

    // Pair links up with e.o. to share services if needed
    for (Link link : link_list) {
      System.out.println(link.canHazServs(link_list, false));
    }

    // write valid links to file
    Writer writer = Files.newBufferedWriter(Paths.get("linklayout.json"));
    System.out.println("ALL ELEMENTS CONNECTED: " + validateSystem(link_list));
    gson.toJson(link_list, writer);
    writer.flush();
    writer.close();
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
