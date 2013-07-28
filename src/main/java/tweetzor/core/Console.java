package tweetzor.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

// Rough-and-ready replacement for `java.io.Console`
// that provides easy access to a `readLine` from stdin.
// 
// This class exists because `java.io.Console` doesn't 
// work well with the Eclipse REPL.
public class Console {

  private static PrintStream out = System.out;
  private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

  // Print `msg` to stdout.
  public static void print(String msg) {
    out.print(msg);
  }
  
  // Move stdout to a new line.
  public static void println() {
    out.println();
  }
  
  // Print `msg` to stdout and move to a new line.
  public static void println(String msg) {
    out.println(msg);
  }
  
  // Print `prompt` to stdout and read a line from stdin.
  public static String readLine(String prompt) {
    try {
      out.print(prompt);
      return in.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
}
