package com.skynet.skynet;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
  public static boolean validateSystem(ArrayList<Link> link_list) {
    for (Link link : link_list) {
      boolean[] arr = link.getAllTrue();
      for (boolean b : arr) {
        if (!b) {
          return false;
        }
      }
    }
    return true;
  }

  public static void main(String[] args)
      throws BadProcException, BadMachineException, BadLinkingException, IOException {
    ArrayList<Proc> proc_list = new ArrayList<Proc>();
    ArrayList<Machine> mach_list = new ArrayList<Machine>();
    ArrayList<Link> link_list = new ArrayList<Link>(); // haha

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Reader reader = Files.newBufferedReader(Paths.get("proclayout.json"));

    Proc[] fromJson = gson.fromJson(reader, Proc[].class);
    proc_list.addAll(Arrays.asList(fromJson));
    for (Proc proc : proc_list) {
      try {
        proc.validateProc();
        // proc.validateProc(proc_list);
      } catch (Exception e) {
        System.out.println("PROC " + proc.getID() + " INVALID!");
      }
    }

    Reader reader1 = Files.newBufferedReader(Paths.get("machlayout.json"));

    Machine[] fromJson1 = gson.fromJson(reader1, Machine[].class);
    mach_list.addAll(Arrays.asList(fromJson1));
    for (Machine machine : mach_list) {
      machine.validateMach();
    }
    // System.out.println(mach_list);

    createLinks(proc_list, mach_list, link_list);
    // Pair links up with e.o. to share services if needed
    for (Link link : link_list) {
      System.out.println(link.canHazServs(link_list, false));
    }
    // System.out.println(link_list);
    Writer writer = Files.newBufferedWriter(Paths.get("linklayout.json"));
    System.out.println("ALL ELEMENTS CONNECTED: " + validateSystem(link_list));
    gson.toJson(link_list, writer);
    writer.flush();
    writer.close();
  }

  private static void createLinks(ArrayList<Proc> proc_list, ArrayList<Machine> mach_list, ArrayList<Link> link_list)
      throws BadLinkingException {
    for (Proc proc : proc_list) {// O(p*m*t); p=#procs, m=#mach, t=#types
      ArrayList<Machine> vmachList = new ArrayList<Machine>();
      for (Machine mach : mach_list) {
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
