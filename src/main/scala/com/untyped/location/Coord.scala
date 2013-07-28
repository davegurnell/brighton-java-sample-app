package com.untyped.location

trait Coord {
  def lat: Double
  def lng: Double
  def height: Double

  // Coordinate conversions
  // ----------------------
  
  // Convert this coordinate to a WGS84 coordinate.
  def toWgs84: Wgs84

  // Convert this coordinate to an OSGB36 coordinate.
  def toOsgb36: Osgb36

  // Convert this coordinate to a UK National Grid Reference.
  def toNgr: Ngr
  
}
