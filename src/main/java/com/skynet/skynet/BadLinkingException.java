package com.skynet.skynet;

/**
 * BadLinkingException -- Thrown when a Link could not be established between a
 * proc and machine for 1 of 2 reasons:
 * 
 * 1) Not enough rss on machine for the proc, or 2) The machine type is
 * unallowed according to proc type specs
 */
public class BadLinkingException extends Exception {
  public BadLinkingException(String message) {
    super(message);
  }
}