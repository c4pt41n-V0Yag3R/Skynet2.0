package com.skynet.skynet;

import java.io.IOException;

public class Main {
  public static void main(String[] args)
      throws BadProcException, BadMachineException, BadLinkingException, IOException {
    LinkSystem ls = new LinkSystem("proclayout.json", "machlayout.json", "linklayout.json");
    ls.create_new();
  }
}
