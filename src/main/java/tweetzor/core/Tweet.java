package tweetzor.core;

import twitter4j.Status;

public class Tweet {

  private long id;
  private String author;
  private String text;
  private long retweetCount;
  private Location location;

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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public long getRetweetCount() {
    return retweetCount;
  }

  public void setRetweetCount(long retweetCount) {
    this.retweetCount = retweetCount;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
  
}