package tweetzor.core;

import twitter4j.GeoLocation;

import com.untyped.location.Ngr;
import com.untyped.location.Wgs84;

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
  
  public Ngr getNgr() {
    return new Wgs84(latitude, longitude, 0.0).toNgr();
  }

}