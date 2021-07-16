package com.skynet.skynet;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class LinkSystemTest {
  @Test
  public void testValidateSystem() throws BadLinkingException, IOException, BadMachineException, BadProcException {
    LinkSystem ls = new LinkSystem("MainTestProcs.json", "MainTestMachs.json");
    ls.create_new(false);
    assertTrue(ls.validateSystem());
  }
}
