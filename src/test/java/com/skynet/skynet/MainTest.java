package com.skynet.skynet;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

public class MainTest {
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

  @Test
  public void testValidateSystem() throws BadLinkingException, IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Reader readerProcs = Files.newBufferedReader(Paths.get("MainTestProcs.json"));
    Reader readerMachs = Files.newBufferedReader(Paths.get("MainTestMachs.json"));
    Proc[] proc_arr = gson.fromJson(readerProcs, Proc[].class);
    Machine[] mach_arr = gson.fromJson(readerMachs, Machine[].class);
    ArrayList<Link> link_list = new ArrayList<Link>();
    link_list.add(new Link(proc_arr[0], mach_arr[0], false));
    link_list.add(new Link(proc_arr[1], mach_arr[1], false));
    for (Link link : link_list) {
      link.canHazServs(link_list, false);
    }
    assertTrue(validateSystem(link_list));
  }
}
