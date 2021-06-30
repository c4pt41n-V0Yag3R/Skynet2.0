package com.skynet.skynet;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Assert.*;

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
  public void testValidateSystem() throws BadLinkingException {
    ArrayList<Link> link_list = new ArrayList<Link>();
    int[] types0 = { 1 };
    int[] servGiv0 = { 2 };
    int[] servReq0 = { 1 };
    int numRss0 = 10;

    int[] types1 = { 2 };
    int[] servGiv1 = { 1 };
    int[] servReq1 = { 2 };
    int numRss1 = 10;

    Proc tProc0 = new Proc(0, types0, servReq0, servGiv0, numRss0);
    Proc tProc1 = new Proc(1, types1, servReq1, servGiv1, numRss1);
    Machine tMach0 = new Machine(0, 1, 10);
    Machine tMach1 = new Machine(1, 2, 10);

    link_list.add(new Link(tProc0, tMach0, false));
    link_list.add(new Link(tProc1, tMach1, false));

    for (Link link : link_list) {
      link.canHazServs(link_list, false);
    }
    assertTrue(validateSystem(link_list));
  }
}
