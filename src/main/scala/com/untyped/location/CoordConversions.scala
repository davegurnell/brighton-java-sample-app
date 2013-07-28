package com.untyped.location

import scala.math._

// Helpers for converting between different GPS coordinate systems.
// Code adapted from http://www.movable-type.co.uk/scripts/latlong-gridref.html
object CoordConversions {

  // Ellipses
  // --------
  
  case class Ellipse(val a: Double, val b: Double, val f: Double) {
    lazy val aSquare = a*a
    lazy val bSquare = b*b
    lazy val eSquare = (aSquare - bSquare) / aSquare
    
    lazy val precision = 4 / a // about 4m
    
    def polarToCart(phi: Double, lambda: Double, h: Double): (Double, Double, Double) = {
      val cosPhi = cos(phi)
      val sinPhi = sin(phi)
      val cosLambda = cos(lambda)
      val sinLambda = sin(lambda)
      
      val v = a / sqrt(1 - eSquare * sinPhi * sinPhi)
      
      val x = (v + h) * cosPhi * cosLambda
      val y = (v + h) * cosPhi * sinLambda
      val z = ( (1 - eSquare) * v + h ) * sinPhi
      
      (x, y, z)
    }
    
    def cartToPolar(x: Double, y: Double, z: Double): (Double, Double, Double) = {
      val p = sqrt(x * x + y * y)
      
      var phi = atan2(z, p * (1 - eSquare))
      var phiPrime = 2 * Pi
      
      var sinPhi = sin(phi)
      var nu = a / sqrt(1 - eSquare * sinPhi * sinPhi)
      
      while(phi - phiPrime > precision) {
        sinPhi = sin(phi)
        
        nu = a / sqrt(1 - eSquare * sinPhi * sinPhi)

        phiPrime = phi
        phi = atan2(z + eSquare * nu * sinPhi, p)
      }
      
      val lambda = atan2(y, x)
      val h = p / cos(phi) - nu
      
      (phi, lambda, h)
    }
  }

  object Ellipse {
    val wgs84 = Ellipse(6378137, 6356752.3142, 1/298.257223563)
    val airy1830 = Ellipse(6377563.396, 6356256.910, 1/299.3249646)
  }
  
  // Helmerts
  // --------

  case class Helmert(
    val tx: Double, val ty: Double, val tz: Double,
    val rx: Double, val ry: Double, val rz: Double, 
    val s: Double)
  
  object Helmert {
    val wgs84ToOsgb36 = Helmert(
      -446.448,   125.157,  -542.060,  // m
        -0.1502,   -0.2470,   -0.8421, // sec
        20.4894)                       // ppm
    val osgb36toWgs84 = Helmert(        
       446.448,  -125.157,   542.060,  // m
         0.1502,    0.2470,    0.8421, // sec
       -20.4894)                       // ppm
  }
  
  // Conversions
  // -----------
  
  def convert[Ans](
    p1: Coord,
    e1: Ellipse,
    t: Helmert,
    e2: Ellipse,
    makeAns: (Double, Double, Double) => Ans
  ): Ans = {
    // convert polar to cartesian coordinates (using ellipse 1)
    
    val (x1, y1, z1) = e1.polarToCart(toRadians(p1.lat), toRadians(p1.lng), p1.height)

    // apply helmert transform using appropriate params
  
    val tx = t.tx
    val ty = t.ty
    val tz = t.tz
    val rx = t.rx/3600.0 * math.Pi/180.0 // normalise seconds to radians
    val ry = t.ry/3600.0 * math.Pi/180.0
    val rz = t.rz/3600.0 * math.Pi/180.0
    val s1 = t.s/1e6 + 1                 // normalise ppm to (s+1)

    // apply transform
    val x2 = tx + x1*s1 - y1*rz + z1*ry
    val y2 = ty + x1*rz + y1*s1 - z1*rx
    val z2 = tz - x1*ry + y1*rx + z1*s1

    // convert cartesian to polar coordinates (using ellipse 2)

    val (phi, lambda, h) = e2.cartToPolar(x2, y2, z2)
    
    makeAns(toDegrees(phi), toDegrees(lambda), h)
  }

}
