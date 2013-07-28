package tweetzor.core;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterClient {

  private Twitter twitter;

  public TwitterClient() {
    this.twitter = new TwitterFactory().getInstance();
  }

  // API calls
  // ---------

  // Get our home timeline as a list of `Tweets`.
  public List<Tweet> getTimeline() {
    try {
      return toTweets(twitter.getHomeTimeline());
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }
  }

  // Get the `user`'s timeline as a list of `Tweets`.
  public List<Tweet> getUserTimeline(String user) {
    try {
      return toTweets(twitter.getUserTimeline(user));
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }
  }

  // Get a list of retweets of the specified `Tweet`.
  public List<Tweet> getRetweets(Tweet tweet) {
    try {
      if (tweet.getRetweetCount() > 0) {
        return toTweets(twitter.getRetweets(tweet.getId()));
      } else {
        return new ArrayList<Tweet>();
      }
    } catch (TwitterException e) {
      throw new RuntimeException(e);
    }
  }

  // Helpers
  // -------
  
  private List<Tweet> toTweets(List<Status> list) {
    List<Tweet> ans = new ArrayList<Tweet>();
    for (Status status : list) {
      ans.add(new Tweet(status));
    }
    return ans;
  }

}
