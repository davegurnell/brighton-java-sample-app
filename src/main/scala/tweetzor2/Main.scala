package tweetzor2

import tweetzor2.core.TwitterClient

// Scala provides *singleton object* syntax as a replacement for *static methods*.
object Main {

  // `public static void main` is a normal method on a singleton object.
  // It has the same type signature we know and love.
  def main(args: Array[String]): Unit = {
    new Menu().menuLoop()
  }

}
