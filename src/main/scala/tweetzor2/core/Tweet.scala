package tweetzor2.core

import twitter4j.Status

// The `case class` keywords produce a class with a bunch of predefined functionality:
// 
// * a default constructor;
// * a set of accessors (and mutators where applicable);
// * a `toString` method;
// * a working implementation `equals` and `hashCode`;
// * a companion object with `apply` and `unapply` methods.
case class Tweet(
  val id: Long,
  val author: String,
  val text: String,
  val retweetCount: Long,
  val location: Option[Location]
)

// The `object` keyword defines a singleton object. In this case,
// because of its name, a *companion object* for the `Tweet` class.
//
// Sala uses singleton and companion objects as a replacement for 
// static fields and methods. Special scoping rules make companion 
// objects are well suited for factory-like patterns.
object Tweet {
  def apply(status: Status): Tweet = {
    Tweet(
      id = status.getId,
      author = status.getUser.getName,
      text = status.getText,
      retweetCount = status.getRetweetCount,
      location = Option(status.getGeoLocation).map(Location.apply _)
    )
  }
}
