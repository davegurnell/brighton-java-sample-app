package tweetzor;

import static tweetzor.core.Console.print;
import static tweetzor.core.Console.println;
import static tweetzor.core.Console.readLine;
import tweetzor.core.Tweet;
import tweetzor.core.TwitterClient;

public class Menu {
  
  private TwitterClient api = new TwitterClient();

  // Main menu
  // ---------
  
  // Main menu loop.
  public void menuLoop() {
    boolean finished = false;
    
    while (!finished) {
      // * print menu options;
      println();
      println("1. Show my timeline");
      println("2. Search my timeline by text");
      println("3. Search my timeline by author");
      println("4. Search my timeline by text or author");
      println("Q. Quit");

      // * read user's input;
      String choice = readLine("Choose an item: ").trim().toLowerCase();

      // * dispatch to the relevant menu option;
      if(choice.equals("1")) {
        showTimeline();
      } else if(choice.equals("2")) {
        searchTimelineByText(readLine("Text: "));
      } else if(choice.equals("3")) {
        searchTimelineByAuthor(readLine("Author: "));
      } else if(choice.equals("3")) {
        String pattern = readLine("Text or author: ");
        searchTimelineByTextOrAuthor(pattern);
      } else if(choice.equals("q")) {
        finished = true;
      } else {
        println("Whu?");
      }

      // * loop until the user chooses "quit".  
    }
  }
  
  // Menu options
  // ------------

  // Display the user's timeline.
  private void showTimeline() {
    for(Tweet tweet : api.getTimeline()) {
      printTweet(tweet);
    }
  }

  // Display tweets that have matching text.
  private void searchTimelineByText(String pattern) {
    for(Tweet tweet : api.getTimeline()) {
      if(tweet.getText().contains(pattern)) {
        printTweet(tweet);
      }
    }
  }

  // Display tweets that have matching authors.
  private void searchTimelineByAuthor(String pattern) {
    for(Tweet tweet : api.getTimeline()) {
      if(tweet.getAuthor().contains(pattern)) {
        printTweet(tweet);
      }
    }
  }

  // Display tweets that have matching text or authors.
  private void searchTimelineByTextOrAuthor(String pattern) {
    for(Tweet tweet : api.getTimeline()) {
      if(tweet.getText().contains(pattern) || tweet.getAuthor().contains(pattern)) {
        printTweet(tweet);
      }
    }
  }

  // Helpers
  // -------

  // Print a `Tweet` to stdout. 
  private void printTweet(Tweet tweet) {
    print(tweet.getAuthor());
    for(int i = tweet.getAuthor().length(); i < 20; i++) {
      print(" ");
    }
    println(tweet.getText().replaceAll("[\r\n]+", " "));
  }
  
}
