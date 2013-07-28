package tweetzor.core;

import twitter4j.Status;

public class Tweet {
  
  // Fields
  // ------

  // * Unique tweet ID.
  private long id;
  
  // * Author's full name.
  private String author;
  
  // * Body text
  private String text;

  // * Number of retweets.
  private long retweetCount;

  // * GPS location of the tweet (may by `null`).
  private Location location;
  
  // Constructors
  // ------------

  public Tweet(long id, String author, String text, long retweetCount, Location location) {
    this.id = id;
    this.author = author;
    this.text = text;
    this.retweetCount = retweetCount;
    this.location = location;
  }

  public Tweet(Status status) {
    this.id = status.getId();
    this.author = status.getUser().getName();
    this.text = status.getText();
    this.retweetCount = status.getRetweetCount();
    this.location = status.getGeoLocation() == null ? null : new Location(status.getGeoLocation());
  }

  // Getters
  // -------

  
  public long getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }

  public long getRetweetCount() {
    return retweetCount;
  }

  public Location getLocation() {
    return location;
  }
 
}