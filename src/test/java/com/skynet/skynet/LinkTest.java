package com.skynet.skynet;

import static org.junit.Assert.*;

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
  public void testValidateLink() {
    // enforce that a link btwn proc p and mach m only exists if mach has enough
    // rss, and if is of an allowed type according to proc
    int[] types0 = { 1, 2, 3 };
    int[] serR0 = { 1, 2, 3 };
    int[] serG0 = { 3, 4, 5 };
    Proc testProc0 = new Proc(0, types0, serR0, serG0, 10);
    Machine testMach0 = new Machine(0, 1, 20);
    Machine testMach1 = new Machine(1, 4, 20);
    Machine testMach2 = new Machine(2, 2, 9);
    Machine testMach3 = new Machine(3, 3, 10);
    // tM0 has more rss than tP0 needs, and is of correct type
    assertTrue(assValLink(testProc0, testMach0));
    // tM1 is not in type array of tP0
    assertFalse(assValLink(testProc0, testMach1));
    // tM2 does not have enough rss
    assertFalse(assValLink(testProc0, testMach2));
    assertTrue(assValLink(testProc0, testMach3));
  }
}
