package com.skynet.skynet;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
  public static void main(String[] args)
      throws BadProcException, BadMachineException, BadLinkingException, IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    LinkSystem ls = new LinkSystem("proclayout.json", "machlayout.json", "linklayout.json");
    ls.create_new(false);
    System.out.println(ls.toString());
    Writer writer = Files.newBufferedWriter(Paths.get("testLS.json"));
    gson.toJson(ls, writer);
    writer.flush();
    writer.close();
  }
}
