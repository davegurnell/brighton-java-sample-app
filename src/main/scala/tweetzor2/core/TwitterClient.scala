package tweetzor2.core

// Rename on import to avoid a naming conflict with `scala.List`.
import java.util.{List => JavaList}

import twitter4j.Status
import twitter4j.TwitterFactory

class TwitterClient() {

  val twitter = new TwitterFactory().getInstance()

  // API calls
  // ---------

  // Get the user's timeline as a list of Tweets.
  def timeline: List[Tweet] = {
    toTweets(twitter.getHomeTimeline())
  }

  // Get a tweet's retweets.
  def retweets(tweet: Tweet): List[Tweet] = {
    if (tweet.retweetCount == 0) {
      Nil
    } else {
      toTweets(twitter.getRetweets(tweet.id))
    }
  }
  
  // Helpers
  // -------
  
  private def toTweets(javaList: JavaList[Status]): List[Tweet] = {
    // Implicit conversions from Java to Scala collections.
    // These allow us to write `javaList.toList` below.
    import scala.collection.JavaConversions._

    // `javaList.toList` converts the argument to a `scala.List[Status]`.
    // `map` converts each `Status` to a `Tweet`.
    javaList.toList.map(status => Tweet(status))
  }

}
