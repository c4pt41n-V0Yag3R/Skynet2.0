package com.skynet.skynet;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

public class LinkTest {

  public boolean assValLink(Proc p, Machine m) {
    try {
      new Link(p, m, true);
    } catch (BadLinkingException e) {
      return false;
    }
    return true;
  }

  @Test
  public void testValidateLink() throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Reader readerProcs = Files.newBufferedReader(Paths.get("LinkTestProcs.json"));
    Reader readerMachs = Files.newBufferedReader(Paths.get("LinkTestMachs.json"));
    Proc[] proc_arr = gson.fromJson(readerProcs, Proc[].class);
    Machine[] mach_arr = gson.fromJson(readerMachs, Machine[].class);
    // enforce that a link btwn proc p and mach m only exists if mach has enough
    // rss, and if is of an allowed type according to proc

    // tM0 has more rss than tP0 needs, and is of correct type
    assertTrue(assValLink(proc_arr[0], mach_arr[0]));
    // tM1 is not in type array of tP0
    assertFalse(assValLink(proc_arr[0], mach_arr[1]));
    // tM2 does not have enough rss
    assertFalse(assValLink(proc_arr[0], mach_arr[2]));
    assertTrue(assValLink(proc_arr[0], mach_arr[3]));
    // tM4 does not have the correct rss
    assertFalse(assValLink(proc_arr[0], mach_arr[4]));
    // tM5 has both a correct rss and not correct rss
    assertTrue(assValLink(proc_arr[0], mach_arr[5]));
  }
}
