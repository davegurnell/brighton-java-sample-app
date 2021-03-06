package tweetzor;

import static tweetzor.core.Console.print;
import static tweetzor.core.Console.println;
import static tweetzor.core.Console.readLine;
import tweetzor.core.Location;
import tweetzor.core.PostcodeDatabase;
import tweetzor.core.Tweet;
import tweetzor.core.TwitterClient;

public class Menu {
  
  private TwitterClient api = new TwitterClient();
  private PostcodeDatabase postcodes = new PostcodeDatabase("/postcodes.csv");

  // Main menu
  // ---------
  
  // Main menu loop.
  public void menuLoop() {
    boolean finished = false;
    
    do {
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
        showTimeline(allTweets);
      } else if(choice.equals("2")) {
        showTimeline(byText(readLine("Text: ")));
      } else if(choice.equals("3")) {
        showTimeline(byAuthor(readLine("Author: ")));
      } else if(choice.equals("4")) {
        String pattern = readLine("Text or author: ");
        showTimeline(or(byText(pattern), byAuthor(pattern)));
      } else if(choice.equals("q")) {
        finished = true;
      } else {
        println("Whu?");
      }

      // * loop until the user chooses "quit".  
    } while(!finished);
  }
  
  // Menu options
  // ------------

  // A `SearchFilter` has a `matches` method that tells us whether to display a tweet.
  interface SearchFilter {
    public boolean matches(Tweet tweet);
  }
  
  // Display the user's timeline, filtered by an arbitrary search filter.
  private void showTimeline(SearchFilter filter) {
    for(Tweet tweet : api.getTimeline()) {
      if(filter.matches(tweet)) {
        printTweet(tweet);
      }
    }
  }
  
  // Search filters
  // --------------
  
  // Search filter that matches all tweets.
  private SearchFilter allTweets = new SearchFilter() {
    public boolean matches(Tweet tweet) {
      return true;
    }
  };
  
  // Create a search filter that matches tweets containing a text pattern.
  private SearchFilter byText(final String pattern) {
    return new SearchFilter() {
      public boolean matches(Tweet tweet) {
        return tweet.getText().contains(pattern);
      }
    };
  }

  // Create a search filter that matches tweets by an author.
  private SearchFilter byAuthor(final String pattern) {
    return new SearchFilter() {
      public boolean matches(Tweet tweet) {
        return tweet.getAuthor().contains(pattern);
      }
    };
  }

  // Create a search filter that is the disjunction of the arguments.
  private SearchFilter or(final SearchFilter... filters) {
    return new SearchFilter() {
      public boolean matches(Tweet tweet) {
        for(SearchFilter filter : filters) {
          if(filter.matches(tweet)) {
            return true;
          }
        }
        return false;
      }
    };
  }
  
  // Helpers
  // -------

  // Print a `Tweet` to stdout. 
  private void printTweet(Tweet tweet) {
    printPadded(tweet.getAuthor(), 20);
    
    String postcode = null;
    try {
      // Could return null
      Location location = tweet.getLocation();
      // Could return null
      postcode = postcodes.getNearestPostcode(location.getNgr());
    } catch(NullPointerException exn) {
      postcode = "Unknown";
    }
    printPadded(postcode, 20);
    println(tweet.getText().replaceAll("[\r\n]+", " "));
  }
  
  private void printPadded(String msg, int width) {
    print(msg);
    for(int i = msg.length(); i < width; i++) {
      print(" ");
    }
  }
  
}
