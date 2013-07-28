package com.untyped.location

import scala.math._

case class Osgb36(val lat: Double, val lng: Double, val height: Double) extends Coord {
  import CoordConversions._
  
  // Coordinate conversions
  // ----------------------
 
  // Convert to a WGS84 coordinate.
  def toWgs84: Wgs84 =
    convert(this, 
            Ellipse.airy1830, 
            Helmert.osgb36toWgs84,
            Ellipse.wgs84,
            Wgs84(_, _, _))
  
  // Convert to an OSGB36 coordinate.
  def toOsgb36: Osgb36 =
    this
  
  // Convert to a UK National Grid Reference.
  //
  // Code adapted from http://www.movable-type.co.uk/scripts/latlong-gridref.html
  def toNgr: Ngr = {
    val lat = toRadians(this.lat)
    val lng = toRadians(this.lng)
  
    val a = 6377563.396                            // Airy 1830 major & minor semi-axes
    val b = 6356256.910
    val F0 = 0.9996012717                          // NatGrid scale factor on central meridian
    val lat0 = toRadians(49)                       // NatGrid true origin
    val lng0 = toRadians(-2)
    val N0 = -100000                               // northing & easting of true origin, metres
    val E0 =  400000
    val e2 = 1 - (b*b)/(a*a)                       // eccentricity squared
    val n = (a-b)/(a+b)
    val n2 = n*n
    val n3 = n*n*n

    val cosLat = cos(lat)
    val sinLat = sin(lat) 
    val nu = a*F0/sqrt(1-e2*sinLat*sinLat)               // transverse radius of curvature
    val rho = a*F0*(1-e2)/pow(1-e2*sinLat*sinLat, 1.5)   // meridional radius of curvature
    val eta2 = nu/rho-1 

    val Ma = (1 + n + (5/4)*n2 + (5/4)*n3) * (lat-lat0) 
    val Mb = (3*n + 3*n*n + (21/8)*n3) * sin(lat-lat0) * cos(lat+lat0) 
    val Mc = ((15/8)*n2 + (15/8)*n3) * sin(2*(lat-lat0)) * cos(2*(lat+lat0)) 
    val Md = (35/24)*n3 * sin(3*(lat-lat0)) * cos(3*(lat+lat0)) 
    val M = b * F0 * (Ma - Mb + Mc - Md)                      // meridional arc

    val cos3lat = cosLat*cosLat*cosLat 
    val cos5lat = cos3lat*cosLat*cosLat 
    val tan2lat = tan(lat)*tan(lat) 
    val tan4lat = tan2lat*tan2lat 

    val I = M + N0 
    val II = (nu/2)*sinLat*cosLat 
    val III = (nu/24)*sinLat*cos3lat*(5-tan2lat+9*eta2) 
    val IIIA = (nu/720)*sinLat*cos5lat*(61-58*tan2lat+tan4lat) 
    val IV = nu*cosLat 
    val V = (nu/6)*cos3lat*(nu/rho-tan2lat) 
    val VI = (nu/120) * cos5lat * (5 - 18*tan2lat + tan4lat + 14*eta2 - 58*tan2lat*eta2) 

    val dLng = lng-lng0 
    val dLng2 = dLng*dLng
    val dLng3 = dLng2*dLng
    val dLng4 = dLng3*dLng
    val dLng5 = dLng4*dLng
    val dLng6 = dLng5*dLng 

    val N = I + II*dLng2 + III*dLng4 + IIIA*dLng6 
    val E = E0 + IV*dLng + V*dLng3 + VI*dLng5 

    Ngr(E.toInt, N.toInt)
  }
  
  // Format latitude and longitude to 6DP.
  override def toString: String =
    "Osgb36(%.6f,%.6f)".format(lat, lng)
}
