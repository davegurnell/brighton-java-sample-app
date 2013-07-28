package com.untyped.location

// Algorithms in this file stolen from code in and around:
// http://www.movable-type.co.uk/scripts/latlong-gridref.html

import scala.math._

case class Wgs84(val lat: Double, val lng: Double, val height: Double) extends Coord {
  import CoordConversions._
  
  // Conversions
  // -----------
  
  // Convert to a WGS84 coordinate.
  def toWgs84: Wgs84 =
    this
  
  // Convert to an OSGB36 coordinate.
  def toOsgb36: Osgb36 =
    convert(this,
            Ellipse.wgs84, 
            Helmert.wgs84ToOsgb36,
            Ellipse.airy1830,
            Osgb36(_, _, _))
  
  // Convert to a UK National Grid Reference.
  def toNgr: Ngr =
    toOsgb36.toNgr
  
  // Format latitude and longitude to 6DP.
  override def toString: String =
    "Wgs84(%.6f,%.6f)".format(lat, lng)
  
}

object Wgs84 {
    
  // Parsing coordinates
  // -------------------

  // Attempt to parse a `String` as a `Wgs84`.
  def unapply(str: String): Option[Wgs84] =
    str.split(",") match {
      case Array(ParseDouble(lat), ParseDouble(lng)) =>
        Some(Wgs84(lat, lng, 0.0))
      case _ => None
    }
  
  // Helpers
  // -------

  // Extractor for values of type `Double`. Used in `Wgs84.unapply` above.
  private object ParseDouble {
    def unapply(str: String): Option[Double] = {
      try {
        Some(str.toDouble)
      } catch {
        case exn: NumberFormatException => None
      }
    }
  }
  

}
