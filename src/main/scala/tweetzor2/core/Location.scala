package tweetzor2.core

import twitter4j.GeoLocation

case class Location(
  val latitude: Double,
  val longitude: Double
)

object Location {
  def apply(loc: GeoLocation): Location = {
    Location(
      latitude = loc.getLatitude,
      longitude = loc.getLongitude
    )
  }
}