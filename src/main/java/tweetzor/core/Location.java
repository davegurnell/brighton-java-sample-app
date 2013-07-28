package tweetzor.core;

import twitter4j.GeoLocation;

public class Location {
  
  // Fields
  // ------

  private double latitude;
  private double longitude;

  // Constructors
  // ------------

  public Location(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
  
  public Location(GeoLocation loc) {
    this.latitude = loc.getLatitude();
    this.longitude = loc.getLongitude();
  }

  // Getters
  // -------
  
  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

}