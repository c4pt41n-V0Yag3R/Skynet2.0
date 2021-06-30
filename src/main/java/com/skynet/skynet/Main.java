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

    Writer writer = Files.newBufferedWriter(Paths.get("linklayout.json"));
    // Writer writer1 = Files.newBufferedWriter(Paths.get("machlayout.json"));

    // for (int i = 0; i < 5; i++) {// set up random procs
    // int[] types_arr = rand.ints(3, 1, 6).distinct().toArray();
    // int[] sReq_arr = rand.ints(3, 0, 5).distinct().toArray();
    // int[] sGivd_arr = rand.ints(3, 0, 5).distinct().toArray();
    // int numRss = rand.nextInt(5) + 5;
    // proc_list.add(new Proc(i, types_arr, sReq_arr, sGivd_arr, numRss));
    // }
    // gson.toJson(proc_list, writer);
    // writer.flush();
    // writer.close();

    Reader reader = Files.newBufferedReader(Paths.get("proclayout.json"));

    Proc[] fromJson = gson.fromJson(reader, Proc[].class);
    proc_list.addAll(Arrays.asList(fromJson));
    for (Proc proc : proc_list) {
      try {
        proc.validateProc();
        proc.validateProc(proc_list);
      } catch (Exception e) {
        System.out.println("PROC " + proc.getID() + " INVALID!");
      }
    }

    // for (int i = 0; i < 5; i++) {// set up random machines
    // int type = rand.nextInt(5) + 1;
    // int numRss = rand.nextInt(5) + 5;
    // mach_list.add(new Machine(i, type, numRss));
    // }

    // gson.toJson(mach_list, writer1);
    // writer1.flush();
    // writer1.close();

    Reader reader1 = Files.newBufferedReader(Paths.get("machlayout.json"));

    Machine[] fromJson1 = gson.fromJson(reader1, Machine[].class);
    mach_list.addAll(Arrays.asList(fromJson1));
    for (Machine machine : mach_list) {
      machine.validateMach();
    }
    // System.out.println(mach_list);

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
    // Pair links up with e.o. to share services if needed
    for (Link link : link_list) {
      System.out.println(link.canHazServs(link_list, false));
    }
    // System.out.println(link_list);
    if (validateSystem(link_list))
      gson.toJson(link_list, writer);
    else
      System.out.println("LINK SYSTEM IS INVALID");
    writer.flush();
    writer.close();
  }
}
