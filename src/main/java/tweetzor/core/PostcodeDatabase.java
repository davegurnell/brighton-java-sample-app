package tweetzor.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.untyped.location.Ngr;

// Database of postcodes. `databaseUrl` must be a path to a CSV file on the classpath.
// The CSV file must be in [OS Codepoint](http://www.ordnancesurvey.co.uk/oswebsite/products/code-point/index.html) format.
public class PostcodeDatabase {

  private String databaseUrl; 
  private List<NgrRecord> ngrRecords;
  
  public PostcodeDatabase(String databaseUrl) {
    this.databaseUrl = databaseUrl;
    this.ngrRecords = null;
  }

  // Find the closest postcode to `here`, or `null` if nothing is closer than `proximiyCutoff`.
  public String getNearestPostcode(Ngr here) {
    // If the database isn't load it, load it now.
    if(ngrRecords == null) {
      ngrRecords = loadPostcodeDatabase(databaseUrl);
    }

    // Build a list of `ProximityRecords`
    List<ProximityRecord> proximityRecords = new ArrayList<ProximityRecord>(ngrRecords.size());
    for(NgrRecord ngrRecord : ngrRecords) {
      ProximityRecord proximityRecord = proximityTo(here, ngrRecord);
      if(proximityRecord != null) {
        proximityRecords.add(proximityRecord);
      }
    }
    
    // Sort the `ProximityRecords` in ascending order of distance
    Collections.sort(proximityRecords);
    
    if(proximityRecords.size() == 0) {
      return null;
    }
    
    return proximityRecords.get(0).postcode;
  }
    
  // Proximity
  // ---------
    
  // Constant to determine how close a postcode needs to be before we consider it "nearby".
  // We look at it if the square of the distance (in easting/northing units) is less than this number. 
  private long proximityCutoff = 10000;
    
  // Return a `ProximityRecord` for this `there` relative to `here` if it is close enough.
  private ProximityRecord proximityTo(Ngr here, NgrRecord there) {
    long east = (long)( there.ngr.easting() - here.easting());
    long north = (long)( there.ngr.northing() - here.northing());
    long squareDist = east * east + north * north;
    if(squareDist < proximityCutoff) {
      return new ProximityRecord(-squareDist, there.postcode);
    } else {
      return null;
    }
  }
  
  // CSV parsing
  // -----------

  private static List<NgrRecord> loadPostcodeDatabase(String databaseUrl) {
    List<NgrRecord> ans = new ArrayList<NgrRecord>();
    BufferedReader in = new BufferedReader(new InputStreamReader(PostcodeDatabase.class.getResourceAsStream(databaseUrl)));
    
    try {
      while(in.ready()) {
        NgrRecord ngrRecord = parseNgrRecord(in.readLine());
        if(ngrRecord != null) {
          ans.add(ngrRecord);
        }
      }
  
      in.close();
      
      return ans;
    } catch(Exception exn) {
      return new ArrayList<NgrRecord>();
    }
  }

  private static NgrRecord parseNgrRecord(String line) {
    String[] parts = line.split(",");
    
    try {
      String postcode = parts[0].substring(1, parts[0].length() - 1);
      int easting = Integer.parseInt(parts[2]);
      int northing = Integer.parseInt(parts[3]);
      return new NgrRecord(new Ngr(easting, northing), postcode);
    } catch(Exception exn) {
      return null;
    }
  }

  // Helper classes
  // --------------

  // A record in the postcode database.     
  private static class NgrRecord {
    Ngr ngr;
    String postcode;
    public NgrRecord(Ngr ngr, String postcode) {
      this.ngr = ngr;
      this.postcode = postcode;
    }
  }
  
  // Proximity of an `NgrRecord` to a location.
  // Used to compare records for proximity and discard them if they're too far away. 
  private static class ProximityRecord implements Comparable<ProximityRecord> {
    long proximity;
    String postcode;
    public ProximityRecord(long proximity, String postcode) {
      this.proximity = proximity;
      this.postcode = postcode;
    }

    @Override
    public int compareTo(ProximityRecord that) {
      return (int)( that.proximity - this.proximity );
    } 
  }
  
}