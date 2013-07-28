package tweetzor2

import tweetzor2.core.Tweet
import tweetzor2.core.TwitterClient

class Menu {

  val api = new TwitterClient

  // Main menu
  // ---------

  // Main menu loop.
  def menuLoop() = {
    var finished = false

    // * print menu options;
    do {
      println()
      println("1. Show my timeline")
      println("2. Search my timeline by text")
      println("3. Search my timeline by author")
      println("4. Search my timeline by text or author")
      println("Q. Quit")

      // * read user's input;
      val choice = readLine("Choose an item: ").trim.toLowerCase

      // * dispatch to the relevant menu option;
      choice match {
        case "1" => showTimeline(allTweets)
        case "2" => showTimeline(byText(readLine("Text: ")))
        case "3" => showTimeline(byAuthor(readLine("Author: ")))
        case "4" =>
          val pattern = readLine("Text or author: ")
          showTimeline(or(byText(pattern), byAuthor(pattern)))
        case "q" => finished = true
        case _   => println("Whu?")
      }

      // * loop until the user chooses "quit".
    } while(!finished);
  }

  // Menu options
  // ------------

  // Display the user's timeline, filtered by an arbitrary search filter.
  def showTimeline(filter: Tweet => Boolean) = {
    api.timeline.filter(filter).foreach(printTweet _)
  }

  // Search filters
  // --------------

  // A search filter that matches all tweets.
  val allTweets =
    (tweet: Tweet) => true

  // Create a search filter that matches tweets containing a text pattern.
  def byText(pattern: String) = {
    (tweet: Tweet) => tweet.text.contains(pattern)
  }

  // Create a search filter that matches tweets by an author.
  def byAuthor(author: String) = {
    (tweet: Tweet) => tweet.author.contains(author)
  }

  // Create a search filter that is the disjunction of the arguments.
  def or(filters: (Tweet => Boolean)*) = {
    (tweet: Tweet) => filters.exists(filter => filter(tweet) == true)
  }

  // Helpers
  // -------

  // Print a `Tweet` to stdout. 
  private def printTweet(tweet: Tweet): Unit = {
    print(tweet.author);
    for (i <- tweet.author.length() to 20) {
      print(" ")
    }
    println(tweet.text.replaceAll("[\r\n]+", " "))
  }

}