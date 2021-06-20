package com.skynet.skynet;

import java.util.*;

public class Main {
  public static void main(String[] args) throws BadLinkingException {
    ArrayList<Proc> proc_list = new ArrayList<Proc>();
    ArrayList<Machine> mach_list = new ArrayList<Machine>();
    ArrayList<Link> link_list = new ArrayList<Link>(); // haha

    Random rand = new Random();

    for (int i = 0; i < 5; i++) {// set up random procs
      int[] types_arr = rand.ints(3, 1, 6).distinct().toArray();
      int[] sReq_arr = rand.ints(3, 0, 5).distinct().toArray();
      int[] sGivd_arr = rand.ints(3, 0, 5).distinct().toArray();
      int numRss = rand.nextInt(5) + 5;
      proc_list.add(new Proc(i, types_arr, sReq_arr, sGivd_arr, numRss));
    }

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
    // System.out.println(Arrays.toString(process_list.toArray()));
    // System.out.println(Arrays.toString(machine_list.toArray()));
    // System.out.println(link_list.get(0));

    // Pair links up with e.o. to share services if needed
    // for (int i = 0; i < link_list.size(); i++) {
    // for (int j = i+1; j < link_list.size()-1; j++) {

    // }
    // }
  }
}
