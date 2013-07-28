package tweetzor2

import tweetzor2.core.Tweet
import tweetzor2.core.TwitterClient
import tweetzor2.core.PostcodeDatabase

class Menu {

  val api = new TwitterClient
  val postcodes = new PostcodeDatabase("/postcodes.csv")

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

  // Type alias. A `SearchFilter` is a function from `Tweet` to `Boolean`.
  // *We don't need this declaration* - it just makes the code clearer.
  type SearchFilter = (Tweet) => Boolean

  // Display the user's timeline, filtered by an arbitrary search filter.
  def showTimeline(filter: SearchFilter) = {
    api.timeline.filter(filter).foreach(printTweet _)
  }

  // Search filters
  // --------------
  
  // Search filter that matches all tweets.
  val allTweets: SearchFilter =
    (tweet: Tweet) => true

  // Create a search filter that matches tweets containing a text pattern.
  def byText(pattern: String): SearchFilter = {
    (tweet: Tweet) => tweet.text.contains(pattern)
  }

  // Create a search filter that matches tweets by an author.
  def byAuthor(author: String): SearchFilter = {
    (tweet: Tweet) => tweet.author.contains(author)
  }

  // Create a search filter that is the disjunction of the arguments.
  def or(filters: (Tweet => Boolean)*): SearchFilter = {
    (tweet: Tweet) => filters.exists(filter => filter(tweet) == true)
  }

  // Helpers
  // -------

  // Print a `Tweet` to stdout. 
  private def printTweet(tweet: Tweet): Unit = {
    printPadded(tweet.author, 20)
    printPadded(tweet.location.map(_.ngr).flatMap(postcodes.nearestPostcode _).getOrElse("Unknown"), 20)
    println(tweet.text.replaceAll("[\r\n]+", " "))
  }
  
  // Print `msg`, padded to `width` with spaces.
  private def printPadded(msg: String, width: Int) {
    print(msg)
    for(i <- msg.length to width) print(" ")
  }

}