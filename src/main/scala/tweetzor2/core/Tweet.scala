package tweetzor2.core

import twitter4j.Status

// The `case class` keywords generate a class with a bunch of predefined convenience functionality:
// 
// * primary constructor;
// * accessors for all fields;
// * mutators for all mutable fields;
// * `copy` method for functional updates;
// * working `equals` and `hashCode` methods;
// * sensible `toString` method;
// * companion object with `apply` and `unapply` methods.
case class Tweet(
  val id: Long,
  val author: String,
  val text: String,
  val retweetCount: Long,
  val location: Option[Location]
)

// The `object` keyword defines a singleton object.
// In this case, we are defining a *companion object* and factory method for the `Tweet` class.
//
// Singleton objects provide a replacement for static fields and methods.
// They have the advantage of being fully fledged objects that can extend a class.
//
// Companion classes and objects have special scoping privileges 
// that make them well suited for factory-like patterns.
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
