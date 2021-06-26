package com.skynet.skynet;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
  public static void main(String[] args) throws BadLinkingException, IOException {
    ArrayList<Proc> proc_list = new ArrayList<Proc>();
    ArrayList<Machine> mach_list = new ArrayList<Machine>();
    ArrayList<Link> link_list = new ArrayList<Link>(); // haha

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Random rand = new Random();

    Writer writer = Files.newBufferedWriter(Paths.get("procs.json"));

    for (int i = 0; i < 5; i++) {// set up random procs
      int[] types_arr = rand.ints(3, 1, 6).distinct().toArray();
      int[] sReq_arr = rand.ints(3, 0, 5).distinct().toArray();
      int[] sGivd_arr = rand.ints(3, 0, 5).distinct().toArray();
      int numRss = rand.nextInt(5) + 5;
      proc_list.add(new Proc(i, types_arr, sReq_arr, sGivd_arr, numRss));
    }
    gson.toJson(proc_list, writer);
    writer.flush();
    writer.close();
    for (int i = 0; i < 5; i++) {// set up random machines
      int type = rand.nextInt(5) + 1;
      int numRss = rand.nextInt(5) + 5;
      mach_list.add(new Machine(i, type, numRss));
    }

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
    System.out.println(link_list);
  }
}
