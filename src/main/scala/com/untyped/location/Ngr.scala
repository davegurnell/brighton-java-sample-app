package com.untyped.location

import scala.math._
import scala.util.matching.Regex
import scala.annotation.tailrec

case class Ngr(val easting: Int, val northing: Int) {
  def meta = Ngr
  
  // Coordinate conversions
  // ----------------------
  
  // Convert to a WGS84 coordinate.
  def toWgs84: Wgs84 =
    toOsgb36.toWgs84
  
  // Convert to an OSGB36 coordinate.
  //
  // Code adapted from http://www.movable-type.co.uk/scripts/latlong-gridref.html
  def toOsgb36: Osgb36 = {
    val E = easting
    val N = northing

    val a = 6377563.396                                // Airy 1830 major & minor semi-axes
    val b = 6356256.910
    val F0 = 0.9996012717                              // NatGrid scale factor on central meridian
    val lat0 = 49.0*Pi/180                             // NatGrid true origin
    val lng0 = -2.0*Pi/180
    val N0 = -100000.0                                 // northing & easting of true origin, metres
    val E0 =  400000.0
    val e2 = 1 - (b*b)/(a*a)                           // eccentricity squared
    var n = (a-b)/(a+b)
    val n2 = n*n
    val n3 = n*n*n

    var lat=lat0
    var M = 0.0
    do {
      lat = (N-N0-M)/(a*F0) + lat

      val Ma = (1 + n + (5/4)*n2 + (5/4)*n3) * (lat-lat0)
      val Mb = (3*n + 3*n*n + (21/8)*n3) * sin(lat-lat0) * cos(lat+lat0)
      val Mc = ((15/8)*n2 + (15/8)*n3) * sin(2*(lat-lat0)) * cos(2*(lat+lat0))
      val Md = (35/24)*n3 * sin(3*(lat-lat0)) * cos(3*(lat+lat0))
      M = b * F0 * (Ma - Mb + Mc - Md)                 // meridional arc

    } while (N-N0-M >= 0.00001)                        // ie until < 0.01mm

    val cosLat = cos(lat)
    val sinLat = sin(lat)
    val nu = a*F0/sqrt(1-e2*sinLat*sinLat)               // transverse radius of curvature
    val rho = a*F0*(1-e2)/pow(1-e2*sinLat*sinLat, 1.5)   // meridional radius of curvature
    val eta2 = nu/rho-1

    val tanLat = tan(lat)
    val tan2lat = tanLat*tanLat
    val tan4lat = tan2lat*tan2lat
    val tan6lat = tan4lat*tan2lat
    val secLat = 1/cosLat
    val nu3 = nu*nu*nu
    val nu5 = nu3*nu*nu
    val nu7 = nu5*nu*nu;
    val VII = tanLat/(2*rho*nu)
    val VIII = tanLat/(24*rho*nu3)*(5+3*tan2lat+eta2-9*tan2lat*eta2)
    val IX = tanLat/(720*rho*nu5)*(61+90*tan2lat+45*tan4lat)
    val X = secLat/nu
    val XI = secLat/(6*nu3)*(nu/rho+2*tan2lat)
    val XII = secLat/(120*nu5)*(5+28*tan2lat+24*tan4lat)
    val XIIA = secLat/(5040*nu7)*(61+662*tan2lat+1320*tan4lat+720*tan6lat)

    val dE = (E-E0)
    val dE2 = dE*dE
    val dE3 = dE2*dE
    val dE4 = dE2*dE2
    val dE5 = dE3*dE2
    val dE6 = dE4*dE2
    val dE7 = dE5*dE2
    
    lat = lat - VII*dE2 + VIII*dE4 - IX*dE6
    var lng = lng0 + X*dE - XI*dE3 + XII*dE5 - XIIA*dE7

    Osgb36(toDegrees(lat), toDegrees(lng), 8)
  }
  
  // String format
  // -------------
  
  // Format as a string of the specified `precision`.
  // Can return `None` if the grid reference is off-grid.
  def format(precision: Ngr.Precision): Option[String] = {
    // get the 100km-grid indices
    var e100k = floor(easting / 100000)
    val n100k = floor(northing / 100000);
  
    if (e100k<0 || e100k>6 || n100k<0 || n100k>12) {
      None
    } else {
      // translate those into numeric equivalents of the grid letters
      var l1 = (19-n100k) - (19-n100k)%5 + floor((e100k+10)/5);
      var l2 = (19-n100k)*5%25 + e100k%5;

      // compensate for skipped 'I' and build grid letter-pairs
      if (l1 > 7) l1 = l1 + 1
      if (l2 > 7) l2 = l2 + 1
      val letPair = "" + (l1+'A'.toInt).toChar + (l2+'A'.toInt).toChar

      // strip 100km-grid indices from easting & northing, and reduce precision
      val e: Int = ( (easting % 100000) / pow(10,5-precision.digits/2) ).toInt
      val n: Int = ( (northing % 100000) / pow(10,5-precision.digits/2) ).toInt

      Some(letPair + 
           Ngr.padLeft(e.toString, precision.digits/2, "0") + 
           Ngr.padLeft(n.toString, precision.digits/2, "0"))
    }
  }
  
  override def toString =
    format(Ngr.TenDigitPrecision).getOrElse(super.toString)
}
  
object Ngr {
  
  // Precisions
  // ----------
  
  // Grid references can be from 2 to 10 digits.
  //
  // This set of classes, used in `unapply` and `parse` below,
  // represents the various levels available. 
  
  sealed case class Precision(val digits: Int, val regex: Regex) {
    val gridrefLength = digits + 2
    
    def unapplySeq(str: String): Option[List[String]] = regex.unapplySeq(str)
    
    def isValidNgr(str: String): Boolean =
      !regex.unapplySeq(str).isEmpty
  }
  
  object TwoDigitPrecision extends Precision(2, """^([A-Z]{2})([0-9]{1})([0-9]{1})""".r)
  object FourDigitPrecision extends Precision(4, """^([A-Z]{2})([0-9]{2})([0-9]{2})""".r)
  object SixDigitPrecision extends Precision(6, """^([A-Z]{2})([0-9]{3})([0-9]{3})""".r)
  object EightDigitPrecision extends Precision(8, """^([A-Z]{2})([0-9]{4})([0-9]{4})""".r)
  object TenDigitPrecision extends Precision(10, """^([A-Z]{2})([0-9]{5})([0-9]{5})""".r)
  
  val precisions =
    List(TwoDigitPrecision,
         FourDigitPrecision,
         SixDigitPrecision,
         EightDigitPrecision,
         TenDigitPrecision)

  val commonPrecisions =
    List(SixDigitPrecision,
         EightDigitPrecision,
         TenDigitPrecision)

  // Parsing
  // -------
         
  // Parse a 6, 8, or 10 digit `gridref` as an `Ngr`.
  def unapply(gridref: String): Option[Ngr] =
    parse(gridref)
  
  // Parse `gridref` as an `Ngr` given a list of valid precisions.
  def parse(gridref: String, precisions: List[Precision] = commonPrecisions): Option[Ngr] =
    for {
      precision                <- precisions.find(_.gridrefLength == gridref.length)
      List(alpha, east, north) <- precision.unapplySeq(gridref.toUpperCase)
    } yield parseParts(alpha, padToFiveDigits(east), padToFiveDigits(north))
  
  // Convert the Easting or Northing from an area grid ref (< 5 digits)
  // to an Easting or Northing from a 1m grid ref (5 digits). e.g.:
  //
  //     "1" -> "15000"
  //     "12" -> "12500"
  //     "123" -> "12350"
  //     "1234" -> "12345"
  private def padToFiveDigits(str: String): String =
    if(str.length >= 5) str else padRight(str + "5", 5, "0")
  
  // Parse the three components of a 10 digit grid reference
  // (2 alpha, 5 digit easting, and 5 digit northing) as an `Ngr`.
  private def parseParts(alpha: String, east: String, north: String): Ngr = {
    // get numeric values of letter references, mapping A->0, B->1, C->2, etc:
    var l1 = alpha(0).toInt - 'A'.toInt
    var l2 = alpha(1).toInt - 'A'.toInt
    
    // shuffle down letters after 'I' since 'I' is not used in the alpha grid:
    if (l1 > 7) l1 = l1 - 1
    if (l2 > 7) l2 = l2 - 1

    // convert grid letters into 100km-square indexes from false origin (grid square SV):
    var alphaE: Int = 100000 * (((l1-2)%5)*5 + (l2%5)).toInt
    var alphaN: Int = 100000 * (19 - floor(l1/5)*5 - floor(l2/5)).toInt

    val numericE = Integer.parseInt(east)
    val numericN = Integer.parseInt(north)

    Ngr(alphaE + numericE, alphaN + numericN)
  }
  
  @tailrec
  private final def padLeft(str: String, len: Int, padWith: String): String =
    if(str.length >= len) str else padLeft(padWith + str, len, padWith)

  @tailrec
  private final def padRight(str: String, len: Int, padWith: String): String =
    if(str.length >= len) str else padRight(str + padWith, len, padWith)

}
