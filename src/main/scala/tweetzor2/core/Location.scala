package tweetzor2.core

import twitter4j.GeoLocation

// Class representing the GPS location of a tweet.
// See `Tweet.scala` for information about the `case class` keywords.
case class Location(
  val latitude: Double,
  val longitude: Double
)

// Companion object and factory method for the `Location` class. 
// See `Tweet.scala` for information about the `object` keyword.
object Location {
  def apply(loc: GeoLocation): Location = {
    Location(
      latitude = loc.getLatitude,
      longitude = loc.getLongitude
    )
  }
}