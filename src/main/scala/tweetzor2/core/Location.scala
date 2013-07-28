package tweetzor2.core

import twitter4j.GeoLocation
import com.untyped.location.Wgs84

// Class representing the GPS location of a tweet.
// See `Tweet.scala` for information about the `case class` keywords.
case class Location(
  val latitude: Double,
  val longitude: Double
) {
  def ngr = Wgs84(latitude, longitude, 0.0).toNgr
}

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